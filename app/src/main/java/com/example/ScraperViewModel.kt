// نام فایل: ScraperViewModel.kt
// مسیر فایل: app/src/main/java/com/example/ScraperViewModel.kt
package com.example
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit

enum class FallbackChoice {
    RETRY,
    USE_LOCAL_ALL,
    CANCEL
}

sealed interface CrawlUiState {
    object Idle : CrawlUiState
    // پارامتر logs اضافه شد تا در مرحله آنالیز هم لاگ‌ها نمایش داده شوند
    data class Scanning(val baseUrl: String, val message: String, val logs: List<String>) : CrawlUiState
    data class Converting(
        val baseUrl: String,
        val foundLinks: List<String>,
        val currentIndex: Int,
        val total: Int,
        val currentLink: String,
        val logs: List<String>
    ) : CrawlUiState
    data class Success(
        val baseUrl: String,
        val totalConverted: Int,
        val savedUri: Uri?,
        val fileName: String,
        val fullContent: String,
        val logs: List<String>
    ) : CrawlUiState
    data class Error(val message: String) : CrawlUiState
    data class WaitingForFallback(
        val baseUrl: String,
        val currentLink: String,
        val foundLinks: List<String>,
        val currentIndex: Int,
        val total: Int,
        val logs: List<String>,
        val errorMessage: String = ""
    ) : CrawlUiState
}

data class RecentExtractionItem(
    val id: String = UUID.randomUUID().toString(),
    val fileName: String,
    val timeAgo: String,
    val sizeString: String,
    val fullContent: String,
    val sourceUrl: String,
    val savedUri: String? = null
)

enum class SpeedMode {
    AUTO_THROTTLE,
    CUSTOM_DELAY,
    FAST_NO_DELAY
}

data class ScraperConfig(
    val jinaApiKey: String = "",
    val speedMode: SpeedMode = SpeedMode.AUTO_THROTTLE,
    val customDelaySeconds: Float = 1.0f,
    val maxPageLimit: Int = 30,
    val excludeKeywords: String = "logout,login,signin,signout,signup,register,admin,dashboard,profile,account,cart,checkout,buy,pay,basket,billing,pricing,feedback,contact,support,help,faq,search,terms,privacy,cookie,policy,share,rss,feed,subscribe,newsletter",
    val noImages: Boolean = true,
    val noCookies: Boolean = true,
    val appLanguage: String = "en",
    val deepScrape: Boolean = false,
    val targetSelector: String = "",
    val removeSelector: String = "",
    val returnFormat: String = "markdown"
)

class ScraperViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<CrawlUiState>(CrawlUiState.Idle)
    val uiState: StateFlow<CrawlUiState> = _uiState.asStateFlow()

    private val _recentExtractions = MutableStateFlow<List<RecentExtractionItem>>(emptyList())
    val recentExtractions: StateFlow<List<RecentExtractionItem>> = _recentExtractions.asStateFlow()

    private val _config = MutableStateFlow(ScraperConfig())
    val config: StateFlow<ScraperConfig> = _config.asStateFlow()

    private var scraperJob: Job? = null

    // افزایش زمان Timeout برای جلوگیری از خطای قطع اتصال ناگهانی در مواجهه با صفحات کند
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val sharedPreferences = application.getSharedPreferences("scraper_prefs", Context.MODE_PRIVATE)
    private var fallbackChoiceDeferred: CompletableDeferred<FallbackChoice>? = null

    init {
        loadDataFromPrefs()
    }

    private fun loadDataFromPrefs() {
        val configJson = sharedPreferences.getString("config_data", null)
        if (configJson != null) {
            _config.value = parseConfig(configJson)
        }
        val historyJson = sharedPreferences.getString("history_data", null)
        if (historyJson != null) {
            _recentExtractions.value = parseHistory(historyJson)
        }
    }

    private fun saveConfigToPrefs(newConfig: ScraperConfig) {
        sharedPreferences.edit().putString("config_data", configToJsonString(newConfig)).apply()
    }

    private fun saveHistoryToPrefs(historyList: List<RecentExtractionItem>) {
        sharedPreferences.edit().putString("history_data", historyToJsonString(historyList)).apply()
    }

    fun validateUrl(url: String): Boolean {
        val trimmed = url.trim()
        return trimmed.startsWith("http://", ignoreCase = true) || trimmed.startsWith("https://", ignoreCase = true)
    }

    fun updateConfig(newConfig: ScraperConfig) {
        _config.value = newConfig
        saveConfigToPrefs(newConfig)
    }

    fun clearHistory() {
        _recentExtractions.value = emptyList()
        saveHistoryToPrefs(emptyList())
    }

    fun deleteExtraction(context: Context, item: RecentExtractionItem) {
        val newList = _recentExtractions.value.filter { it.id != item.id }
        _recentExtractions.value = newList
        saveHistoryToPrefs(newList)
        item.savedUri?.let { uriStr ->
            try {
                val uri = Uri.parse(uriStr)
                if (uri.scheme == "content") {
                    context.contentResolver.delete(uri, null, null)
                } else if (uri.scheme == "file") {
                    val file = File(uri.path ?: "")
                    if (file.exists()) {
                        file.delete()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun respondToFallbackChoice(choice: FallbackChoice) {
        fallbackChoiceDeferred?.complete(choice)
    }

    fun startScraping(context: Context, rawUrl: String) {
        val cleanUrl = rawUrl.trim()
        if (!validateUrl(cleanUrl)) {
            _uiState.value = CrawlUiState.Error("Invalid URL! URL must start with http or https.")
            return
        }
        scraperJob?.cancel()
        scraperJob = viewModelScope.launch(Dispatchers.IO) {
            val logsList = mutableListOf<String>()

            // تابع کمکی برای ثبت لاگ و به‌روزرسانی زنده رابط کاربری
            fun logMsg(msg: String) {
                logsList.add(msg)
                when (val state = _uiState.value) {
                    is CrawlUiState.Scanning -> _uiState.value = state.copy(message = msg, logs = logsList.toList())
                    is CrawlUiState.Converting -> _uiState.value = state.copy(logs = logsList.toList())
                    is CrawlUiState.WaitingForFallback -> _uiState.value = state.copy(logs = logsList.toList())
                    else -> {}
                }
            }

            _uiState.value = CrawlUiState.Scanning(cleanUrl, "Initializing...", emptyList())

            try {
                val currentConfig = _config.value
                logMsg("🚀 Starting compilation using Dynamic Engine Configuration:")
                logMsg("⚙️ Pacing Mode: ${currentConfig.speedMode}")
                logMsg("📄 Limit Cap: ${currentConfig.maxPageLimit} maximum pages")

                if (currentConfig.jinaApiKey.isNotBlank()) {
                    logMsg("🔑 API Key auth: Enabled (No Jina speed/tier caps!)")
                } else {
                    logMsg("🔑 API Key auth: Free-Tier mode (Applying safety pacing)")
                }

                val finalLinks = if (!currentConfig.deepScrape) {
                    logMsg("📍 Single Page mode active: converting only the entry URL.")
                    listOf(cleanUrl)
                } else {
                    logMsg("🩺 Spawning native hyper-link parser to index files...")
                    logMsg("🔗 Entry Point: $cleanUrl")
                    logMsg("🔍 Gathering index anchors based on Jsoup selectors...")
                    
                    val subLinks = getSubLinks(cleanUrl, ::logMsg)
                    logMsg("📌 Scrapy: Identified ${subLinks.size} nested anchor hyper-references.")
                    
                    val excludeList = currentConfig.excludeKeywords
                        .split(",")
                        .map { it.trim().lowercase() }
                        .filter { it.isNotEmpty() }
                        
                    val filteredLinks = if (excludeList.isNotEmpty()) {
                        subLinks.filter { link ->
                            val lowerLink = link.lowercase()
                            !excludeList.any { lowerLink.contains(it) }
                        }
                    } else {
                        subLinks
                    }
                    
                    if (excludeList.isNotEmpty() && subLinks.size != filteredLinks.size) {
                        logMsg("🧹 Filter: Excluded ${subLinks.size - filteredLinks.size} URLs containing filter keywords.")
                    }
                    
                    if (filteredLinks.isEmpty()) {
                        logMsg("⚠ No sub-links matching directory patterns. Falling back to solo URL index.")
                        listOf(cleanUrl)
                    } else {
                        if (filteredLinks.size > currentConfig.maxPageLimit) {
                            logMsg("✂️ Limit: List restricted to ${currentConfig.maxPageLimit} pages (Maximum limit limit).")
                            filteredLinks.take(currentConfig.maxPageLimit)
                        } else {
                            filteredLinks
                        }
                    }
                }
                
                val totalCount = finalLinks.size
                logMsg("✅ Total pages prepared for scraping: $totalCount")

                val pagesList = mutableListOf<ScrapedPage>()
                val combinedBuilder = StringBuilder()
                combinedBuilder.append("<!-- Compiled with URL to Markdown Android App -->\n")
                combinedBuilder.append("<!-- Dynamic configs - SpeedMode: ${currentConfig.speedMode} -->\n")
                combinedBuilder.append("<!-- Source Directory Scraped: $cleanUrl -->\n")
                combinedBuilder.append("<!-- Compiled on: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())} -->\n\n")

                val dynamicSleepMs = when (currentConfig.speedMode) {
                    SpeedMode.AUTO_THROTTLE -> {
                        if (totalCount <= 20) {
                            1000L
                        } else {
                            val computedDelaySeconds = 1.0 + (3.0 - 1.0) * (totalCount - 20) / 20.0
                            val clampedDelaySeconds = if (computedDelaySeconds > 3.0) 3.0 else computedDelaySeconds
                            (clampedDelaySeconds * 1000.0).toLong()
                        }
                    }
                    SpeedMode.CUSTOM_DELAY -> {
                        (currentConfig.customDelaySeconds * 1000.0).toLong()
                    }
                    SpeedMode.FAST_NO_DELAY -> {
                        0L
                    }
                }
                
                val delayInSeconds = dynamicSleepMs / 1000.0
                if (dynamicSleepMs > 0) {
                    logMsg("⏱️ Delay interval set smoothly to ${delayInSeconds}s between requests.")
                } else {
                    logMsg("⚡ Burst mode active: Zero-delay crawling (requires API key to bypass Jina rate limits).")
                }

                var forceLocalFallback = false
                
                for (index in finalLinks.indices) {
                    if (!isActive) {
                        logMsg("❌ Operation terminated. System clean.")
                        _uiState.value = CrawlUiState.Idle
                        return@launch
                    }
                    
                    val currentLink = finalLinks[index]
                    val visualIndex = index + 1
                    var attemptSuccess = false
                    var skipLink = false
                    val attemptStartTime = System.currentTimeMillis()
                    var lastErrorDetails = ""
                    
                    while (!attemptSuccess && !skipLink) {
                        if (!isActive) {
                            logMsg("❌ Operation terminated. System clean.")
                            _uiState.value = CrawlUiState.Idle
                            return@launch
                        }
                        
                        _uiState.value = CrawlUiState.Converting(
                            baseUrl = cleanUrl,
                            foundLinks = finalLinks,
                            currentIndex = index,
                            total = totalCount,
                            currentLink = currentLink,
                            logs = logsList.toList()
                        )
                        
                        logMsg("[$visualIndex/$totalCount] Processing download pipeline: $currentLink")
                        
                        var pageContent = ""
                        var downloadSuccess = false
                        
                        if (forceLocalFallback) {
                            logMsg("🔄 [Forced Local] Processing via Local JSoup fallback engine...")
                            try {
                                val doc = Jsoup.connect(currentLink)
                                    .userAgent("Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36")
                                    .timeout(10000) // 10 seconds timeout for Local fallback
                                    .ignoreContentType(true)
                                    .get()
                                    
                                val docTitle = doc.title().trim()
                                val body = doc.body()
                                val localMarkdownBuilder = StringBuilder()
                                localMarkdownBuilder.append("# $docTitle\n\n")
                                
                                if (body != null) {
                                    val elements = body.select("h1, h2, h3, h4, h5, h6, p, li, pre, code")
                                    for (element in elements) {
                                        val tagName = element.tagName()
                                        val text = element.text().trim()
                                        if (text.isEmpty()) continue
                                        when {
                                            tagName.startsWith("h") -> {
                                                val level = tagName.substring(1).toIntOrNull() ?: 2
                                                val prefix = "#".repeat(level)
                                                localMarkdownBuilder.append("$prefix $text\n\n")
                                            }
                                            tagName == "p" -> {
                                                localMarkdownBuilder.append("$text\n\n")
                                            }
                                            tagName == "li" -> {
                                                localMarkdownBuilder.append("- $text\n")
                                            }
                                            tagName == "pre" || tagName == "code" -> {
                                                if (element.parent()?.tagName() != "pre" || tagName == "pre") {
                                                    localMarkdownBuilder.append("```\n$text\n```\n\n")
                                                }
                                            }
                                        }
                                    }
                                }
                                val fallbackMarkdown = localMarkdownBuilder.toString()
                                if (fallbackMarkdown.length > docTitle.length + 12) {
                                    pageContent = fallbackMarkdown
                                    downloadSuccess = true
                                    logMsg("✅ Webpage download response success index [$visualIndex] (via Local fallback JSoup)")
                                } else {
                                    lastErrorDetails = "Local fallback got too short content (Too short) / محتوای استخراج‌شده محلی بسیار کوتاه یا نامعتبر است"
                                    logMsg("❌ Local fallback got too short content for index [$visualIndex]")
                                }
                            } catch (e: Exception) {
                                val errType = e.javaClass.name
                                val errDetail = when {
                                    errType.contains("HttpStatusException") -> "HTTP Status Error (Target server returned an error status code) / خطای وضعیت وب‌سایت مقصد"
                                    errType.contains("UnsupportedMimeTypeException") -> "Unsupported media type (Target page contents might be PDF/binary instead of HTML) / فرمت نامعتبر محتوا"
                                    e is java.net.UnknownHostException -> "DNS Resolution Failure (Could not resolve website host address) / عدم امکان اتصال به سرور مقصد"
                                    e is java.net.SocketTimeoutException -> "Local Connection Timeout (Target host is too slow to load or down) / اتمام زمان ارتباط محلی"
                                    else -> e.localizedMessage ?: e.message ?: "Unknown Local Connection Error"
                                }
                                lastErrorDetails = errDetail
                                logMsg("❌ Local fallback failed: $errDetail for index [$visualIndex]")
                            }
                        } else {
                            val jinaUrl = "https://r.jina.ai/$currentLink"
                            try {
                                val requestBuilder = Request.Builder()
                                    .url(jinaUrl)
                                    .header("User-Agent", "Mozilla/5.0 (Linux; Android 10; K)")
                                    .header("X-No-Cache", "true")
                                    
                                if (currentConfig.jinaApiKey.isNotBlank()) {
                                    requestBuilder.header("Authorization", "Bearer ${currentConfig.jinaApiKey.trim()}")
                                }
                                if (currentConfig.noImages) {
                                    requestBuilder.header("X-Retain-Images", "none")
                                }
                                requestBuilder.header("X-With-Links-Summary", "false")
                                requestBuilder.header("X-With-Images-Summary", "false")
                                requestBuilder.header("X-No-Cookies", if (currentConfig.noCookies) "true" else "false")
                                if (currentConfig.targetSelector.isNotBlank()) {
                                    requestBuilder.header("X-Target-Selector", currentConfig.targetSelector.trim())
                                }
                                if (currentConfig.removeSelector.isNotBlank()) {
                                    requestBuilder.header("X-Remove-Selector", currentConfig.removeSelector.trim())
                                }
                                val requestedFormat = when (currentConfig.returnFormat.trim().lowercase()) {
                                    "xml" -> "markdown"
                                    else -> currentConfig.returnFormat.trim().lowercase()
                                }
                                if (requestedFormat.isNotBlank() && requestedFormat != "markdown") {
                                    requestBuilder.header("X-Return-Format", requestedFormat)
                                }
                                
                                val request = requestBuilder.build()
                                val response = okHttpClient.newCall(request).execute()
                                
                                if (response.isSuccessful) {
                                    val resultText = response.body?.string() ?: ""
                                    if (resultText.isNotBlank()) {
                                        pageContent = resultText
                                        downloadSuccess = true
                                        logMsg("✅ Webpage download response success index [$visualIndex] (via Jina)")
                                    } else {
                                        lastErrorDetails = "Empty response from Jina Reader / پاسخ دریافتی از سرور Jina خالی است"
                                        logMsg("⚠ Jina empty response for index [$visualIndex]")
                                    }
                                } else {
                                    val errorExplanation = when (response.code) {
                                        400 -> "Bad Request (Jina parameter error) / پارامتر غیرمجاز جی‌نا"
                                        401, 403 -> "Unauthorized/Forbidden (API Key or Cloudflare blocked) / عدم دسترسی یا خطای کلید امنیتی"
                                        402 -> "Payment Required (No balance) / نیاز به شارژ حساب Jina"
                                        404 -> "Not Found (Webpage not found) / صفحه مورد نظر پیدا نشد"
                                        422 -> "Unprocessable Entity (Failed rendering) / خطا در رندر و پردازش صفحه"
                                        429 -> "Too Many Requests (Rate limit) / خطای محدودیت تعداد درخواست"
                                        451 -> "Unavailable For Legal Reasons (Content Blocked) / مسدود به دلایل حقوقی یا کپی‌رایت"
                                        500 -> "Internal Server Error (Jina crashed) / خطای داخلی سرور"
                                        502 -> "Bad Gateway (Remote unreachable) / خطا در برقراری ارتباط با مقصد"
                                        503 -> "Service Unavailable / سرور جی‌نا موقتاً در دسترس نیست"
                                        504 -> "Gateway Timeout (Timeout) / اتمام زمان ارتباط با سایت مرجع"
                                        else -> "HTTP status error ${response.code}"
                                    }
                                    val errBody = try { response.body?.string()?.take(120)?.trim() } catch (ignored: Exception) { null }
                                    val formattedRaw = if (!errBody.isNullOrBlank()) " ($errBody)" else ""
                                    lastErrorDetails = "HTTP ${response.code}: $errorExplanation$formattedRaw"
                                    
                                    logMsg("⚠ Jina returned HTTP status ${response.code}: $errorExplanation for [$visualIndex]")
                                    if (!errBody.isNullOrBlank()) {
                                        logMsg("📝 Raw Jina Service Message: $errBody")
                                    }
                                }
                            } catch (e: Exception) {
                                val errDetail = when (e) {
                                    is java.net.UnknownHostException -> "DNS/Network Failure (Check connection) / خطای آدرس شبکه‌ یا قطع اینترنت"
                                    is java.net.SocketTimeoutException -> "Request Timed Out (Jina slow) / اتمام زمان پاسخ‌دهی سرور"
                                    is javax.net.ssl.SSLHandshakeException -> "SSL Handshake Failed / خطای گواهی فایروال یا اتصال امن"
                                    is java.net.ConnectException -> "Connection Refused / رد ارتباط از سمت میزبان"
                                    else -> e.localizedMessage ?: e.message ?: "Unknown Connection Exception"
                                }
                                lastErrorDetails = errDetail
                                logMsg("⚠ Jina Reader offline or connection issue: $errDetail")
                            }
                        }
                        
                        if (downloadSuccess && pageContent.isNotBlank()) {
                            val processedPageContent = if (currentConfig.noImages) {
                                sanitizeAndRemoveImages(pageContent, currentConfig.returnFormat)
                            } else {
                                pageContent
                            }
                            combinedBuilder.append("\n\n\n")
                            combinedBuilder.append("=========================================\n")
                            combinedBuilder.append("# Source: $currentLink\n")
                            combinedBuilder.append("=========================================\n\n")
                            combinedBuilder.append(processedPageContent)
                            combinedBuilder.append("\n\n---")
                            
                            pagesList.add(ScrapedPage(index = visualIndex, url = currentLink, content = processedPageContent))
                            attemptSuccess = true
                        } else {
                            if (forceLocalFallback) {
                                logMsg("❌ Forced Local fallback also failed for index [$visualIndex]. Skipping page.")
                                skipLink = true
                            } else {
                                val elapsedMs = System.currentTimeMillis() - attemptStartTime
                                val oneMinuteMs = 60_000L
                                if (elapsedMs < oneMinuteMs) {
                                    val remainingMs = oneMinuteMs - elapsedMs
                                    val remainingSec = (remainingMs / 1000).toInt()
                                    val sleepSec = 5
                                    
                                    logMsg("⏳ Autoretry count-down: Reconnecting Jina in $sleepSec seconds... (Elapsed: ${(elapsedMs/1000)}s - Automatic retries stop in ${remainingSec}s)")
                                    logMsg("⏳ تلاش مجدد خودکار: تلاش برای برقراری مجدد ارتباط با جی‌نا در $sleepSec ثانیه دیگر... (زمان سپری‌شده: ${(elapsedMs/1000)} ثانیه - اتمام تلاش‌های خودکار در $remainingSec ثانیه)")
                                    
                                    val actualSleepMs = minOf(sleepSec * 1000L, remainingMs)
                                    delay(actualSleepMs)
                                } else {
                                    logMsg("❓ Pipeline suspended on [$visualIndex/$totalCount] after retrying for 1 minute.")
                                    logMsg("📝 Reason / علت خطا: $lastErrorDetails")
                                    logMsg("Waiting for user command...")
                                    
                                    val deferred = CompletableDeferred<FallbackChoice>()
                                    fallbackChoiceDeferred = deferred
                                    
                                    _uiState.value = CrawlUiState.WaitingForFallback(
                                        baseUrl = cleanUrl,
                                        currentLink = currentLink,
                                        foundLinks = finalLinks,
                                        currentIndex = index,
                                        total = totalCount,
                                        logs = logsList.toList(),
                                        errorMessage = lastErrorDetails
                                    )
                                    
                                    val choice = deferred.await()
                                    fallbackChoiceDeferred = null
                                    
                                    when (choice) {
                                        FallbackChoice.RETRY -> {
                                            logMsg("🔄 Command: [Retry Jina]. Re-evaluating Jina connection for index [$visualIndex]...")
                                        }
                                        FallbackChoice.USE_LOCAL_ALL -> {
                                            logMsg("🔄 Command: [Force Local Fallback]. Switching completely to Jsoup for remaining pages...")
                                            forceLocalFallback = true
                                        }
                                        FallbackChoice.CANCEL -> {
                                            logMsg("❌ Command: [Cancel]. Aborting crawl sequence dynamically...")
                                            _uiState.value = CrawlUiState.Idle
                                            return@launch
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                val domainName = try {
                    URL(cleanUrl).host.replace(".", "_")
                } catch (e: Exception) {
                    "website_docs"
                }
                
                val returnFormat = currentConfig.returnFormat.trim().lowercase()
                val extension = when (returnFormat) {
                    "html" -> "html"
                    "text" -> "txt"
                    "xml" -> "xml"
                    else -> "md"
                }
                val fileName = "${domainName}_docs.$extension"
                
                var finalFileContent = when (returnFormat) {
                    "html" -> {
                        val sb = StringBuilder()
                        sb.append("<!DOCTYPE html>\n")
                        sb.append("<html lang=\"en\">\n")
                        sb.append("<head>\n")
                        sb.append("    <meta charset=\"UTF-8\">\n")
                        sb.append("    <title>Compiled Docs - $domainName</title>\n")
                        sb.append("    <style>\n")
                        sb.append("        body { font-family: -apple-system, system-ui, BlinkMacSystemFont, \"Segoe UI\", Roboto, Helvetica, Arial, sans-serif; line-height: 1.6; max-width: 900px; margin: 40px auto; padding: 0 20px; color: #1a1a1a; background-color: #ffffff; }\n")
                        sb.append("        h1 { border-bottom: 2px solid #eaecef; padding-bottom: 0.3em; margin-top: 2em; color: #0366d6; }\n")
                        sb.append("        .metadata { background-color: #f6f8fa; border-radius: 8px; padding: 15px; margin: 20px 0; border-left: 4px solid #0366d6; font-size: 0.95em; }\n")
                        sb.append("        .page-banner { background-color: #eaecef; border-radius: 6px; padding: 10px 15px; margin: 30px 0 15px 0; font-weight: bold; font-family: monospace; font-size: 0.9em; border-left: 4px solid #28a745; }\n")
                        sb.append("        .page-content { padding: 10px; border: 1px solid #eaecef; border-radius: 6px; margin-bottom: 40px; white-space: pre-wrap; }\n")
                        sb.append("        hr { height: 1px; border: none; background-color: #e1e4e8; margin: 40px 0; }\n")
                        sb.append("    </style>\n")
                        sb.append("</head>\n")
                        val isRtl = isPersianText(combinedBuilder.toString())
                        if (isRtl) {
                            sb.append("<body dir=\"rtl\">\n")
                        } else {
                            sb.append("<body>\n")
                        }
                        sb.append("    <h1>Compiled Website Documents</h1>\n")
                        sb.append("    <div class=\"metadata\">\n")
                        sb.append("        <p><strong>Source Directory URL:</strong> <a href=\"$cleanUrl\" target=\"_blank\">$cleanUrl</a></p>\n")
                        sb.append("        <p><strong>Total Scraped Pages:</strong> ${pagesList.size}</p>\n")
                        sb.append("        <p><strong>Generation Date:</strong> ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}</p>\n")
                        sb.append("    </div>\n")
                        sb.append("    <hr/>\n")
                        
                        pagesList.forEach { page ->
                            sb.append("    <div class=\"page-banner\">[${page.index}/${pagesList.size}] Source: <a href=\"${page.url}\" target=\"_blank\">${page.url}</a></div>\n")
                            sb.append("    <div class=\"page-content\">\n")
                            sb.append(page.content)
                            sb.append("\n    </div>\n")
                            sb.append("    <hr/>\n")
                        }
                        
                        sb.append("</body>\n</html>")
                        sb.toString()
                    }
                    "text" -> {
                        val sb = StringBuilder()
                        sb.append("==============================================================\n")
                        sb.append("Compiled with URL to Markdown Android App\n")
                        sb.append("Source Directory Scraped: $cleanUrl\n")
                        sb.append("Compiled on: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}\n")
                        sb.append("==============================================================\n\n")
                        
                        pagesList.forEach { page ->
                            sb.append("\n\n")
                            sb.append("--------------------------------------------------------------\n")
                            sb.append("PAGE [${page.index}/${pagesList.size}]: ${page.url}\n")
                            sb.append("--------------------------------------------------------------\n\n")
                            sb.append(page.content)
                            sb.append("\n\n")
                        }
                        sb.toString()
                    }
                    "xml" -> {
                        val sb = StringBuilder()
                        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                        sb.append("<!-- Compiled with URL to Markdown Android App -->\n")
                        sb.append("<!-- Compiled on: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())} -->\n")
                        sb.append("<documents source_directory=\"$cleanUrl\" count=\"${pagesList.size}\">\n")
                        
                        pagesList.forEach { page ->
                            sb.append("    <document index=\"${page.index}\" url=\"${page.url}\">\n")
                            sb.append("        <content><![CDATA[\n")
                            sb.append(page.content)
                            sb.append("\n        ]]></content>\n")
                            sb.append("    </document>\n")
                        }
                        
                        sb.append("</documents>")
                        sb.toString()
                    }
                    else -> {
                        // Markdown
                        var mdText = combinedBuilder.toString()
                        val isRtl = isPersianText(mdText)
                        if (isRtl) {
                            mdText = "<div dir=\"rtl\">\n\n$mdText\n\n</div>"
                            logMsg("✍️ [RTL Display Active] Persian/Arabic content detected. Prepending <div dir=\"rtl\"> context container.")
                        }
                        mdText
                    }
                }
                
                val finalFileContentCleaned = if (currentConfig.noImages) {
                    sanitizeAndRemoveImages(finalFileContent, currentConfig.returnFormat)
                } else {
                    finalFileContent
                }
                
                val savedUri = saveMarkdownToDownloads(getApplication<Application>(), fileName, finalFileContentCleaned)
                
                if (savedUri != null) {
                    logMsg("✅ Compilation completed successfully.")
                    logMsg("📂 Output stored in phone's default Downloads folder: $fileName")
                } else {
                    logMsg("⚠ Compelling finished but files couldn't be auto-saved directly back to MediaStore storage.")
                }
                
                _uiState.value = CrawlUiState.Success(
                    baseUrl = cleanUrl,
                    totalConverted = totalCount,
                    savedUri = savedUri,
                    fileName = fileName,
                    fullContent = finalFileContentCleaned,
                    logs = logsList.toList()
                )
                
                val newExtraction = RecentExtractionItem(
                    fileName = fileName,
                    timeAgo = "Just now",
                    sizeString = "%.1f KB".format(finalFileContentCleaned.length / 1024.0),
                    fullContent = finalFileContentCleaned,
                    sourceUrl = cleanUrl,
                    savedUri = savedUri?.toString()
                )
                
                val updatedHistory = listOf(newExtraction) + _recentExtractions.value.filter { it.fileName != fileName }
                _recentExtractions.value = updatedHistory
                saveHistoryToPrefs(updatedHistory)
                
            } catch (e: Exception) {
                logMsg("❌ Execution error occurred: ${e.message}")
                _uiState.value = CrawlUiState.Error(e.message ?: "An unexpected error occurred during execution.")
            }
        }
    }

    fun cancelScraping() {
        fallbackChoiceDeferred?.complete(FallbackChoice.CANCEL)
        scraperJob?.cancel()
        _uiState.value = CrawlUiState.Idle
    }

    private fun getSubLinks(baseUrl: String, onLog: ((String) -> Unit)? = null): List<String> {
        val links = mutableSetOf<String>()
        try {
            val doc = Jsoup.connect(baseUrl)
                .userAgent("Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36")
                .timeout(30000)
                .ignoreContentType(true)
                .get()
            val aTags = doc.select("a[href]")
            for (a in aTags) {
                val fullUrl = a.absUrl("href")
                if (fullUrl.startsWith(baseUrl, ignoreCase = true)) {
                    val cleanUrl = fullUrl.split("#")[0].trimEnd('/')
                    if (cleanUrl.isNotEmpty() && cleanUrl.startsWith("http")) {
                        links.add(cleanUrl)
                    }
                }
            }
        } catch (e: Exception) {
            val errType = e.javaClass.name
            val errDetail = when {
                errType.contains("HttpStatusException") -> "HTTP Status Error (Received error status from target web server)"
                errType.contains("UnsupportedMimeTypeException") -> "Unsupported content type (Expected HTML index page)"
                e is java.net.UnknownHostException -> "DNS Lookup Failure (Check server URL or device internet connection)"
                e is java.net.SocketTimeoutException -> "Network Scraper Timeout (Target host is too slow to load index anchors)"
                else -> e.localizedMessage ?: e.message ?: "Unknown Connection Error"
            }
            onLog?.invoke("⚠ Index scan failed: $errDetail")
            e.printStackTrace()
        }
        return links.sorted()
    }

    private fun saveMarkdownToDownloads(context: Context, fileName: String, content: String): Uri? {
        val resolver = context.contentResolver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val mimeType = when {
                fileName.endsWith(".html") -> "text/html"
                fileName.endsWith(".txt") -> "text/plain"
                fileName.endsWith(".xml") -> "application/xml"
                else -> "text/markdown"
            }
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            return try {
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(content.toByteArray(Charsets.UTF_8))
                    }
                }
                uri
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            return try {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }
                val file = File(downloadsDir, fileName)
                file.writeText(content, Charsets.UTF_8)
                val authority = "${context.packageName}.provider"
                try {
                    FileProvider.getUriForFile(context, authority, file)
                } catch (e: Exception) {
                    Uri.fromFile(file)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun sanitizeAndRemoveImages(content: String, format: String): String {
        val isRtlText = isPersianText(content)
        val imageLabel = if (isRtlText) "تصویر" else "Image"
        
        var result = content
        
        // 1. Clean HTML <img ...> tags
        try {
            val htmlImageRegex = """(?i)<img\s+[^>]*>""".toRegex()
            result = result.replace(htmlImageRegex) { matchResult ->
                val imgTag = matchResult.value
                val altMatch = """(?i)\balt\s*=\s*["']([^"']*)["']""".toRegex().find(imgTag)
                val altText = altMatch?.groupValues?.get(1)?.trim() ?: ""
                if (altText.isNotEmpty()) {
                    "[$imageLabel: $altText]"
                } else {
                    "[$imageLabel]"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        // 2. Clean Markdown ![alt](url) tags with the original extremely robust regex
        try {
            val markdownImageRegex = """!\[(.*?)\]\((.*?)\)""".toRegex()
            result = result.replace(markdownImageRegex) { matchResult ->
                val altText = matchResult.groupValues[1].trim()
                if (altText.isNotEmpty()) {
                    "[$imageLabel: $altText]"
                } else {
                    "[$imageLabel]"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return result
    }

    private fun isPersianText(text: String): Boolean {
        val persianPattern = Regex("[\\u0600-\\u06FF]+")
        return persianPattern.containsMatchIn(text)
    }

    // --- JSON Parsers ---
    private fun configToJsonString(c: ScraperConfig): String {
        val obj = JSONObject()
        obj.put("jinaApiKey", c.jinaApiKey)
        obj.put("speedMode", c.speedMode.name)
        obj.put("customDelaySeconds", c.customDelaySeconds.toDouble())
        obj.put("maxPageLimit", c.maxPageLimit)
        obj.put("excludeKeywords", c.excludeKeywords)
        obj.put("noImages", c.noImages)
        obj.put("noCookies", c.noCookies)
        obj.put("appLanguage", c.appLanguage)
        obj.put("deepScrape", c.deepScrape)
        obj.put("targetSelector", c.targetSelector)
        obj.put("removeSelector", c.removeSelector)
        obj.put("returnFormat", c.returnFormat)
        return obj.toString()
    }

    private fun parseConfig(json: String): ScraperConfig {
        return try {
            val obj = JSONObject(json)
            ScraperConfig(
                jinaApiKey = obj.optString("jinaApiKey", ""),
                speedMode = try { SpeedMode.valueOf(obj.optString("speedMode", SpeedMode.AUTO_THROTTLE.name)) } catch (e: Exception) { SpeedMode.AUTO_THROTTLE },
                customDelaySeconds = obj.optDouble("customDelaySeconds", 1.0).toFloat(),
                maxPageLimit = obj.optInt("maxPageLimit", 30),
                excludeKeywords = obj.optString("excludeKeywords", "logout,login,signin,signout,signup,register,admin,dashboard,profile,account,cart,checkout,buy,pay,basket,billing,pricing,feedback,contact,support,help,faq,search,terms,privacy,cookie,policy,share,rss,feed,subscribe,newsletter"),
                noImages = obj.optBoolean("noImages", true),
                noCookies = obj.optBoolean("noCookies", true),
                appLanguage = obj.optString("appLanguage", "en"),
                deepScrape = obj.optBoolean("deepScrape", false),
                targetSelector = obj.optString("targetSelector", ""),
                removeSelector = obj.optString("removeSelector", ""),
                returnFormat = obj.optString("returnFormat", "markdown")
            )
        } catch (e: Exception) { ScraperConfig() }
    }

    private fun historyToJsonString(history: List<RecentExtractionItem>): String {
        val array = JSONArray()
        for (item in history) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("fileName", item.fileName)
            obj.put("timeAgo", item.timeAgo)
            obj.put("sizeString", item.sizeString)
            obj.put("fullContent", item.fullContent)
            obj.put("sourceUrl", item.sourceUrl)
            item.savedUri?.let { obj.put("savedUri", it) }
            array.put(obj)
        }
        return array.toString()
    }

    private fun parseHistory(json: String): List<RecentExtractionItem> {
        val list = mutableListOf<RecentExtractionItem>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    RecentExtractionItem(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        fileName = obj.optString("fileName", ""),
                        timeAgo = obj.optString("timeAgo", ""),
                        sizeString = obj.optString("sizeString", ""),
                        fullContent = obj.optString("fullContent", ""),
                        sourceUrl = obj.optString("sourceUrl", ""),
                        savedUri = if (obj.has("savedUri")) obj.getString("savedUri") else null
                    )
                )
            }
        } catch (e: Exception) { e.printStackTrace() }
        return list
    }
}

data class ScrapedPage(
    val index: Int,
    val url: String,
    val content: String
)
