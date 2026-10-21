# Contributing

## Code changes

1. Keep the application offline and permission-minimal.
2. Do not add telemetry, advertising, analytics, remote configuration, or tracking.
3. Preserve fail-open call screening behavior unless a security review justifies a change.
4. Add or update tests for behavior changes.
5. Run `python3 scripts/check_manifest.py` and the unit tests before opening a PR.

## Prefix catalog changes

Do not submit complete personal phone numbers. Propose only a prefix in domestic format.

A default-block addition should include:
- evidence that the prefix is predominantly used for unwanted/promotional calls;
- an assessment of legitimate-use and false-positive risk;
- geographic/operator context when known;
- a reason the prefix should be enabled by default rather than left as an optional local rule.

Maintainers may reject or defer a rule when evidence is weak or the false-positive surface is too large.

## Security reports

Follow `SECURITY.md`. Do not post exploit details publicly.
