# Trust and Transparency

Open source is one part of the trust chain. A user should be able to inspect **source → build → signature → checksum**.

## What the repository demonstrates

- the app requests no Android permissions;
- call decisions are local;
- no network stack is required;
- rules are visible in source control;
- CI builds every change and runs tests;
- security-sensitive manifest changes fail CI.

## What open source alone does not prove

A random APK found on the Internet is not automatically trustworthy just because this repository exists. An APK can be modified after compilation. Official releases therefore need:

1. a stable Android signing certificate;
2. a published certificate SHA-256 fingerprint;
3. a SHA-256 checksum for each distributed APK;
4. a release tag that identifies the source revision;
5. release notes describing rule and security changes.

## CI artifacts

GitHub Actions artifacts are useful for independent inspection and testing. Unless explicitly promoted and signed through the official release process, they are not the canonical public release.
