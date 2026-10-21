$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$gradleVersion = '9.6.0'
$bootstrapDir = Join-Path $root '.gradle-bootstrap'
$gradleHome = Join-Path $bootstrapDir "gradle-$gradleVersion"
$gradleBat = Join-Path $gradleHome 'bin\gradle.bat'
$distZip = Join-Path $bootstrapDir "gradle-$gradleVersion-bin.zip"

Push-Location $root
try {
    Write-Host 'Checking Java...' -ForegroundColor Cyan
    & java -version
    if ($LASTEXITCODE -ne 0) {
        throw 'Java is not available in PATH. JDK 17 is required.'
    }

    # Help Gradle find a standard Android Studio SDK installation on Windows.
    $sdkCandidates = @(
        $env:ANDROID_HOME,
        $env:ANDROID_SDK_ROOT,
        (Join-Path $env:LOCALAPPDATA 'Android\Sdk')
    ) | Where-Object { $_ -and (Test-Path $_) }

    if ($sdkCandidates.Count -gt 0) {
        $sdk = $sdkCandidates[0]
        $sdkForProperties = $sdk -replace '\\', '/'
        Set-Content -Path (Join-Path $root 'local.properties') -Value "sdk.dir=$sdkForProperties" -Encoding ASCII
        Write-Host "Android SDK: $sdk" -ForegroundColor DarkGray
    }
    else {
        Write-Warning 'Android SDK was not auto-detected. If the build fails with SDK location not found, install/open Android Studio once or set ANDROID_HOME.'
    }

    # The Gradle distributions site does not serve gradle-9.6.0-wrapper.jar itself.
    # Bootstrap with the official binary distribution, then build directly.
    if (-not (Test-Path $gradleBat)) {
        New-Item -ItemType Directory -Force -Path $bootstrapDir | Out-Null
        Write-Host "Downloading official Gradle $gradleVersion distribution..." -ForegroundColor Cyan

        if (-not (Test-Path $distZip)) {
            Invoke-WebRequest `
                -UseBasicParsing `
                -Uri "https://services.gradle.org/distributions/gradle-$gradleVersion-bin.zip" `
                -OutFile $distZip
        }

        Write-Host 'Extracting Gradle...' -ForegroundColor Cyan
        Expand-Archive -Path $distZip -DestinationPath $bootstrapDir -Force
    }

    Write-Host 'Building debug APK...' -ForegroundColor Cyan
    & $gradleBat :app:assembleDebug
    if ($LASTEXITCODE -ne 0) {
        throw "Gradle build failed with exit code $LASTEXITCODE"
    }

    $apk = Join-Path $root 'app\build\outputs\apk\debug\app-debug.apk'
    if (-not (Test-Path $apk)) {
        throw "Build completed but APK was not found at: $apk"
    }

    Write-Host "`nBUILD SUCCESSFUL" -ForegroundColor Green
    Write-Host 'APK:' -ForegroundColor Green
    Write-Host $apk
}
finally {
    Pop-Location
}
