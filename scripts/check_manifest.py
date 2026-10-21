#!/usr/bin/env python3
from pathlib import Path
import sys
import xml.etree.ElementTree as ET

manifest = Path(__file__).resolve().parents[1] / "app/src/main/AndroidManifest.xml"
root = ET.parse(manifest).getroot()
android = "{http://schemas.android.com/apk/res/android}"

permissions = [node.attrib.get(android + "name", "") for node in root.findall("uses-permission")]
if permissions:
    print("ERROR: MozahhemGir's trust model requires zero app-requested permissions.")
    for permission in permissions:
        print(" -", permission)
    sys.exit(1)

services = root.findall("./application/service")
screening = [s for s in services if s.attrib.get(android + "name") == ".SpamCallScreeningService"]
if len(screening) != 1:
    print("ERROR: expected exactly one SpamCallScreeningService declaration")
    sys.exit(1)

required = "android.permission.BIND_SCREENING_SERVICE"
if screening[0].attrib.get(android + "permission") != required:
    print("ERROR: screening service must be protected by", required)
    sys.exit(1)

print("Manifest trust checks passed: zero uses-permission entries; screening service is system-protected.")
