param(
    [Parameter(Mandatory=$true)]
    [string]$Path
)

$resolved = Resolve-Path $Path
$hash = Get-FileHash -Algorithm SHA256 $resolved
"$($hash.Hash.ToLower())  $([System.IO.Path]::GetFileName($resolved))"
