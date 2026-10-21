# Release Process

1. Complete `TEST-CHECKLIST.md` on at least one supported physical Android device.
2. Update `versionCode`, `versionName`, and rule-catalog metadata.
3. Run CI and review the unsigned release artifact.
4. Build the release candidate from the tagged source revision.
5. Sign with the stable release key. Never commit or paste the key/password into logs, issues, chat, or source control.
6. Run `apksigner verify --print-certs` and record the signing certificate SHA-256 fingerprint.
7. Generate the APK SHA-256 checksum.
8. Create a Git tag and GitHub Release containing the signed APK, checksum, certificate fingerprint, source commit, and changelog.
9. Keep an offline backup of the release key and document key custody separately from the public repository.

A CI signing workflow can be added later if secure repository secrets and an explicit key-rotation/incident plan are configured. The initial project intentionally does not embed any signing secret or key material.
