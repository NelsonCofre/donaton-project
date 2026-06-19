# API Gateway (KrakenD)

Punto de entrada HTTP para el navegador. Enruta **solo** hacia el BFF; no valida JWT (eso lo hace el BFF).

## Flujo

```
Frontend → api-gateway:8090 → bff-service:8080 → microservicios
```

## Endpoints expuestos

| Ruta | Método | Notas |
|------|--------|-------|
| `/api/auth/login` | POST | Rate limit por IP |
| `/api/auth/register` | POST | Rate limit por IP |
| `/api/donations` | GET, POST | JWT en BFF |
| `/api/donations/{id}` | GET, PUT, DELETE | JWT en BFF |
| `/api/v1/necessities` | GET, POST | JWT en BFF |
| `/api/v1/necessities/{id}` | GET, PUT, DELETE | JWT en BFF |
| `/api/v1/logistics/{resource}` | GET, POST | JWT en BFF |
| `/api/v1/logistics/{resource}/{id}` | GET, PUT, DELETE | JWT en BFF |

## Docker Compose

```powershell
docker compose up --build api-gateway
```

URL pública: `http://localhost:8090`

## Kubernetes

Imagen: `donaton/api-gateway:latest` — ver `scripts/build-k8s-images.ps1`.

NodePort: **30090** — `http://localhost:30090`

## Configuración

Editar `config/krakend.json` y reconstruir la imagen.
