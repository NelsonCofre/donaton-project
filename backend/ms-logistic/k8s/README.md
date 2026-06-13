# Kubernetes — Logistics

Manifiestos del **microservicio de logística** y su **PostgreSQL** dedicado.

| Recurso | Nombre en el cluster |
|---------|----------------------|
| Base de datos | `postgres-logistics` (StatefulSet + PVC) |
| Microservicio | `logistics-service` (NodePort **30084**, puerto 8084) |

Despliegue completo: [docs/KUBERNETES.md](../../../docs/KUBERNETES.md).
