# Kubernetes — Necessity

Manifiestos del **microservicio de necesidades** y su **PostgreSQL** dedicado.

| Recurso | Nombre en el cluster |
|---------|----------------------|
| Base de datos | `postgres-necessity` (StatefulSet + PVC) |
| Microservicio | `necessity-service` (NodePort **30083**, puerto 8083) |

Despliegue completo: [docs/KUBERNETES.md](../../../docs/KUBERNETES.md).
