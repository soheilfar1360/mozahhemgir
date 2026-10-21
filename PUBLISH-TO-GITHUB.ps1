$ErrorActionPreference = 'Stop'

$repo = 'soheilfar1360/mozahhemgir'
$description = 'Open-source, offline Android call screening for prefix-based unwanted calls in Iran.'

if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    throw 'Git is not installed or not available in PATH.'
}
if (-not (Get-Command gh -ErrorAction SilentlyContinue)) {
    throw 'GitHub CLI (gh) is required. Install it, then run: gh auth login'
}

& gh auth status
if ($LASTEXITCODE -ne 0) {
    throw 'GitHub CLI is not authenticated. Run: gh auth login'
}

if (-not (Test-Path '.git')) {
    git init -b main
    git add .
    git commit -m 'Open-source MozahhemGir v0.3.0'
}

& gh repo view $repo *> $null
if ($LASTEXITCODE -ne 0) {
    gh repo create $repo --public --description $description --source . --remote origin --push
} else {
    if (-not (git remote get-url origin 2>$null)) {
        git remote add origin "https://github.com/$repo.git"
    }
    git branch -M main
    git push -u origin main
}

Write-Host "Published: https://github.com/$repo" -ForegroundColor Green
