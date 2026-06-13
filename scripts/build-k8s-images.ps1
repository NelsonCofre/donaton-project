# Construye todas las imágenes Docker usadas por Kubernetes (Docker Desktop).
# Ejecutar desde la raíz del repositorio:
#   .\scripts\build-k8s-images.ps1

$ErrorActionPreference = "Stop"
$Root = Split-Path -Parent $PSScriptRoot

$BffUrl = if ($env:VITE_API_BASE_URL) { $env:VITE_API_BASE_URL } else { "http://localhost:30080" }

Write-Host "==> Construyendo imágenes Donaton para Kubernetes..." -ForegroundColor Cyan
Write-Host "    VITE_API_BASE_URL = $BffUrl" -ForegroundColor DarkGray

$images = @(
    @{ Name = "donaton/ms-auth:latest";       Context = "backend/ms-auth" },
    @{ Name = "donaton/ms-donation:latest";   Context = "backend/ms-donation" },
    @{ Name = "donaton/ms-logistic:latest";   Context = "backend/ms-logistic" },
    @{ Name = "donaton/ms-necessity:latest";  Context = "backend/ms-necessity" },
    @{ Name = "donaton/bff:latest";           Context = "backend/bff" }
)

foreach ($img in $images) {
    $ctx = Join-Path $Root $img.Context
    Write-Host "`n==> docker build -t $($img.Name) $ctx" -ForegroundColor Yellow
    docker build -t $img.Name $ctx
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
}

Write-Host "`n==> docker build -t donaton/frontend:latest (VITE_API_BASE_URL=$BffUrl)" -ForegroundColor Yellow
docker build -t donaton/frontend:latest --build-arg "VITE_API_BASE_URL=$BffUrl" (Join-Path $Root "frontend")
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host "`nImágenes listas:" -ForegroundColor Green
docker images --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}" | Select-String "donaton/"
