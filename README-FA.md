# راهنمای کامل FARX GOD و ساخت APK

## این بسته چیست؟
این نسخه یک پایه‌ی واقعی و قابل ساخت برای پروژه FARX است: اپ اندرویدی FARX Studio، ویرایشگر کد، کنسول اجرا، برش اولیه از مفسر FARX، تست‌های خودکار و گردش‌کار GitHub Actions برای ساخت APK.

این نسخه را نباید با محصول نهایی FARX اشتباه گرفت. قابلیت‌هایی که هنوز موتور اجرایی ندارند صریحاً در وضعیت «برنامه‌ریزی‌شده» هستند.

## خروجی
نام Artifact در GitHub Actions برابر `FARX-Studio-Android` و نام فایل داخل آن `FARX-Studio-Android.apk` است. این خروجی Debug و مناسب آزمایش است، نه انتشار رسمی فروشگاهی.

## پیش‌نیازها
- حساب GitHub و دسترسی Write به مخزن `farhan6698/Farx`
- گوشی اندرویدی و مرورگر
- فعال بودن GitHub Actions در مخزن
- اینترنت برای دریافت وابستگی‌های Gradle در اولین اجرا

## بارگذاری از گوشی
1. ZIP را دانلود و Extract کنید.
2. در GitHub مخزن `farhan6698/Farx` را باز کنید.
3. اگر مخزن کد قبلی دارد، ابتدا از آن نسخه پشتیبان بگیرید و فایل‌های هم‌نام را کورکورانه overwrite نکنید.
4. در صفحه Code روی Add file و سپس Upload files بزنید.
5. همه‌ی محتویات پوشه‌ی `FARX-GOD-Android-Studio` را انتخاب کنید؛ محتویات باید مستقیماً در ریشه‌ی مخزن قرار بگیرند. ساختار `.github/workflows` را حفظ کنید.
6. Commit changes را بزنید. اگر GitHub در موبایل انتخاب پوشه را سخت کرد، می‌توانید ZIP را در فایل‌منیجر استخراج کنید و از نسخه دسکتاپ مرورگر استفاده کنید؛ نیازی به نصب Android Studio نیست.

## اجرای ساخت
1. به تب Actions بروید.
2. Workflow با نام `FARX GOD - Android APK` را باز کنید.
3. اگر گزینه فعال‌سازی Actions نمایش داده شد، آن را فعال کنید.
4. Run workflow را بزنید و branch `main` را انتخاب کنید.
5. وارد اجرای جدید شوید. مرحله‌های `Run FARX engine tests` و `Build debug APK` باید سبز شوند.
6. در انتهای صفحه Artifacts را باز کنید و `FARX-Studio-Android` را دانلود کنید.
7. ZIP مربوط به Artifact را در گوشی استخراج کنید و `FARX-Studio-Android.apk` را نصب کنید.
8. اگر اندروید هشدار نصب از منبع ناشناس داد، فقط برای برنامه‌ای که خودتان از مخزن خود دریافت کرده‌اید اجازه موقت بدهید.

## اگر ساخت شکست خورد
- قرمز شدن `Run FARX engine tests`: خطای تست یا منطق زبان را در log همان مرحله بخوانید.
- قرمز شدن `Build debug APK`: log مربوط به Gradle را باز کنید؛ معمولاً خطای تنظیمات یا دانلود وابستگی است.
- نبودن Artifact: ساخت APK موفق نشده یا مسیر خروجی تغییر کرده است.
- خطای permission در Commit: حساب واردشده دسترسی Write به مخزن ندارد.
- Actions غیرفعال: در تنظیمات مخزن، بخش Actions را بررسی کنید.

## اجرای کد نمونه
برنامه با نمونه‌ای آماده باز می‌شود. دکمه RUN را بزنید تا خروجی در کنسول پایین نمایش داده شود.

نمونه:
```farx
MODE CODE
  PRINT "Hello FARX"
  LET a = 40
  LET b = 2
  ADD a b
  PRINT "Engine is alive"
END MODE
```

## توسعه بعدی
برای نزدیک‌شدن به مشخصات محصول کامل باید مرحله‌به‌مرحله این موارد پیاده‌سازی و تست شوند:
1. Lexer واقعی با tokenها و شماره خط/ستون
2. Parser و AST به‌جای تفسیر خطی فعلی
3. سیستم نوع‌ها، scope و semantic analyzer
4. IR، optimizer و code generator
5. Runtime، module loader و linker واقعی
6. پیاده‌سازی اجرایی هر Mode به‌صورت افزونه‌ی مستقل
7. FARX Package Manager و قالب بسته
8. Studio کامل: فایل‌منیجر، syntax highlighting، autocomplete، diagnostics، debugger، preview و build manager
9. پشتیبانی چندسکویی Windows/Linux/macOS/iOS
10. امضای Release و انتشار امن

## امنیت انتشار
APK فعلی Debug است. برای انتشار عمومی باید keystore و رمزها را در GitHub Secrets نگه داشت و workflow جداگانه‌ی Release ساخت. هرگز keystore یا رمز آن را commit نکنید.
