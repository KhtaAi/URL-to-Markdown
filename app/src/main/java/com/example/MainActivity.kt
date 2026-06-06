// نام فایل: MainActivity.kt
// مسیر فایل: app/src/main/java/com/example/MainActivity.kt
package com.example
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

fun t(key: String, lang: String): String {
    return when (lang) {
        "fa" -> when (key) {
            "app_title" -> "تبدیل وب‌سایت به مارک‌داون"
            "app_desc" -> "تبدیل آسان و هوشمند لینک‌ها و درخت مستندات وب‌سایت به یک فایل یکپارچه Markdown مناسب برای ابزارهای هوش مصنوعی."
            "engine_profile" -> "پروفایل موتور: "
            "auto_throttle" -> "کاهش بار امنیتی (Auto-Throttle)"
            "custom_delay" -> "تأخیر ثابت (Custom Delay)"
            "burst_mode" -> "باند پرسرعت (Burst Mode)"
            "doc_entry" -> "آدرس شروع مستندات"
            "doc_url" -> "آدرس مستند یا وب‌سایت (URL)"
            "paste" -> "جایگذاری"
            "convert_url" -> "تبدیل آدرس"
            "cancel_req" -> "لغو درخواست"
            "status_live" -> "کنسول وضعیت و فایل خروجی"
            "awaiting_link" -> "در انتظار وارد کردن آدرس وب‌سایت"
            "ready_desc" -> "آماده برای شروع فرآیند تبدیل. موتور پردازشگر به صورت هوشمند آدرس وارد شده را اسکن کرده و فایل مارک‌داون منسجم تحویل می‌دهد."
            "recent_extractions" -> "آخرین استخراج‌های موفق"
            "view_all_history" -> "مشاهده آرشیو تاریخچه"
            "no_recent" -> "هیچ سابقه‌ای در حافظه محلی ذخیره نشده است."
            "analyzing_struct" -> "در حال آنالیز ساختار وب‌سایت و لینک‌ها..."
            "compiling_md" -> "در حال دانلود و کامپایل صفحات در یک فایل واحد..."
            "gen_sucess" -> "تبدیل با موفقیت انجام شد!"
            "pages_indexed" -> "صفحه با موفقیت استخراج و یکی شدند."
            "copy" -> "کپی متن"
            "share" -> "اشتراک‌گذاری"
            "open_file" -> "باز کردن فایل و مشاهده"
            "saved_downloads" -> "فایل به طور خودکار در پوشه دانلودها ذخیره شد"
            "process_complete_log" -> "گزارش نهایی فرآیند تبدیل"
            "conversion_failed" -> "خطا در فرآیند تبدیل"
            "reset" -> "دیدم خطا را (Reset)"
            "extraction_archive" -> "آرشیو گزارش‌های مارک‌داون"
            "manage_reports" -> "مدیریت و دسترسی سریع به کدهای مارک‌داون استخراج شده"
            "clear_history" -> "پاک کردن تاریخچه"
            "search_placeholder" -> "جستجوی فایل بر اساس نام یا آدرس..."
            "history_empty" -> "تاریخچه شما خالی است"
            "history_empty_desc" -> "پس از تبدیل موفقیت‌آمیز آدرس‌ها، موارد برای دسترسی سریع‌تر در این لیست طبقه‌بندی می‌شوند."
            "deep_scrape_title" -> "استخراج تمام صفحات فرعی عمیق (Deep Scan)"
            "deep_scrape_desc" -> "در صورت فعال بودن، تمام زیرلینک‌های فرعی مستند استخراج شده و در یک فایل واحد ادغام می‌شوند. در غیر این صورت، فقط همان تک صفحه تبدیل می‌شود."
            "language_selection" -> "زبان برنامه (App Language)"
            "language_selection_desc" -> "زبان مورد نظر خود را برای محیط کاربری انتخاب کنید:"
            "engine_settings" -> "تنظیمات موتور پردازشگر"
            "engine_settings_desc" -> "پیکربندی هوشمند و پویا برای دانلود و پردازش آدرس وب‌سایت‌ها"
            "jina_auth" -> "احراز هویت Jina API (اختیاری)"
            "jina_auth_desc" -> "با وارد کردن کلید دسترسی Jina، محدودیت پردازشگر (۲۰ درخواست در دقیقه) لغو می‌شود."
            "speed_pacing" -> "سرعت و تأخیر درخواست‌ها (Speed & Pacing)"
            "filters_limits" -> "فیلترها و سقف محدودیت‌ها"
            "max_page_limit" -> "حداکثر سقف مجاز صفحات:"
            "max_page_desc" -> "جلوگیری از زیاده‌روی استخراج. تعداد دقیق صفحات شناسایی‌شده (مثلاً ۲۰ صفحه) در کنسول فرآیند در شروع کار لاگ کتبی می‌شود. برای استخراج همه صفحات، این نشان‌گر اسلایدر سقف مجاز را افزایش دهید."
            "exclude_keywords" -> "حذف لینک‌های حاوی کلمات کلیدی خاص:"
            "exclude_desc" -> "هر کلمه را با ویرگول انگلیسی (,) جدا کنید. مانند: login, cart, register"
            "output_settings" -> "تنظیمات استخراج و حریم خصوصی"
            "ignore_images" -> "حذف تمامی عکس‌های وب‌سایت (No Images)"
            "ignore_images_desc" -> "جلوگیری از درج تگ‌های تصاویر در خروجی مارک‌داون جهت بهینه‌سازی شدید حجم فایل."
            "add_links_summary" -> "افزودن لیست خلاصه لینک‌ها در انتها"
            "add_links_summary_desc" -> "اضافه کردن لیست یا رفرنس تمام پیوندهای استخراج شده در انتهای فایل."
            "disable_cookies" -> "غیرفعال‌سازی کوکی‌های مرورگر (No Cookies)"
            "disable_cookies_desc" -> "عدم دریافت و ذخیره اطلاعات ردگیر کوکی صفحات حین واکشی برای افزایش حریم خصوصی."
            "jina_advanced" -> "تنظیمات پیشرفته Jina (CSS Selectors)"
            "jina_advanced_desc" -> "تنظیمات اختصاصی Jina برای استخراج دقیق‌تر و حرفه‌ای‌تر بخش‌های خاص صفحات وب."
            "target_selector_title" -> "انتخابگر بخش هدف (Target CSS Selector):"
            "target_selector_desc" -> "فقط عناصر منطبق با این انتخابگر CSS استخراج شوند (مانند article یا .main). خالی یعنی کل صفحه."
            "remove_selector_title" -> "حذف عناصر با بخش انتخابگر (Remove CSS Selector):"
            "remove_selector_desc" -> "عناصر منطبق با این انتخابگر از خروجی حذف شوند (مانند .comments,.ads,footer)."
            "with_images_summary" -> "افزودن لیست خلاصه تصاویر در انتها"
            "with_images_summary_desc" -> "افزودن تگ‌ها و آدرس تصاویر پیدا شده در صفحه به صورت خلاصه در انتهای فایل."
            "auto_throttle_desc" -> "پیش‌فرض هوشمند. محاسبه پویای تأخیر بر حسب تعداد صفحات برای تضمین سلامت سیستم بدون مسدود شدن آی‌پی."
            "custom_delay_desc" -> "تعیین تأخیر زمانی مشخص بین پردازش هر کدام از لینک‌های فرعی وب‌سایت."
            "burst_mode_desc" -> "عملکرد بدون وقفه و فوق‌العاده سریع. این حالت پیشنهاد می‌شود حتماً با API Key اختصاصی اجرا شود."
            "fixed_delay_label" -> "میزان تأخیر ثابت:"
            "seconds_unit" -> "ثانیه"
            "pages_unit" -> "صفحه"
            "jina_token_label" -> "کلید دسترسی Jina API"
            "tab_converter" -> "مبدل"
            "tab_history" -> "تاریخچه"
            "tab_settings" -> "تنظیمات"
            "where_is_saved" -> "محل ذخیره فایل‌ها در پوشه Downloads (دانلودهای عمومی) حافظه داخلی تلفن همراه شماست."
            "where_is_saved_title" -> "فایل‌ها در کجا ذخیره می‌شوند؟"
            "delete_item_confirm" -> "آیا از حذف این فایل آرشیو به کلی اطمینان دارید؟"
            "delete_success" -> "فایل آرشیو با موفقیت حذف گردید."
            "invalid_url_toast" -> "لطفاً ابتدا یک آدرس وب‌سایت معتبر وارد کنید!"
            "pasted_toast" -> "با موفقیت از حافظه جایگذاری شد!"
            "no_clipboard_toast" -> "حافظه موقت گوشی شما خالی است!"
            "copied_toast" -> "متن با موفقیت کپی شد!"
            else -> key
        }
        else -> when (key) {
            "app_title" -> "Web to Markdown"
            "app_desc" -> "Convert any website or documentation structure recursively into a clean unified single Markdown file optimized for LLMs."
            "engine_profile" -> "Engine profile: "
            "auto_throttle" -> "Auto-Throttle (Safe)"
            "custom_delay" -> "Custom Delay"
            "burst_mode" -> "Burst Mode"
            "doc_entry" -> "Documentation Entry Point"
            "doc_url" -> "Documentation URL"
            "paste" -> "Paste"
            "convert_url" -> "Convert URL"
            "cancel_req" -> "Cancel Request"
            "status_live" -> "Status & Live Output Console"
            "awaiting_link" -> "Awaiting Documentation Link"
            "ready_desc" -> "Ready to launch crawler queries. The engine scans the target, fetches contents via Jina endpoints, and bundles a highly optimized markdown archive."
            "recent_extractions" -> "RECENT EXTRACTIONS"
            "view_all_history" -> "View All History"
            "no_recent" -> "No recent records found inside your local extraction log."
            "analyzing_struct" -> "Analyzing Web Site Structure and links..."
            "compiling_md" -> "Compiling pages into a single unified Markdown file..."
            "gen_sucess" -> "Generation Successful!"
            "pages_indexed" -> "pages successfully indexed and compiled."
            "copy" -> "Copy"
            "share" -> "Share"
            "open_file" -> "Open File"
            "saved_downloads" -> "Saved automatically in Downloads folder"
            "process_complete_log" -> "Process Complete Output Log"
            "conversion_failed" -> "Conversion Failed"
            "reset" -> "Reset"
            "extraction_archive" -> "Extraction Archive"
            "manage_reports" -> "Manage your generated Markdown reports locally"
            "clear_history" -> "Clear History"
            "search_placeholder" -> "Search files by sitemap names / urls..."
            "history_empty" -> "Your history index is empty!"
            "history_empty_desc" -> "Once you crawl and download documentation, those reports will automatically be logged here for quick retrieval."
            "deep_scrape_title" -> "Deep Scrape Sub-links"
            "deep_scrape_desc" -> "If enabled, crawler will search and extract all inner sub-links and join them in a single file. If disabled, it only converts the entered single URL."
            "language_selection" -> "App Language"
            "language_selection_desc" -> "Select the language for the user interface:"
            "engine_settings" -> "Engine Settings"
            "engine_settings_desc" -> "Smart and dynamic configuration for crawling and processing website URLs"
            "jina_auth" -> "Jina API Authentication (Optional)"
            "jina_auth_desc" -> "Adding a Jina key lifts rate limits (20 requests/min limit) for crawling large structures."
            "speed_pacing" -> "Speed & Pacing Settings"
            "filters_limits" -> "Filters & Limits"
            "max_page_limit" -> "Max crawl depth page limit:"
            "max_page_desc" -> "Halts crawl if links exceed this limit. The scanner logs the actual found sub-page count in the console at startup. Increase this slider to scrape larger structures."
            "exclude_keywords" -> "Ignore URLs containing these keywords:"
            "exclude_desc" -> "Separate each word with a comma (,). E.g.: login, logout, cart, admin"
            "output_settings" -> "Extraction & Privacy Settings"
            "ignore_images" -> "Ignore all images (No Images)"
            "ignore_images_desc" -> "Prevents loading/injecting images into Markdown output to save file size."
            "add_links_summary" -> "Add links summary at the end"
            "add_links_summary_desc" -> "Appends a clean numbered list of reference URLs at the end of the markdown."
            "disable_cookies" -> "Disable browser tracking cookies (No Cookies)"
            "disable_cookies_desc" -> "Enhances privacy by blocking page tracking cookies during the extraction process."
            "jina_advanced" -> "Advanced Jina Query Options"
            "jina_advanced_desc" -> "Target or remove specific webpage elements with CSS selectors for a super clean output."
            "target_selector_title" -> "Target CSS Selector:"
            "target_selector_desc" -> "Only extract HTML matching this selector (e.g. article, .main-content). Leave empty for whole page."
            "remove_selector_title" -> "Remove CSS Selector:"
            "remove_selector_desc" -> "Exclude HTML matching this selector (e.g. .comments, nav, footer, .ads)."
            "with_images_summary" -> "Include image summary at the end"
            "with_images_summary_desc" -> "Appends a neat summary panel of all discovered page images with alt labels at the end."
            "return_format_title" -> "Return Format:"
            "return_format_desc" -> "The output format requested from Jina Reader API."
            "auto_throttle_desc" -> "Smart default. Calculates delays dynamically based on page count to ensure smooth crawling without getting IP-blocked."
            "custom_delay_desc" -> "Applies a fixed delay in seconds between crawling consecutive sub-pages of the website."
            "burst_mode_desc" -> "Ultra-fast crawling with zero delays. Highly recommended to use your custom Jina API key to prevent rate limits."
            "fixed_delay_label" -> "Fixed Delay Duration:"
            "seconds_unit" -> "seconds"
            "pages_unit" -> "pages"
            "jina_token_label" -> "Jina Authorization API Key"
            "tab_converter" -> "Converter"
            "tab_history" -> "History"
            "tab_settings" -> "Settings"
            "where_is_saved" -> "Files are saved in your phone's primary internal public 'Downloads' folder."
            "where_is_saved_title" -> "Where are these database files saved?"
            "delete_item_confirm" -> "Are you sure you want to permanently delete this archive item?"
            "delete_success" -> "Archive item removed successfully."
            "invalid_url_toast" -> "Please enter a valid website URL address first!"
            "pasted_toast" -> "Pasted from clipboard!"
            "no_clipboard_toast" -> "Clipboard is empty!"
            "copied_toast" -> "Copied to clipboard!"
            else -> key
        }
    }
}

fun openMarkdownFile(context: Context, uriString: String?, content: String, fileName: String) {
    if (uriString == null) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, fileName)
            putExtra(Intent.EXTRA_TEXT, content)
        }
        context.startActivity(Intent.createChooser(intent, "Share Markdown Content"))
        return
    }
    try {
        val uri = Uri.parse(uriString)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "text/markdown")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, "Open Markdown Document"))
    } catch (e: Exception) {
        try {
            val uri = Uri.parse(uriString)
            val intentFallback = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "text/plain")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(intentFallback, "Open file with..."))
        } catch (ex: Exception) {
            Toast.makeText(context, "No app found to open Markdown/text files. Sharing text fallback instead.", Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, fileName)
                putExtra(Intent.EXTRA_TEXT, content)
            }
            context.startActivity(Intent.createChooser(intent, "Share File Content"))
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ScraperViewModel = viewModel()
            val config by viewModel.config.collectAsState()
            var activeTab by remember { mutableStateOf("converter") }
            val layoutDirection = if (config.appLanguage == "fa") LayoutDirection.Rtl else LayoutDirection.Ltr
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            if (activeTab != "settings") {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF211F26))
                                        .navigationBarsPadding()
                                ) {
                                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(80.dp)
                                            .padding(horizontal = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Converter Tab
                                        val isConverterActive = activeTab == "converter"
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { activeTab = "converter" }
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .width(56.dp)
                                                    .height(32.dp)
                                                    .background(
                                                        color = if (isConverterActive) Color(0xFF381E72) else Color.Transparent,
                                                        shape = RoundedCornerShape(16.dp)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Transform,
                                                    contentDescription = "Converter",
                                                    tint = if (isConverterActive) Color(0xFFD0BCFF) else Color(0xFFCAC4D0).copy(alpha = 0.7f),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = t("tab_converter", config.appLanguage),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontSize = 11.sp,
                                                    fontWeight = if (isConverterActive) FontWeight.Bold else FontWeight.Medium
                                                ),
                                                color = if (isConverterActive) Color(0xFFD0BCFF) else Color(0xFFCAC4D0).copy(alpha = 0.7f)
                                            )
                                        }
                                        // History Tab
                                        val isHistoryActive = activeTab == "history"
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { activeTab = "history" }
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .width(56.dp)
                                                    .height(32.dp)
                                                    .background(
                                                        color = if (isHistoryActive) Color(0xFF381E72) else Color.Transparent,
                                                        shape = RoundedCornerShape(16.dp)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.History,
                                                    contentDescription = "History",
                                                    tint = if (isHistoryActive) Color(0xFFD0BCFF) else Color(0xFFCAC4D0).copy(alpha = 0.7f),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = t("tab_history", config.appLanguage),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontSize = 11.sp,
                                                    fontWeight = if (isHistoryActive) FontWeight.Bold else FontWeight.Medium
                                                ),
                                                color = if (isHistoryActive) Color(0xFFD0BCFF) else Color(0xFFCAC4D0).copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (activeTab) {
                                "converter" -> {
                                    UrlToMarkdownScreen(
                                        viewModel = viewModel,
                                        onNavigateToHistory = { activeTab = "history" },
                                        onNavigateToScripts = { activeTab = "settings" }
                                    )
                                }
                                "history" -> {
                                    HistoryScreen(
                                        viewModel = viewModel
                                    )
                                }
                                "settings" -> {
                                    SettingsAndScriptsScreen(
                                        viewModel = viewModel,
                                        onNavigateBack = { activeTab = "converter" }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrlToMarkdownScreen(
    viewModel: ScraperViewModel,
    onNavigateToHistory: () -> Unit,
    onNavigateToScripts: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val config by viewModel.config.collectAsState()
    var urlInput by remember { mutableStateOf("") }
    var isPreviewDialogOpen by remember { mutableStateOf(false) }
    var isFullScreenLogsOpen by remember { mutableStateOf(false) }
    var selectedRecentFileName by remember { mutableStateOf("") }
    var selectedRecentContent by remember { mutableStateOf("") }
    var selectedRecentUriString by remember { mutableStateOf<String?>(null) }
    
    val clipboardManager = remember {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
    
    fun copyToClipboard(text: String) {
        val clip = ClipData.newPlainText("Markdown Docs", text)
        clipboardManager.setPrimaryClip(clip)
        Toast.makeText(context, "Copied content to clipboard!", Toast.LENGTH_SHORT).show()
    }
    
    fun shareMarkdownContent(fileName: String, content: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, fileName)
            putExtra(Intent.EXTRA_TEXT, content)
        }
        context.startActivity(Intent.createChooser(intent, "Share Markdown"))
    }

    val isRunning = uiState is CrawlUiState.Scanning || uiState is CrawlUiState.Converting || uiState is CrawlUiState.WaitingForFallback

    val logsToZoom = when (val state = uiState) {
        is CrawlUiState.Scanning -> state.logs
        is CrawlUiState.Converting -> state.logs
        is CrawlUiState.WaitingForFallback -> state.logs
        is CrawlUiState.Success -> state.logs
        else -> emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // App header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { 
                    Toast.makeText(context, "You are on the main converter view", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = t("app_title", config.appLanguage),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Normal,
                        letterSpacing = (-0.5).sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = onNavigateToScripts) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Scripts Customization Center",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        // Description Section - Hide during scanning to save maximum vertical space
        if (!isRunning) {
            Column(modifier = Modifier.padding(bottom = 12.dp, start = 4.dp, end = 4.dp)) {
                Text(
                    text = t("app_desc", config.appLanguage),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                )
            }
        }
        
        // Input Area: Sleek and dynamic depending on scraper state
        if (isRunning) {
            // کارت جمع‌وجور فرآیند فعال برای افزایش فضای لاگ‌ها در صفحه‌های کوچک
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (config.appLanguage == "fa") "فرآیند استخراج فعال..." else "Active crawling job...",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = urlInput,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { viewModel.cancelScraping() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = "Cancel", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = t("cancel_req", config.appLanguage),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            // همان کارت ورودی قبلی در حالت عادی
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text(
                        text = t("doc_entry", config.appLanguage),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = urlInput,
                        onValueChange = { urlInput = it },
                        label = { Text(t("doc_url", config.appLanguage)) },
                        placeholder = { Text("https://docs.python.org/3/") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = "Link Icon"
                            )
                        },
                        trailingIcon = {
                            if (urlInput.isNotEmpty()) {
                                IconButton(onClick = { urlInput = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear Input"
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("url_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Deep Scrape toggle on Converter Screen
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = t("deep_scrape_title", config.appLanguage),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = config.deepScrape,
                            onCheckedChange = { isChecked ->
                                viewModel.updateConfig(config.copy(deepScrape = isChecked))
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Paste Button
                        OutlinedButton(
                            onClick = {
                                val primaryClip = clipboardManager.primaryClip
                                if (primaryClip != null && primaryClip.itemCount > 0) {
                                    val text = primaryClip.getItemAt(0).text
                                    if (!text.isNullOrEmpty()) {
                                        urlInput = text.toString().trim()
                                        Toast.makeText(context, t("pasted_toast", config.appLanguage), Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, t("no_clipboard_toast", config.appLanguage), Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, t("no_clipboard_toast", config.appLanguage), Toast.LENGTH_SHORT).show()
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("paste_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentPaste,
                                contentDescription = "Paste",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(t("paste", config.appLanguage))
                        }
                        
                        // Main Action Button (Convert)
                        Button(
                            onClick = {
                                if (urlInput.isEmpty()) {
                                    Toast.makeText(context, t("invalid_url_toast", config.appLanguage), Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.startScraping(context, urlInput)
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(2f)
                                .height(48.dp)
                                .testTag("convert_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Convert"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(t("convert_url", config.appLanguage))
                        }
                    }
                }
            }
        }
        
        // Live status / progress region Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = t("status_live", config.appLanguage),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            
            // نمایش زوم در صورتی که کاری معتبر در حال اجرا یا اجرا شده باشد
            if (logsToZoom.isNotEmpty()) {
                IconButton(
                    onClick = { isFullScreenLogsOpen = true },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Fullscreen,
                        contentDescription = "Zoom Logs",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
        
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (val state = uiState) {
                is CrawlUiState.Idle -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(
                                1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info",
                                        tint = Color(0xFFD0BCFF),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = t("awaiting_link", config.appLanguage),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = t("ready_desc", config.appLanguage),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
                is CrawlUiState.Scanning -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = t("analyzing_struct", config.appLanguage),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = state.message,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Text(
                            text = t("process_complete_log", config.appLanguage),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TerminalLogsConsole(logs = state.logs, modifier = Modifier.weight(1f))
                    }
                }
                is CrawlUiState.Converting -> {
                    val progressFloat = if (state.total > 0) {
                        (state.currentIndex + 1).toFloat() / state.total.toFloat()
                    } else {
                        0f
                    }
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = t("compiling_md", config.appLanguage),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1.5f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (config.appLanguage == "fa") "صفحه ${state.currentIndex + 1} از ${state.total}" else "Page ${state.currentIndex + 1} of ${state.total}",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "${(progressFloat * 100).toInt()}%",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        LinearProgressIndicator(
                            progress = progressFloat,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .testTag("conversion_progress_bar"),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Text(
                            text = if (config.appLanguage == "fa") "تعداد کل صفحات شناسایی‌شده: ${state.total} صفحه" else "Total discovered pages: ${state.total}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        Text(
                            text = "Processing: ${state.currentLink}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(8.dp))
                        TerminalLogsConsole(logs = state.logs, modifier = Modifier.weight(1f))
                    }
                }
                is CrawlUiState.WaitingForFallback -> {
                    val progressFloat = if (state.total > 0) {
                        (state.currentIndex + 1).toFloat() / state.total.toFloat()
                    } else {
                        0f
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (config.appLanguage == "fa") "اسکراپ موقتاً متوقف شد (انتظار برای تصمیم)" else "Scraping Paused (Waiting for choice)",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.weight(1.5f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (config.appLanguage == "fa") "صفحه ${state.currentIndex + 1} از ${state.total}" else "Page ${state.currentIndex + 1} of ${state.total}",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = "${(progressFloat * 100).toInt()}%",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        LinearProgressIndicator(
                            progress = progressFloat,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = MaterialTheme.colorScheme.error,
                            trackColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Text(
                            text = if (config.appLanguage == "fa") "تعداد کل صفحات شناسایی‌شده: ${state.total} صفحه" else "Total discovered pages: ${state.total}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        Text(
                            text = if (config.appLanguage == "fa") "متوقف روی: ${state.currentLink}" else "Paused on: ${state.currentLink}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(8.dp))
                        TerminalLogsConsole(logs = state.logs, modifier = Modifier.weight(1f))
                        
                        // Inline non-blocking Fallback panel so that logs are completely visible and scrollable!
                        Spacer(modifier = Modifier.height(8.dp))
                        FallbackDecisionPanel(
                            currentLink = state.currentLink,
                            appLanguage = config.appLanguage,
                            onRetry = { viewModel.respondToFallbackChoice(FallbackChoice.RETRY) },
                            onUseLocalAll = { viewModel.respondToFallbackChoice(FallbackChoice.USE_LOCAL_ALL) },
                            onCancel = { viewModel.respondToFallbackChoice(FallbackChoice.CANCEL) }
                        )
                    }
                }
                is CrawlUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 1.dp,
                                color = Color(0xFF2E7D32),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Success check",
                                    tint = Color(0xFF2E7D32)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = t("gen_sucess", config.appLanguage),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF2E7D32)
                                )
                                Text(
                                    text = "${state.totalConverted} ${t("pages_indexed", config.appLanguage)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = "File",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = state.fileName,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = t("saved_downloads", config.appLanguage),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Button(
                                onClick = { isPreviewDialogOpen = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("preview_button"),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = "View",
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (config.appLanguage == "fa") "پیش‌نمایش" else "Preview",
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Button(
                                onClick = { copyToClipboard(state.fullContent) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("copy_button"),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = t("copy", config.appLanguage),
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Button(
                                onClick = { shareMarkdownContent(state.fileName, state.fullContent) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("share_button"),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = t("share", config.appLanguage),
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = t("process_complete_log", config.appLanguage),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        TerminalLogsConsole(logs = state.logs, modifier = Modifier.weight(1f))
                    }
                }
                is CrawlUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.error,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = "Error icon",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = t("conversion_failed", config.appLanguage),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.message,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.cancelScraping() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(t("reset", config.appLanguage))
                        }
                    }
                }
            }
        }
    }
    
    // Zoom log terminal in full screen Dialog
    if (isFullScreenLogsOpen) {
        Dialog(onDismissRequest = { isFullScreenLogsOpen = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.85f),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.background,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("💻", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (config.appLanguage == "fa") "نمایش زنده لاگ‌ها" else "Terminal Output logs",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        IconButton(onClick = { isFullScreenLogsOpen = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    TerminalLogsConsole(logs = logsToZoom, modifier = Modifier.weight(1f))
                }
            }
        }
    }
    
    // Preview / Document Action Dialog
    ArchiveItemActionDialog(
        isOpen = isPreviewDialogOpen,
        title = if (uiState is CrawlUiState.Success) (uiState as CrawlUiState.Success).fileName else selectedRecentFileName,
        content = if (uiState is CrawlUiState.Success) (uiState as CrawlUiState.Success).fullContent else selectedRecentContent,
        uriString = if (uiState is CrawlUiState.Success) (uiState as CrawlUiState.Success).savedUri?.toString() else selectedRecentUriString,
        appLanguage = config.appLanguage,
        onDismiss = { isPreviewDialogOpen = false },
        onCopy = {
            val contentToCopy = if (uiState is CrawlUiState.Success) (uiState as CrawlUiState.Success).fullContent else selectedRecentContent
            copyToClipboard(contentToCopy)
        },
        onShare = {
            val titleToShare = if (uiState is CrawlUiState.Success) (uiState as CrawlUiState.Success).fileName else selectedRecentFileName
            val contentToShare = if (uiState is CrawlUiState.Success) (uiState as CrawlUiState.Success).fullContent else selectedRecentContent
            shareMarkdownContent(titleToShare, contentToShare)
        }
    )
}

@Composable
fun HistoryScreen(
    viewModel: ScraperViewModel
) {
    val config by viewModel.config.collectAsState()
    val context = LocalContext.current
    val recentExtractions by viewModel.recentExtractions.collectAsState()
    var searchInput by remember { mutableStateOf("") }
    var isPreviewDialogOpen by remember { mutableStateOf(false) }
    var selectedFileName by remember { mutableStateOf("") }
    var selectedContent by remember { mutableStateOf("") }
    var selectedUriString by remember { mutableStateOf<String?>(null) }
    
    val clipboardManager = remember {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
    
    val filteredList = remember(recentExtractions, searchInput) {
        if (searchInput.isEmpty()) {
            recentExtractions
        } else {
            recentExtractions.filter {
                it.fileName.contains(searchInput, ignoreCase = true) ||
                it.sourceUrl.contains(searchInput, ignoreCase = true)
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // History Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = t("extraction_archive", config.appLanguage),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = t("manage_reports", config.appLanguage),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
            if (recentExtractions.isNotEmpty()) {
                IconButton(onClick = {
                    viewModel.clearHistory()
                    Toast.makeText(context, if (config.appLanguage == "fa") "تاریخچه با موفقیت پاک شد" else "Clear local extraction log successfully", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Clear History",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        
        // Where are files saved - Localized Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ℹ️", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = t("where_is_saved_title", config.appLanguage),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = t("where_is_saved", config.appLanguage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                }
            }
        }
        
        // Search Bar
        OutlinedTextField(
            value = searchInput,
            onValueChange = { searchInput = it },
            placeholder = { Text(t("search_placeholder", config.appLanguage)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "SearchIcon") },
            trailingIcon = {
                if (searchInput.isNotEmpty()) {
                    IconButton(onClick = { searchInput = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp)
        )
        
        // Historical Records List
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (filteredList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Empty",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (searchInput.isEmpty()) t("history_empty", config.appLanguage) else "No search results matched.",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (searchInput.isEmpty()) t("history_empty_desc", config.appLanguage) else "Try adjusting search query.",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedFileName = item.fileName
                                    selectedContent = item.fullContent
                                    selectedUriString = item.savedUri
                                    isPreviewDialogOpen = true
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color(0xFF381E72), shape = RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("📄", fontSize = 16.sp)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.fileName,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "Source: ${item.sourceUrl}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "View",
                                        tint = Color.White.copy(alpha = 0.3f)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${item.timeAgo} • ${item.sizeString}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        IconButton(
                                            onClick = {
                                                val clip = ClipData.newPlainText("Markdown Docs", item.fullContent)
                                                clipboardManager.setPrimaryClip(clip)
                                                Toast.makeText(context, if (config.appLanguage == "fa") "کپی شد!" else "Copied content!", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = "Copy",
                                                tint = Color(0xFFD0BCFF),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_SEND).apply {
                                                    type = "text/plain"
                                                    putExtra(Intent.EXTRA_SUBJECT, item.fileName)
                                                    putExtra(Intent.EXTRA_TEXT, item.fullContent)
                                                }
                                                context.startActivity(Intent.createChooser(intent, "Share Report"))
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Share,
                                                contentDescription = "Share",
                                                tint = Color(0xFFD0BCFF),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                viewModel.deleteExtraction(context, item)
                                                Toast.makeText(context, t("delete_success", config.appLanguage), Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Document Action Dialog for history entries
    ArchiveItemActionDialog(
        isOpen = isPreviewDialogOpen,
        title = selectedFileName,
        content = selectedContent,
        uriString = selectedUriString,
        appLanguage = config.appLanguage,
        onDismiss = { isPreviewDialogOpen = false },
        onCopy = {
            val clip = ClipData.newPlainText("Markdown Docs", selectedContent)
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(context, if (config.appLanguage == "fa") "محتوا کپی شد!" else "Copied content to clipboard!", Toast.LENGTH_SHORT).show()
        },
        onShare = {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, selectedFileName)
                putExtra(Intent.EXTRA_TEXT, selectedContent)
            }
            context.startActivity(Intent.createChooser(intent, "Share Report"))
        }
    )
}

@Composable
fun SettingsAndScriptsScreen(
    viewModel: ScraperViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val config by viewModel.config.collectAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Core Header with Back button
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = t("engine_settings", config.appLanguage),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = t("engine_settings_desc", config.appLanguage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        }
        // Section 0: Language Selection
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🌐", fontSize = 20.sp)
                        Text(
                            text = t("language_selection", config.appLanguage),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = t("language_selection_desc", config.appLanguage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Persian Option
                        Button(
                            onClick = {
                                viewModel.updateConfig(config.copy(appLanguage = "fa"))
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (config.appLanguage == "fa") MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.05f),
                                contentColor = if (config.appLanguage == "fa") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (config.appLanguage == "fa") Color.Transparent else Color.White.copy(alpha = 0.1f)
                            )
                        ) {
                            Text("فارسی (FA)", fontWeight = FontWeight.Bold)
                        }
                        // English Option
                        Button(
                            onClick = {
                                viewModel.updateConfig(config.copy(appLanguage = "en"))
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (config.appLanguage == "en") MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.05f),
                                contentColor = if (config.appLanguage == "en") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (config.appLanguage == "en") Color.Transparent else Color.White.copy(alpha = 0.1f)
                            )
                        ) {
                            Text("English (EN)", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        // Section 1: APIs & Credentials (Jina Key)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🔑", fontSize = 20.sp)
                        Text(
                            text = t("jina_auth", config.appLanguage),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = t("jina_auth_desc", config.appLanguage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    OutlinedTextField(
                        value = config.jinaApiKey,
                        onValueChange = { newVal ->
                            viewModel.updateConfig(config.copy(jinaApiKey = newVal))
                        },
                        label = { Text(t("jina_token_label", config.appLanguage)) },
                        placeholder = { Text("jina_1234abcd...") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
        // Section 2: Pacing & Speed throttling
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("⏱️", fontSize = 20.sp)
                        Text(
                            text = t("speed_pacing", config.appLanguage),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    // Speed Modes interactives
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SpeedModeCard(
                            modeName = t("auto_throttle", config.appLanguage),
                            description = t("auto_throttle_desc", config.appLanguage),
                            isSelected = config.speedMode == SpeedMode.AUTO_THROTTLE,
                            onClick = {
                                viewModel.updateConfig(config.copy(speedMode = SpeedMode.AUTO_THROTTLE))
                            }
                        )
                        SpeedModeCard(
                            modeName = t("custom_delay", config.appLanguage),
                            description = t("custom_delay_desc", config.appLanguage),
                            isSelected = config.speedMode == SpeedMode.CUSTOM_DELAY,
                            onClick = {
                                viewModel.updateConfig(config.copy(speedMode = SpeedMode.CUSTOM_DELAY))
                            }
                        )
                        SpeedModeCard(
                            modeName = t("burst_mode", config.appLanguage),
                            description = t("burst_mode_desc", config.appLanguage),
                            isSelected = config.speedMode == SpeedMode.FAST_NO_DELAY,
                            onClick = {
                                viewModel.updateConfig(config.copy(speedMode = SpeedMode.FAST_NO_DELAY))
                            }
                        )
                    }
                    // Slider for Custom Delay (if CONSTANT selected)
                    if (config.speedMode == SpeedMode.CUSTOM_DELAY) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = t("fixed_delay_label", config.appLanguage),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "${String.format("%.1f", config.customDelaySeconds)} ${t("seconds_unit", config.appLanguage)}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Slider(
                            value = config.customDelaySeconds,
                            onValueChange = { newVal ->
                                viewModel.updateConfig(config.copy(customDelaySeconds = newVal))
                            },
                            valueRange = 0.1f..5.0f,
                            steps = 49,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        }
        // Section 3: Limitations & Ignored items
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🛡️", fontSize = 20.sp)
                        Text(
                            text = t("filters_limits", config.appLanguage),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    // Max page Limit
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = t("max_page_limit", config.appLanguage),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${config.maxPageLimit} ${t("pages_unit", config.appLanguage)}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = t("max_page_desc", config.appLanguage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Minus Button
                        IconButton(
                            onClick = {
                                val currentLimit = config.maxPageLimit
                                if (currentLimit > 1) {
                                    val target = currentLimit - 1
                                    viewModel.updateConfig(config.copy(maxPageLimit = target))
                                }
                            },
                            modifier = Modifier
                                .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                                .size(40.dp)
                        ) {
                            Text(
                                text = "—",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        // Text Field Input (numeric)
                        var pageInputText by remember(config.maxPageLimit) { mutableStateOf(config.maxPageLimit.toString()) }
                        
                        OutlinedTextField(
                            value = pageInputText,
                            onValueChange = { newVal ->
                                val filtered = newVal.filter { it.isDigit() }
                                pageInputText = filtered
                                val parsed = filtered.toIntOrNull()
                                if (parsed != null && parsed >= 1 && parsed <= 1000) {
                                    viewModel.updateConfig(config.copy(maxPageLimit = parsed))
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            ),
                            placeholder = {
                                Text(
                                    text = "30",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )

                        // Plus Button
                        IconButton(
                            onClick = {
                                val currentLimit = config.maxPageLimit
                                if (currentLimit < 1000) {
                                    val target = currentLimit + 1
                                    viewModel.updateConfig(config.copy(maxPageLimit = target))
                                }
                            },
                            modifier = Modifier
                                .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase page limit",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Preset Chips
                    Text(
                        text = if (config.appLanguage == "fa") "انتخاب سریع سقف صفحات:" else "Quick Select limit:",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val presets = listOf(1, 5, 10, 20, 50, 100)
                        presets.forEach { preset ->
                            val isSelected = config.maxPageLimit == preset
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                        else Color.White.copy(alpha = 0.05f)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        viewModel.updateConfig(config.copy(maxPageLimit = preset))
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$preset",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                    Spacer(modifier = Modifier.height(14.dp))
                    // Excluded keywords
                    Text(
                        text = t("exclude_keywords", config.appLanguage),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = t("exclude_desc", config.appLanguage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = config.excludeKeywords,
                        onValueChange = { newVal ->
                            viewModel.updateConfig(config.copy(excludeKeywords = newVal))
                        },
                        singleLine = false,
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
        // Section 4: Headers and toggles
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🔒", fontSize = 20.sp)
                        Text(
                            text = t("output_settings", config.appLanguage),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    ToggleRowItem(
                        title = t("ignore_images", config.appLanguage),
                        description = t("ignore_images_desc", config.appLanguage),
                        isChecked = config.noImages,
                        onCheckedChange = { newVal ->
                            viewModel.updateConfig(config.copy(noImages = newVal))
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                    Spacer(modifier = Modifier.height(12.dp))
                    ToggleRowItem(
                        title = t("add_links_summary", config.appLanguage),
                        description = t("add_links_summary_desc", config.appLanguage),
                        isChecked = config.withLinksSummary,
                        onCheckedChange = { newVal ->
                            viewModel.updateConfig(config.copy(withLinksSummary = newVal))
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                    Spacer(modifier = Modifier.height(12.dp))
                    ToggleRowItem(
                        title = t("disable_cookies", config.appLanguage),
                        description = t("disable_cookies_desc", config.appLanguage),
                        isChecked = config.noCookies,
                        onCheckedChange = { newVal ->
                            viewModel.updateConfig(config.copy(noCookies = newVal))
                        }
                    )
                }
            }
        }
        // Section 5: Advanced Jina settings
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🛠️", fontSize = 20.sp)
                        Text(
                            text = t("jina_advanced", config.appLanguage),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = t("jina_advanced_desc", config.appLanguage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    // Target CSS Selector
                    Text(
                        text = t("target_selector_title", config.appLanguage),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = t("target_selector_desc", config.appLanguage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = config.targetSelector,
                        onValueChange = { newVal ->
                            viewModel.updateConfig(config.copy(targetSelector = newVal))
                        },
                        placeholder = { Text("article, .main-content") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.15f)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Remove CSS Selector
                    Text(
                        text = t("remove_selector_title", config.appLanguage),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = t("remove_selector_desc", config.appLanguage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = config.removeSelector,
                        onValueChange = { newVal ->
                            viewModel.updateConfig(config.copy(removeSelector = newVal))
                        },
                        placeholder = { Text(".ads, .comments, footer") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.15f)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                    Spacer(modifier = Modifier.height(16.dp))
                    // With Images Summary
                    ToggleRowItem(
                        title = t("with_images_summary", config.appLanguage),
                        description = t("with_images_summary_desc", config.appLanguage),
                        isChecked = config.withImagesSummary,
                        onCheckedChange = { newVal ->
                            viewModel.updateConfig(config.copy(withImagesSummary = newVal))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                    Spacer(modifier = Modifier.height(16.dp))
                    // Return Format Selection
                    Text(
                        text = t("return_format_title", config.appLanguage),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = t("return_format_desc", config.appLanguage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val formats = listOf("markdown", "html", "text", "xml")
                        formats.forEach { format ->
                            Button(
                                onClick = {
                                    viewModel.updateConfig(config.copy(returnFormat = format))
                                },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (config.returnFormat == format) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.05f),
                                    contentColor = if (config.returnFormat == format) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                                ),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (config.returnFormat == format) Color.Transparent else Color.White.copy(alpha = 0.1f)
                                )
                            ) {
                                Text(
                                    text = format.uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpeedModeCard(
    modeName: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF381E72).copy(alpha = 0.3f) else Color.White.copy(alpha = 0.03f)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) Color(0xFFD0BCFF).copy(alpha = 0.6f) else Color.White.copy(alpha = 0.05f)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = modeName,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) Color(0xFFD0BCFF) else MaterialTheme.colorScheme.onBackground
                )
                RadioButton(
                    selected = isSelected,
                    onClick = onClick,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFFD0BCFF)
                    )
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun ToggleRowItem(
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                lineHeight = 15.sp
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF381E72),
                checkedTrackColor = Color(0xFFD0BCFF)
            )
        )
    }
}

@Composable
fun TerminalLogsConsole(logs: List<String>, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    val context = LocalContext.current
    
    // Auto-scroll to the bottom when new logs compile
    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E1E1E))
            .border(
                width = 1.dp,
                color = Color(0xFF333333),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(end = 40.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(logs) { log ->
                val textColor = when {
                    log.contains("✅") -> Color(0xFF81C784)
                    log.contains("❌") -> Color(0xFFE57373)
                    log.contains("⚠") -> Color(0xFFFFB74D)
                    log.contains("🔗") || log.contains("🔍") -> Color(0xFF64B5F6)
                    else -> Color(0xFFE0E0E0)
                }
                Text(
                    text = log,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = textColor,
                    lineHeight = 16.sp
                )
            }
        }

        // Beautiful floating copy logs button
        FilledIconButton(
            onClick = {
                if (logs.isNotEmpty()) {
                    val fullText = logs.joinToString("\n")
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Terminal Logs", fullText)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(
                        context,
                        if (fullText.any { it in '\u0600'..'\u06FF' }) "لاگ‌ها کپی شدند!" else "Logs copied successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "لاگ یا گزارشی برای کپی وجود ندارد",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(34.dp)
                .testTag("copy_logs_button"),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = Color(0xFF2E2E2E),
                contentColor = Color(0xFFE0E0E0)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy Logs",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun ArchiveItemActionDialog(
    isOpen: Boolean,
    title: String,
    content: String,
    uriString: String?,
    appLanguage: String,
    onDismiss: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    if (!isOpen) return
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(if (appLanguage == "fa") "بستن" else "Close")
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📄", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (appLanguage == "fa") "مدیریت سند استخراج شده" else "Document Management",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Open File button
                Button(
                    onClick = {
                        openMarkdownFile(context, uriString, content, title)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (appLanguage == "fa") "باز کردن و مشاهده در گوشی" else "Open in External App")
                }
                // Copy Markdown button
                OutlinedButton(
                    onClick = {
                        onCopy()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (appLanguage == "fa") "کپی متن مارک‌داون" else "Copy Markdown Content")
                }
                // Share button
                OutlinedButton(
                    onClick = {
                        onShare()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (appLanguage == "fa") "اشتراک‌گذاری" else "Share Document")
                }
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun SimpleMarkdownRenderer(content: String, modifier: Modifier = Modifier) {
    val cleanContent = content
        .replace("<div dir=\"rtl\">", "")
        .replace("<div dir='rtl'>", "")
        .replace("<div dir=rtl>", "")
        .replace("</div>", "")
        .trim()
    val lines = cleanContent.split("\n")
    var inCodeBlock = false
    val codeBlockLines = remember { mutableStateListOf<String>() }
    
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        lines.forEach { line ->
            val trimmedLine = line.trim()
            if (trimmedLine.startsWith("```")) {
                if (inCodeBlock) {
                    val codeText = codeBlockLines.joinToString("\n")
                    item {
                        CodeBlockView(codeText)
                    }
                    codeBlockLines.clear()
                    inCodeBlock = false
                } else {
                    inCodeBlock = true
                }
            } else if (inCodeBlock) {
                codeBlockLines.add(line)
            } else {
                when {
                    trimmedLine.startsWith("# ") -> {
                        item {
                            Text(
                                text = trimmedLine.removePrefix("# "),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), thickness = 2.dp)
                        }
                    }
                    trimmedLine.startsWith("## ") -> {
                        item {
                            Text(
                                text = trimmedLine.removePrefix("## "),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f))
                        }
                    }
                    trimmedLine.startsWith("### ") -> {
                        item {
                            Text(
                                text = trimmedLine.removePrefix("### "),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                ),
                                color = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
                            )
                        }
                    }
                    trimmedLine.startsWith("- ") || trimmedLine.startsWith("* ") -> {
                        val bulletText = trimmedLine.substring(2)
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("•  ", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                                Text(
                                    text = bulletText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                    trimmedLine.startsWith("> ") -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                                    .background(
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .drawBehind {
                                        drawLine(
                                            color = Color(0xFFD0BCFF),
                                            start = Offset(0f, 0f),
                                            end = Offset(0f, size.height),
                                            strokeWidth = 10f
                                        )
                                    }
                                    .padding(start = 14.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)
                            ) {
                                Text(
                                    text = trimmedLine.removePrefix("> "),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontStyle = FontStyle.Italic
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                    trimmedLine.isEmpty() -> {
                        item { Spacer(modifier = Modifier.height(4.dp)) }
                    }
                    else -> {
                        item {
                            Text(
                                text = line,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                modifier = Modifier.padding(vertical = 1.dp)
                            )
                        }
                    }
                }
            }
        }
        if (inCodeBlock && codeBlockLines.isNotEmpty()) {
            val codeText = codeBlockLines.joinToString("\n")
            item {
                CodeBlockView(codeText)
            }
        }
    }
}

@Composable
fun CodeBlockView(code: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF333333), shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            text = code,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = Color(0xFFE5C07B),
            lineHeight = 16.sp
        )
    }
}

@Composable
fun FallbackDecisionPanel(
    currentLink: String,
    appLanguage: String,
    onRetry: () -> Unit,
    onUseLocalAll: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.12f)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("⚠️", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (appLanguage == "fa") "خطا در اتصال به سرور" else "Connection Failed",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (appLanguage == "fa") {
                    "امکان برقراری ارتباط عمیق با سرور Jina فراهم نشد:\n$currentLink"
                } else {
                    "Jina Reader offline for:\n$currentLink"
                },
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = onRetry,
                    modifier = Modifier.weight(1f).height(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        if (appLanguage == "fa") "تلاش مجدد Jina" else "Retry Jina",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Button(
                    onClick = onUseLocalAll,
                    modifier = Modifier.weight(1.1f).height(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(Icons.Default.SettingsInputComponent, contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        if (appLanguage == "fa") "موتور محلی" else "Use Local",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1.2f).height(34.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        if (appLanguage == "fa") "لغو کل فرآیند" else "Cancel Crawl",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun FallbackConfirmationDialog(
    currentLink: String,
    appLanguage: String,
    onRetry: () -> Unit,
    onUseLocalAll: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("⚠️", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (appLanguage == "fa") "خطا در اتصال به سرور" else "Connection Failed",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        text = {
            Text(
                text = if (appLanguage == "fa") {
                    "امکان برقراری ارتباط با سرور هوشمند Jina برای آدرس زیر فراهم نشد:\n\n" +
                            "$currentLink\n\n" +
                            "لطفاً یکی از سه تصمیم زیر را برای ادامه فرآیند اتخاذ کنید:"
                } else {
                    "Failed to connect to Jina Reader server for the following address:\n\n" +
                            "$currentLink\n\n" +
                            "Please choose one of the three options below to proceed:"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // گزینه اول: تلاش مجدد با Jina
                Button(
                    onClick = onRetry,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (appLanguage == "fa") "تلاش مجدد اتصال با Jina" else "Retry Jina Connection")
                }
                // گزینه دوم: ادامه همه با موتور محلی
                Button(
                    onClick = onUseLocalAll,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.SettingsInputComponent, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (appLanguage == "fa") "اسکراپ باقی صفحات با موتور محلی" else "Use Local Engine for All")
                }
                // گزینه سوم: لغو کامل
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (appLanguage == "fa") "لغو کامل عملیات استخراج" else "Cancel Scraping Operation")
                }
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
