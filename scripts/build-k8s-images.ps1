# Construye todas las imágenes Docker usadas por Kubernetes (Docker Desktop).
# Ejecutar desde la raíz del repositorio:
#   .\scripts\build-k8s-images.ps1

$ErrorActionPreference = "Stop"
$Root = Split-Path -Parent $PSScriptRoot

$ApiUrl = if ($env:VITE_API_BASE_URL) { $env:VITE_API_BASE_URL } else { "http://localhost:30090" }
$NecessityApiUrl = if ($env:VITE_NECESSITY_API_BASE_URL) { $env:VITE_NECESSITY_API_BASE_URL } else { $ApiUrl }
$LogisticsApiUrl = if ($env:VITE_LOGISTICS_API_BASE_URL) { $env:VITE_LOGISTICS_API_BASE_URL } else { $ApiUrl }

Write-Host "==> Construyendo imágenes Donaton para Kubernetes..." -ForegroundColor Cyan
Write-Host "    VITE_API_BASE_URL = $ApiUrl" -ForegroundColor DarkGray
Write-Host "    VITE_NECESSITY_API_BASE_URL = $NecessityApiUrl" -ForegroundColor DarkGray
Write-Host "    VITE_LOGISTICS_API_BASE_URL = $LogisticsApiUrl" -ForegroundColor DarkGray

$images = @(
    @{ Name = "donaton/ms-auth:latest";       Context = "backend/ms-auth" },
    @{ Name = "donaton/ms-donation:latest";   Context = "backend/ms-donation" },
    @{ Name = "donaton/ms-logistic:latest";   Context = "backend/ms-logistic" },
    @{ Name = "donaton/ms-necessity:latest";  Context = "backend/ms-necessity" },
    @{ Name = "donaton/bff:latest";           Context = "backend/bff" },
    @{ Name = "donaton/api-gateway:latest";   Context = "backend/api-gateway" }
)

foreach ($img in $images) {
    $ctx = Join-Path $Root $img.Context
    Write-Host "`n==> docker build -t $($img.Name) $ctx" -ForegroundColor Yellow
    docker build -t $img.Name $ctx
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
}

Write-Host "`n==> docker build -t donaton/frontend:latest (VITE_API_BASE_URL=$ApiUrl)" -ForegroundColor Yellow
docker build -t donaton/frontend:latest `
    --build-arg "VITE_API_BASE_URL=$ApiUrl" `
    --build-arg "VITE_NECESSITY_API_BASE_URL=$NecessityApiUrl" `
    --build-arg "VITE_LOGISTICS_API_BASE_URL=$LogisticsApiUrl" `
    (Join-Path $Root "frontend")
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host "`nImágenes listas:" -ForegroundColor Green
docker images --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}" | Select-String "donaton/"
