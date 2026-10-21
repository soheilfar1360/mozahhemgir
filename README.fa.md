# مزاحم‌گیر (MozahhemGir)

[English](README.md)

مزاحم‌گیر یک اپ اندروید Native و متن‌باز برای مسدود کردن تماس‌های ناخواسته بر اساس پیش‌شماره است. هستهٔ برنامه از API رسمی Android یعنی `CallScreeningService` استفاده می‌کند. کاتالوگ اولیهٔ ایران، `0942` را به‌عنوان Rule پیش‌فرض دارد.

## چرا قابل‌اعتمادتر است؟

| داده / دسترسی | اپ درخواست می‌کند؟ |
|---|---:|
| اینترنت | خیر |
| مخاطبین | خیر |
| Call Log | خیر |
| Phone State | خیر |
| SMS | خیر |
| موقعیت مکانی | خیر |
| میکروفون | خیر |
| نقش Call Screening اندروید | بله — با تأیید صریح کاربر |

در Manifest برنامه **هیچ `<uses-permission>`ای وجود ندارد**. عبارت `android.permission.BIND_SCREENING_SERVICE` فقط روی Service قرار دارد تا صرفاً سیستم Telecom اندروید بتواند به آن متصل شود؛ این یک Permission خطرناک یا Runtime Permission برای کاربر نیست.

تمام تصمیم‌گیری روی خود گوشی انجام می‌شود: بدون سرور، بدون حساب کاربری، بدون تبلیغات، بدون Analytics، بدون Telemetry و بدون دانلود Rule از اینترنت.

## رفتار ایمن

- `+98942…`، `0098942…` و `98942…` به فرم داخلی `0942…` نرمال می‌شوند.
- Rule کمتر از چهار رقم پذیرفته نمی‌شود.
- سیستم **Fail-open** است: اگر خطای غیرمنتظره‌ای رخ دهد تماس عبور می‌کند تا تماس سالم اشتباهی Block نشود.
- Ruleهای پیش‌فرض جامعه در [`rules/iran.json`](rules/iran.json) نگهداری و داخل APK بسته‌بندی می‌شوند.
- کاربر می‌تواند Ruleها را محلی اضافه یا حذف کند؛ چیزی از گوشی ارسال نمی‌شود.

## Build روی ویندوز

نیازمندی‌ها: JDK 17، Android SDK 36، AGP 9.4.0 و Gradle 9.6.0.

```powershell
Set-ExecutionPolicy -Scope Process Bypass
.\setup-and-build.ps1
```

خروجی Debug:

```text
appuild\outputspk\debugpp-debug.apk
```

CI گیت‌هاب به‌طور خودکار Manifest را از نظر Permission بررسی می‌کند، Unit Test اجرا می‌کند، APK Debug و Release بدون امضا می‌سازد و SHA-256 تولید می‌کند.

## گزارش Prefix جدید

برای پیشنهاد اضافه/حذف Prefix، Issue مخصوص Prefix باز کنید و **شماره کامل شخصی منتشر نکنید**. هر Rule پیش‌فرض باید شواهد کافی و ارزیابی False Positive داشته باشد.

## اعتبار فایل نصب

برای Release عمومی باید APK با یک Release Key ثابت امضا شود و Hash آن منتشر شود. Artifactهای CI به‌خودی‌خود Release رسمی امضاشده نیستند. جزئیات در [`TRUST.md`](TRUST.md) و [`docs/VERIFY.md`](docs/VERIFY.md) آمده است.

## مجوز

Apache License 2.0 — فایل [`LICENSE`](LICENSE).
