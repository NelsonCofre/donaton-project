# Kubernetes — Donations

Manifiestos del **microservicio de donaciones** y su **PostgreSQL** dedicado.

| Recurso | Nombre en el cluster |
|---------|----------------------|
| Base de datos | `postgres-donation` (StatefulSet + PVC) |
| Microservicio | `donation-service` (Deployment, puerto 8082) |

Despliegue completo: [docs/KUBERNETES.md](../../../docs/KUBERNETES.md).
