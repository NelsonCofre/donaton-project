param(
    [switch]$CoverageCheck,
    [switch]$VerboseTests
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

function Get-GradleTestSummary {
    param([string]$ModulePath)

    $resultsDir = Join-Path $ModulePath "build\test-results\test"
    if (-not (Test-Path $resultsDir)) {
        return $null
    }

    $summary = @{
        Total   = 0
        Passed  = 0
        Failed  = 0
        Skipped = 0
        Cases   = [System.Collections.Generic.List[string]]::new()
    }

    Get-ChildItem $resultsDir -Filter *.xml | ForEach-Object {
        [xml]$xml = Get-Content $_.FullName -Encoding UTF8
        $suite = $xml.testsuite
        if (-not $suite) { return }

        $tests = [int]$suite.tests
        $failures = [int]$suite.failures
        $errors = [int]$suite.errors
        $skipped = [int]$suite.skipped

        $summary.Total += $tests
        $summary.Failed += ($failures + $errors)
        $summary.Skipped += $skipped
        $summary.Passed += ($tests - $failures - $errors - $skipped)

        if ($VerboseTests -and $suite.testcase) {
            $className = [string]$suite.name
            foreach ($case in @($suite.testcase)) {
                $summary.Cases.Add("$className > $($case.name)")
            }
        }
    }

    if ($summary.Total -eq 0) { return $null }
    return $summary
}

function Write-GradleTestSummary {
    param(
        [string]$ModuleLabel,
        [hashtable]$Summary
    )

    if (-not $Summary) {
        Write-Host "    (sin reporte de tests; revisa errores de Gradle)" -ForegroundColor Yellow
        return
    }

    $status = if ($Summary.Failed -gt 0) { "FAIL" } else { "OK" }
    $color = if ($Summary.Failed -gt 0) { "Red" } else { "Green" }

    $line = "    [$status] $($Summary.Passed) passed"
    if ($Summary.Failed -gt 0) { $line += ", $($Summary.Failed) failed" }
    if ($Summary.Skipped -gt 0) { $line += ", $($Summary.Skipped) skipped" }
    $line += " - $ModuleLabel ($($Summary.Total) tests)"

    Write-Host $line -ForegroundColor $color

    if ($VerboseTests) {
        foreach ($case in $Summary.Cases) {
            Write-Host "      - $case"
        }
    }
}

function Get-PlainTextLines {
    param($Output)

    $lines = [System.Collections.Generic.List[string]]::new()
    foreach ($item in @($Output)) {
        if ($null -eq $item) { continue }
        if ($item -is [System.Management.Automation.ErrorRecord]) {
            if ($item.Exception -and $item.Exception.Message) {
                [void]$lines.Add([string]$item.Exception.Message)
            }
            foreach ($part in ($item.ToString() -split "`r?`n")) {
                if ($part) { [void]$lines.Add($part) }
            }
        } else {
            foreach ($part in ([string]$item -split "`r?`n")) {
                if ($part) { [void]$lines.Add($part) }
            }
        }
    }
    return $lines
}

function Get-VitestSummary {
    param([string]$JsonPath)

    if (-not (Test-Path $JsonPath)) {
        return $null
    }

    $report = Get-Content $JsonPath -Raw -Encoding UTF8 | ConvertFrom-Json
    $cases = [System.Collections.Generic.List[string]]::new()

    if ($VerboseTests -and $report.testResults) {
        foreach ($fileResult in @($report.testResults)) {
            $fileName = Split-Path $fileResult.name -Leaf
            foreach ($test in @($fileResult.assertionResults)) {
                $cases.Add("$fileName > $($test.title)")
            }
        }
    }

    return @{
        Total  = [int]$report.numTotalTests
        Passed = [int]$report.numPassedTests
        Failed = [int]$report.numFailedTests
        Cases  = $cases
    }
}

function Write-VitestSummary {
    param([hashtable]$Summary)

    if (-not $Summary) {
        Write-Host "    (sin reporte JSON de Vitest)" -ForegroundColor Yellow
        return
    }

    $status = if ($Summary.Failed -gt 0) { "FAIL" } else { "OK" }
    $color = if ($Summary.Failed -gt 0) { "Red" } else { "Green" }
    Write-Host "    [$status] $($Summary.Passed) passed - frontend ($($Summary.Total) tests)" -ForegroundColor $color

    if ($VerboseTests) {
        foreach ($case in $Summary.Cases) {
            Write-Host "      - $case"
        }
    }
}

$backendTotal = 0
$backendPassed = 0
$backendFailed = 0
$moduleSummaries = @()

Write-Host "==> Ejecutando tests backend..."
foreach ($module in $backendModules) {
    $modulePath = Join-Path $Root $module
    $task = if ($CoverageCheck) { "jacocoTestCoverageVerification" } else { "test" }
    $moduleLabel = ($module -split "/")[-1]

    Write-Host "==> $module :: ./gradlew $task"
    Push-Location $modulePath
    try {
        if ($IsWindowsHost) {
            .\gradlew.bat $task --console=plain
        } else {
            ./gradlew $task --console=plain
        }
        if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

        $summary = Get-GradleTestSummary -ModulePath $modulePath
        if ($summary) {
            $backendTotal += $summary.Total
            $backendPassed += $summary.Passed
            $backendFailed += $summary.Failed
            $moduleSummaries += [pscustomobject]@{
                Module = $moduleLabel
                Total  = $summary.Total
                Passed = $summary.Passed
                Failed = $summary.Failed
            }
        }

        Write-GradleTestSummary -ModuleLabel $moduleLabel -Summary $summary
    } finally {
        Pop-Location
    }
}

Write-Host ""
Write-Host "==> Ejecutando tests frontend..."
$frontendTotal = 0
$frontendPassed = 0
$frontendFailed = 0
$frontendSummary = $null
$frontendDir = Join-Path $Root "frontend"
$vitestJsonPath = Join-Path $frontendDir "vitest-results.json"

Push-Location $frontendDir
try {
    if (-not (Test-Path "node_modules")) {
        npm install
    }

    if (Test-Path $vitestJsonPath) {
        Remove-Item $vitestJsonPath -Force
    }

    $vitestArgs = @(
        "vitest", "run",
        "--pool=threads",
        "--maxWorkers=1",
        "--fileParallelism=false",
        "--reporter=default",
        "--reporter=json",
        "--outputFile=vitest-results.json"
    )
    if ($CoverageCheck) {
        $vitestArgs += "--coverage"
    }

    $prevErrorAction = $ErrorActionPreference
    $ErrorActionPreference = 'Continue'
    try {
        $frontendOutput = & npx @vitestArgs 2>&1
    } finally {
        $ErrorActionPreference = $prevErrorAction
    }
    foreach ($line in (Get-PlainTextLines $frontendOutput)) {
        Write-Host $line
    }

    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

    $frontendSummary = Get-VitestSummary -JsonPath $vitestJsonPath
    if ($frontendSummary) {
        $frontendTotal = $frontendSummary.Total
        $frontendPassed = $frontendSummary.Passed
        $frontendFailed = $frontendSummary.Failed
    }

    Write-VitestSummary -Summary $frontendSummary
} finally {
    Pop-Location
}

Write-Host ""
Write-Host "==> Resumen de tests"
Write-Host "    Backend ($($backendModules.Count) modulos):"
foreach ($row in $moduleSummaries) {
    Write-Host ("      {0,-14} {1,3} tests ({2} passed)" -f $row.Module, $row.Total, $row.Passed)
}
Write-Host ("      {0,-14} {1,3} tests ({2} passed)" -f "TOTAL backend", $backendTotal, $backendPassed)
Write-Host ""
Write-Host "    Frontend:"
Write-Host ("      {0,-14} {1,3} tests ({2} passed)" -f "vitest", $frontendTotal, $frontendPassed)
Write-Host ""
$grandTotal = $backendTotal + $frontendTotal
$grandPassed = $backendPassed + $frontendPassed
Write-Host ("    GRAN TOTAL:   {0,3} tests ({1} passed)" -f $grandTotal, $grandPassed) -ForegroundColor Cyan
Write-Host ""
Write-Host "==> Flujo de tests finalizado."
