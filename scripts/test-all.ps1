param(
    [switch]$CoverageCheck
)

$ErrorActionPreference = "Stop"
$Root = Split-Path -Parent $PSScriptRoot
$IsWindowsHost = $env:OS -eq "Windows_NT"

$backendModules = @(
    "backend/ms-auth",
    "backend/ms-donation",
    "backend/ms-necessity",
    "backend/ms-logistic",
    "backend/bff"
)

Write-Host "==> Ejecutando tests backend..."
foreach ($module in $backendModules) {
    $modulePath = Join-Path $Root $module
    $task = if ($CoverageCheck) { "jacocoTestCoverageVerification" } else { "test" }

    Write-Host "==> $module :: ./gradlew $task"
    Push-Location $modulePath
    try {
        if ($IsWindowsHost) {
            .\gradlew.bat $task
        } else {
            ./gradlew $task
        }
    } finally {
        Pop-Location
    }
}

Write-Host "==> Ejecutando tests frontend..."
Push-Location (Join-Path $Root "frontend")
try {
    if (-not (Test-Path "node_modules")) {
        npm install
    }

    if ($CoverageCheck) {
        npm run coverage
    } else {
        npm test
    }
} finally {
    Pop-Location
}

Write-Host "==> Flujo de tests finalizado."
