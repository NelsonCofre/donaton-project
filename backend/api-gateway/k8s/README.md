# API Gateway (KrakenD)

| Recurso | Descripción |
|---------|-------------|
| Deployment | `api-gateway` — imagen `donaton/api-gateway:latest` |
| Service | `api-gateway` (NodePort **30090**, puerto 8090) |

Proxy interno hacia `http://bff-service:8080`. El frontend debe compilarse con `VITE_API_BASE_URL=http://localhost:30090`.
