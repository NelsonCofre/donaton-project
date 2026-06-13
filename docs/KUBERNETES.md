# Despliegue con Kubernetes (Docker Desktop)

Guía para ejecutar **Donaton** en Kubernetes usando **Docker Desktop**, manteniendo también **Docker Compose** para desarrollo local.

---

## Arquitectura en el cluster

```
Namespace: donaton
├── postgres-auth          → auth-service:8081
├── postgres-donation      → donation-service:8082
├── postgres-logistics     → logistics-service:8084
├── postgres-necessity     → necessity-service:8083
├── bff-service:8080       → expuesto en localhost:30080
└── frontend:80            → expuesto en localhost:30517
```

El BFF llama a los microservicios por DNS interno (`http://auth-service:8081`), igual que en `docker-compose.yml`.

---

## Estructura de archivos

| Ubicación | Contenido |
|-----------|-----------|
| **`k8s.yaml`** (raíz) | **Manifiesto único** — despliega todo el stack |
| **`docker-compose.yml`** (raíz) | Entorno Docker Compose (desarrollo) |
| **`frontend/k8s/`** | Deployment + Service del frontend |
| **`backend/bff/k8s/`** | Deployment + Service del BFF |
| **`backend/ms-auth/k8s/`** | PostgreSQL + auth-service |
| **`backend/ms-donation/k8s/`** | PostgreSQL + donation-service |
| **`backend/ms-logistic/k8s/`** | PostgreSQL + logistics-service |
| **`backend/ms-necessity/k8s/`** | PostgreSQL + necessity-service |
| **`scripts/build-k8s-images.ps1`** | Construye imágenes `donaton/*` |
| **`scripts/deploy-k8s.ps1`** | Aplica todos los manifiestos en orden |
| **`scripts/undeploy-k8s.ps1`** | Elimina el namespace completo |

Cada pieza de software tiene su carpeta **`k8s/`** con un YAML autocontenido (BD + app cuando aplica).

---

## Requisitos

1. **Docker Desktop** instalado.
2. **Kubernetes habilitado** en Docker Desktop:
   - Settings → Kubernetes → **Enable Kubernetes** → Apply & Restart.
3. **kubectl** disponible (incluido con Docker Desktop).
4. Verificar:

```powershell
kubectl cluster-info
kubectl get nodes
```

---

## Paso 1 — Construir imágenes Docker

Kubernetes usa las mismas imágenes que construyes con Dockerfile. Desde la **raíz del repo**:

```powershell
.\scripts\build-k8s-images.ps1
```

Esto genera:

| Imagen | Origen |
|--------|--------|
| `donaton/ms-auth:latest` | `backend/ms-auth` |
| `donaton/ms-donation:latest` | `backend/ms-donation` |
| `donaton/ms-logistic:latest` | `backend/ms-logistic` |
| `donaton/ms-necessity:latest` | `backend/ms-necessity` |
| `donaton/bff:latest` | `backend/bff` |
| `donaton/frontend:latest` | `frontend` (con `VITE_API_BASE_URL=http://localhost:30080`) |

> Docker Desktop comparte el daemon de Docker con Kubernetes: las imágenes locales están disponibles con `imagePullPolicy: IfNotPresent`.

---

## Paso 2 — Desplegar en Kubernetes (un solo comando)

```powershell
kubectl apply -f k8s.yaml
```

O con el script auxiliar (hace lo mismo + espera pods):

```powershell
.\scripts\deploy-k8s.ps1
```

El archivo **`k8s.yaml` en la raíz** incluye todo el stack. Se **genera** uniendo los YAML modulares:

```powershell
.\scripts\build-k8s-yaml.ps1
```

Edita los manifiestos en `*/k8s/*.yaml` y vuelve a ejecutar ese script antes de aplicar.

Espera a que los pods queden `Running`:

```powershell
kubectl get pods -n donaton -w
```

---

## Paso 3 — Acceder a la aplicación

| Servicio | URL |
|----------|-----|
| **Frontend** | http://localhost:30517 |
| **BFF** | http://localhost:30080 |
| **Logistics API** (directo) | http://localhost:30084 |
| **Necessity API** (directo) | http://localhost:30083 |

Flujo de prueba:

1. Abrir http://localhost:30517  
2. Registrar usuario / iniciar sesión  
3. Gestionar donaciones (BFF → auth + donation)  

---

## Comandos útiles

```powershell
# Estado general
kubectl get all -n donaton

# Logs del BFF
kubectl logs -f deployment/bff-service -n donaton

# Logs de un microservicio
kubectl logs -f deployment/auth-service -n donaton

# Describir un pod con error
kubectl describe pod <nombre-pod> -n donaton

# Reiniciar un deployment tras rebuild de imagen
kubectl rollout restart deployment/bff-service -n donaton
```

---

## Actualizar tras cambios en el código

```powershell
# 1. Reconstruir la imagen afectada (ejemplo: BFF)
docker build -t donaton/bff:latest ./backend/bff

# 2. Reiniciar el deployment
kubectl rollout restart deployment/bff-service -n donaton
```

Si cambias el frontend, reconstruye con el script completo (necesita el build-arg de la API):

```powershell
.\scripts\build-k8s-images.ps1
kubectl rollout restart deployment/frontend -n donaton
```

---

## Eliminar el despliegue

```powershell
.\scripts\undeploy-k8s.ps1
```

Elimina el namespace `donaton` y todos sus recursos (pods, services, PVCs).

---

## Docker Compose vs Kubernetes

| | Docker Compose | Kubernetes |
|---|----------------|------------|
| **Comando** | `docker compose up --build` | `.\scripts\build-k8s-images.ps1` + `.\scripts\deploy-k8s.ps1` |
| **Frontend** | http://localhost:5173 | http://localhost:30517 |
| **BFF** | http://localhost:8080 | http://localhost:30080 |
| **BD** | Volúmenes Compose | PVC + StatefulSet |
| **Uso** | Desarrollo diario | Evaluación / informe K8s |

Ambos conviven en el repo; no es necesario eliminar Compose.

---

## Despliegue por pieza (opcional)

Si solo quieres aplicar un microservicio (tras `kubectl apply -f k8s.yaml` para el namespace):

```powershell
kubectl apply -f backend/ms-auth/k8s/auth-service.yaml
```

---

## Solución de problemas

| Problema | Causa probable | Solución |
|----------|----------------|----------|
| `ErrImageNeverPull` / `ImagePullBackOff` | Imagen no construida | Ejecutar `.\scripts\build-k8s-images.ps1` |
| Pod `CrashLoopBackOff` en MS | BD no lista o Flyway falla | `kubectl logs <pod> -n donaton`; esperar postgres |
| Frontend no llama al BFF | URL incorrecta en build | Rebuild frontend con `VITE_API_BASE_URL=http://localhost:30080` |
| CORS en login | Origen distinto | BFF usa `http://localhost:30517` en el manifiesto K8s |
| PVC `Pending` | StorageClass | Docker Desktop crea `hostpath` por defecto; reiniciar K8s en Settings |

---

## Puertos NodePort (referencia)

Los NodePort deben estar entre **30000–32767**. Equivalencias con Compose:

| Compose (host) | Kubernetes (NodePort) |
|----------------|------------------------|
| 5173 (frontend) | 30517 |
| 8080 (bff) | 30080 |
| 8083 (necessity) | 30083 |
| 8084 (logistics) | 30084 |

Auth (8081) y donation (8082) son **ClusterIP** (solo acceso interno), igual que en producción detrás del BFF.
