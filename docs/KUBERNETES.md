# Kubernetes — referencia técnica Donaton

Documentación de arquitectura, archivos y operación del despliegue en **Docker Desktop Kubernetes**.

> **Guía paso a paso para levantar el proyecto:** [EJECUTAR.md](EJECUTAR.md) · Índice: [README.md](README.md)

---

## Resumen del flujo de despliegue

```text
┌─────────────────────────────────────────────────────────────────┐
│  Tu PC (Docker Desktop)                                         │
│                                                                 │
│  1. .\scripts\build-k8s-images.ps1                              │
│     → docker build → imágenes donaton/* en el daemon local      │
│                                                                 │
│  2. .\scripts\deploy-k8s.ps1                                    │
│     → kubectl apply -f k8s.yaml → pods + services + PVCs        │
│                                                                 │
│  3. Navegador → localhost:30517 / localhost:30090               │
└─────────────────────────────────────────────────────────────────┘
```

**Importante:** `docker compose up` levanta contenedores en Docker Engine, **no** despliega en Kubernetes.

---

## Arquitectura en el cluster

```text
Namespace: donaton

  [Navegador]
       │
       ├─► frontend:30517 (NodePort)          UI React
       │
       └─► api-gateway:30090 (NodePort)       KrakenD
                 │
                 ▼
            bff-service:8080 (ClusterIP)     JWT + orquestación
                 │
     ┌───────────┼───────────┬───────────────┐
     ▼           ▼           ▼               ▼
 auth-service  donation-   necessity-    logistics-
 :8081         service     service       service
 (ClusterIP)   :8082       :8083         :8084
     │           │           │               │
     ▼           ▼           ▼               ▼
 postgres-    postgres-   postgres-     postgres-
 auth         donation    necessity     logistics
```

### Responsabilidades por capa

| Componente | Rol |
|------------|-----|
| **Frontend** | SPA React; llama solo al gateway (`:30090`) |
| **KrakenD** | Enrutamiento, CORS, rate limit; **no** valida JWT |
| **BFF** | Adapta contratos UI ↔ microservicios; valida JWT vía `ms-auth` |
| **Microservicios** | Lógica de dominio; cada uno con su PostgreSQL |
| **PostgreSQL** | Una BD por microservicio (StatefulSet + PVC) |

### Rutas expuestas por KrakenD

| Prefijo | Destino | JWT |
|---------|---------|-----|
| `/api/auth/login` | BFF → ms-auth | No |
| `/api/auth/register` | BFF → ms-auth | No |
| `/api/donations/*` | BFF → ms-donation | Sí (BFF) |
| `/api/v1/necessities/*` | BFF → ms-necessity | Sí (BFF) |
| `/api/v1/logistics/*` | BFF → ms-logistic | Sí (BFF) |

---

## Puertos y acceso

### Puertos públicos (desde el navegador / host)

| Servicio | NodePort | URL |
|----------|----------|-----|
| Frontend | 30517 | http://localhost:30517 |
| API Gateway | 30090 | http://localhost:30090 |
| **BFF (Swagger)** | **30080** | http://localhost:30080/swagger-ui/index.html |
| **Auth (Swagger)** | **30081** | http://localhost:30081/swagger-ui/index.html |
| **Donations (Swagger)** | **30082** | http://localhost:30082/swagger-ui/index.html |
| **Necessities (Swagger)** | **30083** | http://localhost:30083/swagger-ui/index.html |
| **Logistics (Swagger)** | **30084** | http://localhost:30084/swagger-ui/index.html |

Los NodePort `30080`–`30084` son para **Swagger / depuración directa** del backend. **El flujo normal de la app** sigue siendo: navegador → frontend `:30517` → gateway `:30090` → BFF (interno). Guía: [SWAGGER.md](SWAGGER.md).

### Puertos internos (ClusterIP / targetPort)

| Servicio | Puerto contenedor |
|----------|-------------------|
| BFF | 8080 |
| auth-service | 8081 |
| donation-service | 8082 |
| necessity-service | 8083 |
| logistics-service | 8084 |

### Equivalencia Docker Compose ↔ Kubernetes

| Compose (host) | Kubernetes (NodePort) |
|----------------|------------------------|
| 5173 (frontend) | 30517 |
| 8090 (api-gateway) | 30090 |
| 8080 (BFF / Swagger) | 30080 |
| 8081 (auth / Swagger) | 30081 |
| 8082 (donation / Swagger) | 30082 |
| 8083 (necessity / Swagger) | 30083 |
| 8084 (logistics / Swagger) | 30084 |

---

## Estructura de archivos

| Ubicación | Contenido |
|-----------|-----------|
| **`k8s.yaml`** (raíz) | Manifiesto único generado — despliega todo |
| **`scripts/build-k8s-images.ps1`** | Construye imágenes `donaton/*` |
| **`scripts/build-k8s-yaml.ps1`** | Regenera `k8s.yaml` desde piezas modulares |
| **`scripts/deploy-k8s.ps1`** | `kubectl apply` + espera pods |
| **`scripts/undeploy-k8s.ps1`** | Elimina el despliegue |
| **`scripts/test-all.ps1`** | Tests (no requiere K8s) |
| **`frontend/k8s/frontend.yaml`** | Deployment + Service frontend |
| **`backend/bff/k8s/bff-service.yaml`** | BFF (ClusterIP) |
| **`backend/api-gateway/k8s/api-gateway.yaml`** | KrakenD |
| **`backend/ms-auth/k8s/`** | PostgreSQL + auth-service |
| **`backend/ms-donation/k8s/`** | PostgreSQL + donation-service |
| **`backend/ms-necessity/k8s/`** | PostgreSQL + necessity-service |
| **`backend/ms-logistic/k8s/`** | PostgreSQL + logistics-service |

### Regenerar `k8s.yaml`

Si editas cualquier archivo en `*/k8s/*.yaml`:

```powershell
.\scripts\build-k8s-yaml.ps1
kubectl apply -f k8s.yaml
```

El script concatena los YAML con separadores `---` y valida con `kubectl apply --dry-run=client`.

---

## Scripts — detalle

### `build-k8s-images.ps1`

Construye 7 imágenes en orden:

1. ms-auth, ms-donation, ms-logistic, ms-necessity, bff, api-gateway
2. frontend (con build-args de Vite)

Variables de entorno opcionales:

| Variable | Default |
|----------|---------|
| `VITE_API_BASE_URL` | `http://localhost:30090` |
| `VITE_NECESSITY_API_BASE_URL` | igual que `VITE_API_BASE_URL` |
| `VITE_LOGISTICS_API_BASE_URL` | igual que `VITE_API_BASE_URL` |

Las imágenes usan `imagePullPolicy: IfNotPresent` — Kubernetes usa las imágenes locales de Docker Desktop.

### `deploy-k8s.ps1`

1. `kubectl apply -f k8s.yaml`
2. `kubectl wait` hasta 300 s por pods con label `app.kubernetes.io/part-of=donaton`
3. Muestra pods y services

### `undeploy-k8s.ps1`

1. `kubectl delete -f k8s.yaml`
2. `kubectl delete namespace donaton` (limpieza por si quedó a medias)

---

## Actualizar tras cambios

### Código de aplicación

```powershell
# Rebuild completo (recomendado)
.\scripts\build-k8s-images.ps1
kubectl rollout restart deployment -n donaton
```

Rebuild parcial de un servicio:

```powershell
docker build -t donaton/bff:latest ./backend/bff
kubectl rollout restart deployment/bff-service -n donaton
```

Frontend (siempre requiere rebuild por variables Vite embebidas en build):

```powershell
.\scripts\build-k8s-images.ps1
kubectl rollout restart deployment/frontend -n donaton
```

### Manifiestos K8s

```powershell
.\scripts\build-k8s-yaml.ps1
kubectl apply -f k8s.yaml
```

---

## Comandos operativos

```powershell
# Estado
kubectl get all -n donaton
kubectl get pods -n donaton -w

# Logs
kubectl logs -f deployment/api-gateway -n donaton
kubectl logs -f deployment/bff-service -n donaton
kubectl logs -f deployment/auth-service -n donaton
kubectl logs -f deployment/necessity-service -n donaton
kubectl logs -f deployment/logistics-service -n donaton

# Entrar a un pod (depuración)
kubectl exec -it deployment/bff-service -n donaton -- sh

# Reiniciar un deployment
kubectl rollout restart deployment/bff-service -n donaton
kubectl rollout status deployment/bff-service -n donaton

# Describir error
kubectl describe pod <nombre> -n donaton
```

---

## Despliegue por pieza (opcional)

Tras tener el namespace creado (`kubectl apply -f k8s.yaml` al menos una vez):

```powershell
kubectl apply -f backend/ms-auth/k8s/auth-service.yaml
kubectl apply -f backend/bff/k8s/bff-service.yaml
```

Útil para probar cambios aislados en un microservicio.

---

## Solución de problemas

| Problema | Causa probable | Solución |
|----------|----------------|----------|
| `ErrImageNeverPull` / `ImagePullBackOff` | Imagen no existe localmente | `.\scripts\build-k8s-images.ps1` |
| Pod `CrashLoopBackOff` | Postgres no listo o Flyway falló | `kubectl logs <pod> -n donaton`; esperar postgres |
| `connection refused` :6443 | Cluster K8s apagado | Enable Kubernetes en Docker Desktop |
| Frontend llama a URL incorrecta | Build sin `VITE_*` correctos | Rebuild con script (usa `:30090`) |
| CORS en login | Origen no permitido | KrakenD permite `localhost:30517` y `5173` |
| PVC `Pending` | StorageClass | Docker Desktop usa `hostpath` por defecto |
| Cambios UI no aparecen | Pod con imagen antigua | Rebuild frontend + rollout restart |
| 401 en API protegida | Token ausente/expirado | Login de nuevo; verificar header Bearer |
| Gateway 500 en 401 | Gateway sin `output_encoding: no-op` | Rebuild api-gateway |

---

## Docker Compose vs Kubernetes

| Aspecto | Docker Compose | Kubernetes |
|---------|----------------|------------|
| Comando | `docker compose up --build` | `build-k8s-images.ps1` + `deploy-k8s.ps1` |
| Orquestación | Contenedores en Docker Engine | Pods, Services, StatefulSets |
| Red | Red bridge Compose | DNS interno del cluster |
| Persistencia | Volúmenes Compose | PVC + StatefulSet |
| Escalado | Manual | Deployments (réplicas) |
| Uso en el repo | Dev local rápido | **Método principal / evaluación** |

Ambos conviven; no es necesario eliminar Compose.

---

## Relacionado

| Documento | Contenido |
|-----------|-----------|
| [README.md](README.md) | Índice de guías |
| [EJECUTAR.md](EJECUTAR.md) | Correr la app con Kubernetes |
| [SWAGGER.md](SWAGGER.md) | Usar Swagger UI / OpenAPI |
| [LOGGING.md](LOGGING.md) | Ver y probar logs |
| [TESTING.md](TESTING.md) | Ejecutar tests |
