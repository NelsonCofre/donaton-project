# Elimina el despliegue de Donaton en Kubernetes.
# Ejecutar desde la raiz del repositorio:
#   .\scripts\undeploy-k8s.ps1

$ErrorActionPreference = "Stop"
$Root = Split-Path -Parent $PSScriptRoot

Write-Host "==> kubectl delete -f k8s.yaml" -ForegroundColor Yellow
kubectl delete -f (Join-Path $Root "k8s.yaml") --ignore-not-found --wait=true 2>$null

Write-Host "==> kubectl delete namespace donaton (por si quedo a medias)" -ForegroundColor Yellow
kubectl delete namespace donaton --ignore-not-found --wait=true

Write-Host "`nDespliegue eliminado." -ForegroundColor Green
