# Verify an APK

## 1. Verify SHA-256

Windows PowerShell:

```powershell
Get-FileHash -Algorithm SHA256 .\MozahhemGir.apk
```

Linux/macOS:

```bash
sha256sum MozahhemGir.apk
```

Compare the result with the checksum published in the matching GitHub Release.

## 2. Verify signing certificate

With Android SDK Build Tools installed:

```bash
apksigner verify --print-certs MozahhemGir.apk
```

Compare the certificate SHA-256 digest with the fingerprint published by the project. A matching filename is not sufficient; the certificate and checksum should both match.

## 3. Inspect permissions

```bash
apkanalyzer manifest permissions MozahhemGir.apk
```

The current trust model expects no app-requested permissions.

## 4. Trace to source

Match the release tag/commit in the release notes, inspect the source at that revision, and review the corresponding CI run.
