# Kubernetes — Frontend

Manifiestos del **cliente React** (Nginx).

| Recurso | Nombre en el cluster |
|---------|----------------------|
| Frontend | `frontend` (NodePort **30517**, puerto 80) |

La imagen debe compilarse con `VITE_API_BASE_URL=http://localhost:30090` (ver `scripts/build-k8s-images.ps1`).

Despliegue completo: [docs/KUBERNETES.md](../../docs/KUBERNETES.md).
