# Release Test Checklist

## Automated gates
- [ ] `scripts/check_manifest.py` passes.
- [ ] Unit tests pass.
- [ ] Debug APK builds.
- [ ] Unsigned release APK builds.
- [ ] SHA-256 is generated for the candidate artifact.

## Must block with the default 0942 rule
- [ ] `0942…`
- [ ] `+98942…`
- [ ] `0098942…`
- [ ] `98942…`

## Must allow
- [ ] ordinary `091x` mobile number that does not match a configured prefix;
- [ ] ordinary landline number;
- [ ] malformed/empty caller handle (fail-open);
- [ ] all calls when the in-app filter switch is OFF.

## Platform behavior
- [ ] saved contact matching a blocked prefix is evaluated as expected for the target Android build;
- [ ] screen locked;
- [ ] app swiped away from recents;
- [ ] after device reboot;
- [ ] dual-SIM incoming calls;
- [ ] Call Screening role revoked: app stops screening.

## Safety / regression
- [ ] add and remove prefix;
- [ ] reject prefix shorter than four digits;
- [ ] normalize `+98`, `0098`, and `98` forms correctly;
- [ ] blocked counter increments only on actual matches;
- [ ] non-matching calls ring with no perceptible screening delay;
- [ ] no unexpected permission prompt appears.

## Release authenticity
- [ ] signed with the official release key;
- [ ] signing certificate fingerprint recorded;
- [ ] APK SHA-256 recorded;
- [ ] source commit/tag recorded;
- [ ] release notes list rule-catalog changes.
