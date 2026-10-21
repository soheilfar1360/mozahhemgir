# MozahhemGir — مزاحم‌گیر

[فارسی](README.fa.md) · Open-source, offline Android call screening for prefix-based unwanted calls.

MozahhemGir is a minimal native Android app that uses Android's official `CallScreeningService` to reject incoming calls matching user-configured prefixes. The initial Iran catalog contains `0942` as a default rule.

## Trust model

MozahhemGir is deliberately small and inspectable:

| Capability / data | Requested by app? |
|---|---:|
| Internet | No |
| Contacts | No |
| Call log | No |
| Phone state | No |
| SMS | No |
| Location | No |
| Microphone | No |
| Android Call Screening role | Yes — explicit system role |

There are **zero `<uses-permission>` entries** in the app manifest. The service itself is protected with `android.permission.BIND_SCREENING_SERVICE`, which restricts binding to Android's telecom system; it is not a runtime permission requested from the user.

All matching happens on-device. There is no account, server, telemetry, advertising SDK, analytics SDK, or remote rule download.

## Safety behavior

- Prefixes are normalized to Iranian domestic format (`+98942…`, `0098942…`, and `98942…` → `0942…`).
- Prefix rules must contain at least four domestic digits beginning with `0`.
- Screening is **fail-open**: an unexpected error allows the call rather than risking an incorrect block.
- Community defaults live in [`rules/iran.json`](rules/iran.json) and are bundled into the APK at build time.
- Users can locally add or remove rules without sending data anywhere.

## Build

Requirements: JDK 17, Android SDK 36, Android Gradle Plugin 9.4.0, Gradle 9.6.0.

### Windows

```powershell
Set-ExecutionPolicy -Scope Process Bypass
.\setup-and-build.ps1
```

### Any platform with Gradle 9.6.0 installed

```bash
gradle :app:testDebugUnitTest :app:assembleDebug
```

The CI workflow performs a manifest trust audit, unit tests, debug build, unsigned release build, and SHA-256 generation.

## Verify before installing

Read [`TRUST.md`](TRUST.md) and [`docs/VERIFY.md`](docs/VERIFY.md). Official release APKs should be signed with the project's stable release key and published together with SHA-256 checksums. CI artifacts are **not** official signed releases.

## Contributing

Bug reports, rule evidence, tests and security review are welcome. See [`CONTRIBUTING.md`](CONTRIBUTING.md). Do not submit complete personal phone numbers in issues or pull requests.

## License

Apache License 2.0. See [`LICENSE`](LICENSE).
