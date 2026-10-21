# Security Policy

## Security posture

MozahhemGir intentionally minimizes its attack surface:
- zero app-requested Android permissions;
- no network access;
- no remote configuration;
- no third-party analytics or ad SDKs;
- Android backup disabled;
- cleartext network traffic disabled;
- the screening service is protected by `android.permission.BIND_SCREENING_SERVICE`;
- screening failures are fail-open.

CI runs `scripts/check_manifest.py` and fails if an app-requested permission is introduced.

## Supported version

Security fixes are maintained on the latest release line.

## Reporting a vulnerability

Do not publish exploit details, private phone numbers, signing material, or other sensitive information in a public issue. Use GitHub private vulnerability reporting when it is enabled for the repository. Until then, contact the maintainer through the GitHub profile and coordinate a private report.

## Signing keys

Release signing keys and passwords must never be committed to this repository, attached to an issue, stored in CI logs, or shared in screenshots. A compromised signing key requires a release-key incident response.
