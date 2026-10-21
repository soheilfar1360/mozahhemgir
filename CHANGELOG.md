# Changelog

## 0.3.0 — Open-source readiness

- Added Apache-2.0 licensing and public contribution policy.
- Added bilingual project documentation.
- Added privacy, security, threat-model, trust, verification, and release documentation.
- Moved community-maintained Iranian default rules to `rules/iran.json` and bundle them into the app at build time.
- Added rule-catalog validation and fail-safe fallback.
- Added unit tests for Iranian number normalization.
- Added CI manifest audit that rejects app-requested Android permissions.
- Added GitHub Actions build/test/audit workflow and verification artifacts.
- Added bug and prefix-report issue templates.
- Kept the application offline, telemetry-free, and fail-open.
