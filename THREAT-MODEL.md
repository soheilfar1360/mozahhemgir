# Threat Model

## Assets to protect

1. The user's ability to receive legitimate calls.
2. Caller-number privacy.
3. Integrity of the prefix catalog.
4. Integrity and authenticity of distributed APKs.
5. Release signing keys.

## Primary threats and controls

### False-positive blocking
A rule could be too broad or incorrect.

Controls:
- prefixes must be at least four domestic digits;
- user can remove any rule locally;
- catalog changes require evidence and false-positive review;
- unexpected runtime errors fail open and allow the call.

### Covert data exfiltration
A malicious modification could add network or sensitive data permissions.

Controls:
- manifest currently contains zero `uses-permission` entries;
- CI rejects new manifest permissions;
- no network library or telemetry SDK is included;
- open source review makes changes inspectable.

### Malicious catalog contribution
A contributor could propose a prefix intended to suppress legitimate calls.

Controls:
- catalog PRs require evidence;
- complete personal numbers are prohibited;
- maintainers assess scope and false-positive risk;
- CI and code review protect the default branch.

### Fake or modified APK distribution
An attacker could redistribute a modified APK using the project's name.

Controls:
- official releases use a stable signing key;
- release checksums are published;
- users can compare certificate fingerprints and SHA-256 values;
- source and CI build history remain public.

### Signing-key compromise
An attacker with the release key could publish apparently authentic builds.

Controls:
- key material is never committed;
- maintain an offline backup and restricted access;
- document certificate fingerprints publicly;
- use Play App Signing if distributing through Google Play.

## Out of scope

MozahhemGir does not attempt to identify the human or organization behind a caller number, guarantee that every blocked call is spam, or protect against telecom-network spoofing.
