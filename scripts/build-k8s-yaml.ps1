# Regenera k8s.yaml en la raiz uniendo los manifiestos modulares con separadores ---.
# Ejecutar tras editar cualquier archivo en */k8s/*.yaml:
#   .\scripts\build-k8s-yaml.ps1

$ErrorActionPreference = "Stop"
$Root = Split-Path -Parent $PSScriptRoot

$header = @"
# =============================================================================
# Donaton - Manifiesto Kubernetes (raiz)
# =============================================================================
# Despliega TODO el stack con un solo comando:
#   kubectl apply -f k8s.yaml
#
# Generado por scripts/build-k8s-yaml.ps1 — no editar a mano; editar */k8s/*.yaml
# =============================================================================

apiVersion: v1
kind: Namespace
metadata:
  name: donaton
  labels:
    app.kubernetes.io/part-of: donaton

---
apiVersion: v1
kind: Secret
metadata:
  name: donaton-shared-secret
  namespace: donaton
  labels:
    app.kubernetes.io/part-of: donaton
type: Opaque
stringData:
  jwt-secret: default-local-auth-secret-minimum-32-characters-long!!

"@

$pieceFiles = @(
    "backend/ms-auth/k8s/auth-service.yaml",
    "backend/ms-donation/k8s/donation-service.yaml",
    "backend/ms-logistic/k8s/logistics-service.yaml",
    "backend/ms-necessity/k8s/necessity-service.yaml",
    "backend/bff/k8s/bff-service.yaml",
    "frontend/k8s/frontend.yaml"
)

$chunks = @($header.TrimEnd())
foreach ($rel in $pieceFiles) {
    $path = Join-Path $Root $rel
    if (-not (Test-Path $path)) {
        throw "No se encontro: $path"
    }
    $chunks += (Get-Content $path -Raw).Trim()
}

$output = ($chunks -join "`n---`n") + "`n"
$outPath = Join-Path $Root "k8s.yaml"
[System.IO.File]::WriteAllText($outPath, $output)

Write-Host "Generado: k8s.yaml" -ForegroundColor Green
kubectl apply -f $outPath --dry-run=client | Select-Object -Last 8
