# Despliega Donaton en Kubernetes con un solo manifiesto raiz.
# Ejecutar desde la raiz del repositorio:
#   .\scripts\deploy-k8s.ps1
#
# Equivalente manual: kubectl apply -f k8s.yaml

$ErrorActionPreference = "Stop"
$Root = Split-Path -Parent $PSScriptRoot

Write-Host "==> kubectl apply -f k8s.yaml" -ForegroundColor Cyan
kubectl apply -f (Join-Path $Root "k8s.yaml")
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host "`n==> Esperando pods..." -ForegroundColor Yellow
kubectl wait --for=condition=ready pod -l app.kubernetes.io/part-of=donaton -n donaton --timeout=300s

Write-Host "`n==> Estado:" -ForegroundColor Green
kubectl get pods,svc -n donaton

Write-Host @"

Acceso:
  Frontend:    http://localhost:30517
  API Gateway: http://localhost:30090

Eliminar: kubectl delete -f k8s.yaml
"@ -ForegroundColor Cyan
