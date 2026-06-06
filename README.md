# URL to Markdown Android Application

> **🇮🇷 [راهنمای فارسی کاربردی (Persian Tutorial Guide)](#راهنمای-فارسی-url-to-markdown)**  
> *Click the link above to jump directly to the complete Persian tutorial and reference guide.*

---

## English Documentation & Tutorial

Welcome to the ultimate **URL to Markdown** Android application! This app is a robust, modern, and production-ready utility designed for Jetpack Compose. It allows you to download, scrape, and compile single web pages or entire site directories into clean, structured Markdown, HTML, XML, or Plain Text files ready for personal study, LLM ingestion, or offline storage.

---

### 🌟 Key Features

1. **Jina Reader API Integration**: Highly optimized extraction of full web layouts leveraging the high-fidelity `r.jina.ai` gateway (supporting dynamic custom selectors, custom JSON structure formatting, and authenticated connection states).
2. **Robust Multi-Page Crawler (Deep Scrape)**: Integrates JSoup to extract nested index lists dynamically, filter out unwanted pages (e.g., login, carts, signups), and respect page limit boundaries.
3. **Fail-safe Fallback Engine**: If Jina's proxy fails or triggers rate limits, a fallback engine triggers custom user prompts—prompting standard retry, full local parse (via client JSoup parsing), or complete cancellation.
4. **Adaptive Pacing Modalities**: 
   - **Auto-Throttle**: Computes dynamic, safe request delays scaled proportionally to total scrap size.
   - **Custom Delays**: Allows users to input precise delays between calls in seconds.
   - **Fast Burst**: Zero-delay crawling for users with standard paid Jina Tier API keys.
5. **Dynamic ABI Splits & Automatic APK Renaming**: Configured in Gradle to compile localized, specialized architectures (`armeabi-v7a`, `arm64-v8a`, `x86`, `x86_64`) alongside a universal pack. It automatically names output files mapped precisely to their machine target.
6. **Automatic CI/CD Setup**: Fully written workflows under `.github/workflows/release.yml` with automated release assets compilation, safe fallback signing keystores, and proper write contents permissions.
7. **RTL & Persian font alignment**: Full localization with the beautiful open-source **Vazirmatn** Persian font configured as the primary styling framework whenever Farsi layout flows are detected.

---

### 🛠️ Technical Architecture & Under the Hood

The application strictly follows modern Google Android MVVM principles written fully in **Kotlin** and **Jetpack Compose**:

* **`ScraperViewModel`**: Orchestrates state flows via nested `StateFlow<CrawlUiState>`, including:
  - `Idle`: Prompting input fields.
  - `Scanning`: Indexing URLs and evaluating web maps.
  - `Converting`: Active download state showing indices, current URLs, and live logs.
  - `WaitingForFallback`: Temporarily suspended sequence waiting on user interactive fallback directives.
  - `Success`: Exposing complete downloads, save directories, and log history.
  - `Error`: Direct visual logs describing connection crashes.
* **Persistent Settings**: Configurations (including API keys, exclusion patterns, selector overrides, formats, and language settings) are persisted across device sessions using Android `SharedPreferences`.
* **Downloads API**: Directly accesses Android's standard `MediaStore` table securely. Compiled documents are delivered safely to the phone's default `/Downloads` directory without requiring dirty, deprecated broad system storage write privileges.

---

### ⚙️ Configuration Guide

In the Settings console within the application UI, you can fine-tune several properties:

| Config Property | Default Value | Description |
| :--- | :--- | :--- |
| **Jina API Key** | *Empty* | Eliminates request caps on Jina's servers and allows fast burst rate speed. |
| **Max Page Limit** | `30` | Imposes maximum boundaries to protect internal storage during deep indexing. |
| **Exclude Keywords** | `login, logout, signin...` | Filters out specific paths from directory crawls (like authentication, profiles, carts, help centers). |
| **Return Format** | `markdown` | Choose output targets: `markdown`, `html`, `text` (plain TXT) or `xml`. |
| **Remove Selectors** | *Empty* | CSS Selectors (comma-separated, e.g., `header, .sidebar`) representing layout objects to remove. |
| **Target Selectors** | *Empty* | CSS Selectors directing Jina to concentrate *only* on a specific class or container. |

---

### 🚀 CI/CD Release Actions Instructions

When you push this repository to your remote GitHub workspace:

1. **Tag Triggers**: Generating or pushing tags beginning with `v` (e.g. `v1.7.0`) initiates the `.github/workflows/release.yml` actions sequence.
2. **Build Configurations**: 
   - Runs on Ubuntu VMs.
   - Automatically checks for base-64 keystore credentials (`RELEASE_KEYSTORE_BASE64`) to build signed APKs.
   - **Keystore Fallback**: If the key secret is missing, it dynamically triggers local auto-generation of an upload key (`my-upload-key.jks`) using placeholder passwords, keeping compilation green.
   - Applies strict write permissions to create standard Releases securely.
3. **Artifact Renaming & Delivery**: Outputs individual architecture-specific APK packages directly under your tag's release body assets list for neat user distribution.

---

---

## راهنمای فارسی (Persian Tutorial & Reference Guide)

<div dir="rtl">

به راهنمای جامع پروژه **مبدل URL به Markdown (URL to Markdown)** برای سیستم‌عامل اندروید خوش آمدید!  
این اپلیکیشن، ابزاری مدرن، سریع و مجهز به رابط کاربری Jetpack Compose است که به توسعه‌دهندگان و کاربران اجازه می‌دهد تا صفحات تکی وب یا کل ساختار یک دایرکتوری وب‌سایت را جمع‌آوری کرده و در قالب فایل‌های خروجی ساختاریافته (نظیر Markdown، HTML، XML و Text) برای استفاده در هوش مصنوعی (LLMs)، ذخیره‌سازی آفلاین یا مطالعه شخصی دریافت نمایند.

---

### 🌟 ویژگی‌های کلیدی پروژه

1. **یکپارچه‌سازی با API قدرتمند Jina Reader**: استخراج بی‌نقص جزئیات محتوا با کمک سرویس پایدار `r.jina.ai` (همراه با قابلیت حذف هدرها، ستون‌های کناری، کوکی‌ها و تصاویر اضافی برای سبک‌سازی حداکثری فایل‌ها).
2. **قابلیت خزش عمیق (Deep Scrape)**: استفاده مداوم از کتابخانه استاندارد JSoup جهت واکشی لینک‌های تو در تو در دایرکتوری‌های مقصد و اعمال محدودیت‌ها برای صفحات نامربوط (مانند سبد خرید، حساب کاربری، و پنل‌های ادمین).
3. **سیستم پیشرفته‌ی واکنش به خطا (Fallback)**: در صورتی که اتصال اینترنت قطع شود یا محدودیت‌های دسترسی API اعمال گردد، برنامه برخلاف ابزارهای مشابه بلافاصله متوقف نمی‌شود؛ بلکه با تعلیق موقت فرآیند، انتخابی سه‌گانه به شما ارائه می‌دهد:
   * **تلاش مجدد (Retry)**: فراخوانیِ دوباره اتصال آسیب دیده.
   * **استفاده از تحلیل‌گر بومی (Local JSoup)**: اجرای موتور استخراج لوکال برای مابقی صفحات بدون نیاز به سرور واسط.
   * **لغو هوشمند (Cancel)**: ذخیره‌سازی محتوای جمع‌آوری شده تا خطای جاری و خروج ایمن.
4. **شیوه‌های ناوبری سرعت در خزش (Speed Specs)**:
   * **برنامه‌ریزی تنظیمی (Auto-Throttle)**: بررسی تعداد صفحات و ایجاد هوشمندانه تاخیرهای ثانیه‌ای به صورت داینامیک جهت جلوگیری از مسدود شدن IP در وب‌سایت‌های هدف.
   * **تاخیر دلخواه (Custom Delay)**: وارد کردن میزان ثانیه دقیق برای توقف بین درخواست‌ها.
   * **حالت توربو (Fast Burst)**: پردازش فوق‌سریع بدون ثانیه انتظار در صورت استفاده از کلید اختصاصی Jina.
5. **توزیع سبک فایل‌های خروجی بر اساس ساختار تراشه (ABI Splits)**: تنظیم هسته بیلد گرادل پروژه به گونه‌ای که به جای ارائه فایل‌های نصبی بسیار سنگین و یکپارچه، خروجی‌های مجزا و بهینه‌سازی شده برای معماری‌های سخت‌افزاری گوناگون نظیر `arm64-v8a`، `armeabi-v7a`، `x86` و `x86_64` به همراه یک فایل تجمیعی تولید و نام‌گذاری می‌کند.
6. **مدیریت استقرار همیشه‌سبز در گیت‌هاب (GitHub Actions CI/CD)**: پیکربندی پیشرفته گیت‌هاب جهت استخراج، اعتبارسنجی، بیلد و انتشار خودکار فایل‌های فشرده‌ی خروجی در قالب زنجیره ریلیز‌های گیت‌هاب با امنیت بالا.
7. **خوانایی فوق‌العاده متون فارسی با قلم وزیر‌متن (Vazirmatn)**: در هنگام پردازش اسناد فارسی، متون به صورت کاملاً راست‌چین و بدون ریختگی حروف با فونت تخصصی وزیر‌متن قالب‌گذاری می‌شوند.

---

### 💻 معماری فنی و ساختار درونی نرم‌افزار

این برنامه بر پایه اصول مدرن گوگل در توسعه اندروید و معماری پیشرو **MVVM** بنا شده است:

* **لایه‌ی `ScraperViewModel`**: هدایت‌کننده اصلی موتور این سیستم است که فرآیندها را از طریق الگوهای رفتاری زنده (Live States) مدیریت می‌کند:
  * وضعیت **Idle**: آماده‌باش برنامه و منتظر دریافت آدرس یا آدرس‌های مبدا.
  * وضعیت **Scanning**: پایش لینک‌ها در دایرکتوری در صورت فعال بودن خزش عمیق.
  * وضعیت **Converting**: فرآیند تبدل زنده و فعال به فرمت‌های مقصد، نمایش فایل کنونی در دست پردازش و جزئیات ترافیک شبکه.
  * وضعیت **WaitingForFallback**: توفق موقت فرآیند به علت خطاهای پیش‌بینی نشده در گیت‌وی به قصد تصمیم‌گیری تعاملی توسط کاربر.
  * وضعیت **Success**: ذخیره‌سازی نهایی خروجی و ثبت تاریخچه و ارقام.
  * وضعیت **Error**: ثبت دقیق علت شکست خط فرمان برای تصحیح رابط‌ها.
* **ذخیره‌سازی اطلاعات با SharedPreferences**: تمام کلیدهای خصوصی وارد شده، الگوهای فیلترینگ، زبان برنامه و تنظیمات حذف کلاس‌های CSS به طور دائم روی دستگاه ذخیره می‌شوند تا نیازی به راه‌اندازی و بازنویسی هرباره نباشد.
* **واسط MediaStore مجاز**: خروجی دانلود شده مستقیماً و با روش‌های کاملاً امن به پوشه‌ی اصلی `/Downloads` (دانلودها) در حافظه‌ی پیش‌فرض دستگاه فرستاده می‌شود تا امنیت سیستم کاربر کاملاً حفظ شود و نیاز به دریافت دسترسی‌های سیستمی خطرناک ذخیره‌سازی از بین برود.

---

### ⚙️ راهنمای بخش تنظیمات پیشرفته (Settings)

با مراجعه به منوی تنظیمات در اپلیکیشن می‌توانید فرآیند خزش خود را کاملاً شخصی‌سازی کنید:

| نام ویژگی | مقدار پیش‌فرض | عملکرد تخصصی |
| :--- | :--- | :--- |
| **کلید خصوصی Jina** | *خالی* | افزایش حد مجاز درخواست‌های ارسالی بدون بافرینگ و فعال‌ساز سرعت توربو. |
| **سقف صفحات مجاز** | `30` | تعیین بالاترین حد مجاز تعداد صفحات فرعی وب‌سایت برای جلوگیری از اتلاف حجم اینترنت و حافظه. |
| **فیلتر کلمات منفی** | `signin, cart...` | نادیده گرفتن صفحات غیرمفید برای هوش مصنوعی نظیر ورود، خروج، سبد خرید، قوانین، تماس با ما و... |
| **فرمت نهایی فایل** | `markdown` | انتخاب قالب‌های مختلف خروجی در قالب یکی از حالت‌های `markdown` یا `html` یا `text` و یا `xml`. |
| **کلاس‌های حذفی (CSS)** | *خالی* | مشخص کردن برچسب‌ها یا شناسه‌های خاص نظیر `nav, footer` برای حذف کامل از فرآیند پردازش محتوا. |

---

### 🛠️ فرآیند توسعه ریلیزها در GitHub Actions

برای هر مرتبه‌ای که کدهای پروژه را به گیت‌هاب مخزن خود میفرستید:

1. **انگیزش تگ‌ها**: اعمال تگ جدید با پیشوند `v` (مانند `v1.7.0`) فرآیند بارگذاری مخزن .github به صورت خودکار فعال را می‌کند.
2. **برنامه‌ریزی محیط اجرا**: 
   * ساخت نمونه بر پایه اوبونتو.
   * دریافت و پردازش خودکار امضای اختصاصی از بخش سکرت‌های گیت‌هاب.
   * **امضای پشتیبان امن**: در صورتی که سکرت کلید امضاء خالی باشد، سیستم با اجرای خودکار دستور `keytool` یک کلید آپلود محلی با مشخصات فرضی ساخته و فرآیند پکیج را متوقف نمی‌کند.
   * نوشتن صریح دسترسی `permissions: contents: write` در پایپ‌لاین که خطای عدم دسترسی ۴۰۳ را در هنگام آپلود دارایی‌ها به بخش ریلیز به طور کامل ریشه‌کن می‌سازد.
3. **تولید مپ‌های کم حجم**: بیلد خودکار آرشیوها و انتشار خروجی‌ها بر اساس تفکیک پردازنده‌ها تحت یک ریلیز شکیل در اختیار شماست.

</div>
