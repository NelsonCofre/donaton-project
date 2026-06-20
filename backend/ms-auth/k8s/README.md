# Kubernetes — Donaton

Manifiestos del **microservicio de autenticación** y su **PostgreSQL** dedicado.

| Recurso | Nombre en el cluster |
|---------|----------------------|
| Base de datos | `postgres-auth` (StatefulSet + PVC) |
| Microservicio | `auth-service` (Deployment, puerto 8081) |

Aplicar solo esta pieza:

```bash
kubectl apply -f k8s.yaml
kubectl apply -f backend/ms-auth/k8s/auth-service.yaml
```

Despliegue completo: ver [docs/KUBERNETES.md](../../../docs/KUBERNETES.md).
