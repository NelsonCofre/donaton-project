# Cómo ejecutar Donaton con Kubernetes

Guía paso a paso para **levantar, probar y detener** el proyecto en **Docker Desktop + Kubernetes**.

> Documentación técnica ampliada: [KUBERNETES.md](KUBERNETES.md)  
> Tests del proyecto: [TESTING.md](TESTING.md)

---

## Inicio rápido (3 comandos)

Desde la **raíz del repositorio** (`donaton-project/`):

```powershell
# 1. Construir imágenes Docker locales
.\scripts\build-k8s-images.ps1

# 2. Desplegar en el cluster
.\scripts\deploy-k8s.ps1

# 3. Abrir en el navegador
start http://localhost:30517
```

| Servicio | URL |
|----------|-----|
| **Frontend** | http://localhost:30517 |
| **API Gateway (KrakenD)** | http://localhost:30090 |

> **¿Acabas de abrir Docker Desktop?** No hace falta rebuild ni redeploy. Ve directo a [Verificar pods](#verificar-pods-cada-vez-que-abres-docker-desktop), espera a que estén `Running` y luego abre el frontend.

---

## Verificar pods (cada vez que abres Docker Desktop)

Si ya desplegaste el proyecto antes, **no necesitas** volver a ejecutar `build-k8s-images.ps1` ni `deploy-k8s.ps1` solo por abrir Docker Desktop. Las imágenes y el despliegue se conservan.

Lo que sí debes hacer es **comprobar que el cluster y los pods estén listos** antes de usar la app (el arranque puede tardar **1–3 minutos**):

```powershell
# 1. Cluster activo
kubectl cluster-info
kubectl get nodes

# 2. Estado de pods y services
kubectl get pods -n donaton
kubectl get pods,svc -n donaton

# 3. Ver en tiempo real hasta que todos queden Running
kubectl get pods -n donaton -w
```

Todos los pods deben mostrar **`Running`** y **`1/1`** en READY. Presta especial atención a:

- `auth-service`
- `bff-service`
- `api-gateway`
- `frontend`

Cuando estén `Running`, espera **1 minuto más** (Spring Boot termina de inicializar) y abre:

```powershell
start http://localhost:30517
```

### Si login o registro devuelven 500

Suele ocurrir si entras al frontend **antes** de que `auth-service` y `bff-service` terminen de arrancar. Espera y vuelve a intentar.

### Si un pod no arranca o sigue reiniciando

```powershell
kubectl logs <nombre-del-pod> -n donaton
kubectl describe pod <nombre-del-pod> -n donaton

# Logs de los servicios clave
kubectl logs deployment/auth-service -n donaton --tail=30
kubectl logs deployment/bff-service -n donaton --tail=30
kubectl logs deployment/api-gateway -n donaton --tail=30
```

Solo vuelve a desplegar si el namespace no existe o borraste el despliegue:

```powershell
.\scripts\deploy-k8s.ps1
```

---

## ¿Qué hace cada paso?

Kubernetes **no compila** tu código. El flujo es:

```text
build-k8s-images.ps1  →  docker build (crea imágenes donaton/* en tu PC)
deploy-k8s.ps1        →  kubectl apply (crea pods, services, BD en K8s)
```

```text
Navegador
   ↓
Frontend (30517)
   ↓  peticiones API
API Gateway / KrakenD (30090)
   ↓  enruta al BFF
BFF (interno, ClusterIP)
   ↓  valida JWT vía ms-auth
   ├── auth-service
   ├── donation-service
   ├── necessity-service
   └── logistics-service
```

---

## Requisitos previos

### 1. Software

| Requisito | Para qué |
|-----------|----------|
| **Docker Desktop** | Motor Docker + cluster Kubernetes |
| **Kubernetes habilitado** | Settings → Kubernetes → **Enable Kubernetes** → Apply & Restart |
| **kubectl** | Viene con Docker Desktop |
| **PowerShell** | Ejecutar los scripts del repo |

### 2. Verificar que el cluster está activo

```powershell
kubectl cluster-info
kubectl get nodes
```

Debe mostrar un nodo en estado `Ready`.

Si aparece `connection refused` en el puerto `6443`:

1. Abre Docker Desktop.
2. Espera a que el icono deje de parpadear.
3. Confirma que Kubernetes está en verde en Settings.
4. Vuelve a ejecutar `kubectl get nodes`.

### 3. Ubicación correcta

Todos los comandos se ejecutan desde la raíz del repo:

```text
C:\...\donaton-project\donaton-project\
```

Ahí deben existir `k8s.yaml`, `scripts/` y las carpetas `backend/` y `frontend/`.

---

## Paso 1 — Construir imágenes Docker

```powershell
.\scripts\build-k8s-images.ps1
```

Construye estas imágenes locales:

| Imagen | Componente |
|--------|------------|
| `donaton/ms-auth:latest` | Autenticación + JWT |
| `donaton/ms-donation:latest` | Donaciones |
| `donaton/ms-necessity:latest` | Necesidades |
| `donaton/ms-logistic:latest` | Logística |
| `donaton/bff:latest` | Backend for Frontend |
| `donaton/api-gateway:latest` | KrakenD |
| `donaton/frontend:latest` | React (URLs apuntan a `:30090`) |

La **primera vez** puede tardar **5–15 minutos** (Gradle, npm, capas Docker).

Verificar imágenes creadas:

```powershell
docker images | Select-String "donaton/"
```

### Variables opcionales del frontend

Por defecto el script usa `http://localhost:30090` para todas las APIs del frontend:

```powershell
$env:VITE_API_BASE_URL = "http://localhost:30090"
$env:VITE_NECESSITY_API_BASE_URL = "http://localhost:30090"
$env:VITE_LOGISTICS_API_BASE_URL = "http://localhost:30090"
.\scripts\build-k8s-images.ps1
```

---

## Paso 2 — Desplegar en Kubernetes

```powershell
.\scripts\deploy-k8s.ps1
```

El script:

1. Ejecuta `kubectl apply -f k8s.yaml`
2. Espera hasta 5 minutos a que los pods queden `Ready`
3. Muestra el estado de pods y services

Equivalente manual:

```powershell
kubectl apply -f k8s.yaml
kubectl wait --for=condition=ready pod -l app.kubernetes.io/part-of=donaton -n donaton --timeout=300s
kubectl get pods,svc -n donaton
```

### Estado esperado

Todos los pods en namespace `donaton` deben quedar **`Running`**:

```powershell
kubectl get pods -n donaton
```

Ejemplo:

```text
NAME                              READY   STATUS    RESTARTS   AGE
api-gateway-...                   1/1     Running   0          2m
auth-service-...                  1/1     Running   0          2m
bff-service-...                   1/1     Running   0          2m
donation-service-...              1/1     Running   0          2m
frontend-...                      1/1     Running   0          2m
logistics-service-...             1/1     Running   0          2m
necessity-service-...             1/1     Running   0          2m
postgres-auth-0                   1/1     Running   0          2m
...
```

La primera vez puede tardar **2–3 minutos** extra (PostgreSQL + Flyway + arranque de Spring Boot).

### Si un pod falla

```powershell
kubectl logs <nombre-del-pod> -n donaton
kubectl describe pod <nombre-del-pod> -n donaton

kubectl logs deployment/auth-service -n donaton --tail=30
kubectl logs deployment/bff-service -n donaton --tail=30
kubectl logs deployment/api-gateway -n donaton --tail=30
```

---

## Paso 3 — Probar la aplicación

### En el navegador

1. Abre http://localhost:30517
2. Ve a **Registrarse** → crea un usuario (contraseña mínimo 8 caracteres)
3. **Iniciar sesión**
4. Prueba las secciones:
   - **Donaciones** — CRUD
   - **Necesidades** — CRUD
   - **Logística** — centros de acopio, inventario, envíos

### Qué verificar en DevTools (F12 → Network)

- Las peticiones van a `http://localhost:30090/api/...`
- Rutas protegidas llevan header `Authorization: Bearer <token>`
- **No** deberían llamarse directamente puertos internos (`8081`, `8082`, etc.)

### Prueba rápida por consola (curl)

Sin token (debe responder **401**):

```powershell
curl.exe -s -o NUL -w "HTTP %{http_code}`n" http://localhost:30090/api/donations
```

Registro:

```powershell
curl.exe -X POST http://localhost:30090/api/auth/register `
  -H "Content-Type: application/json" `
  -d "{\"email\":\"demo@donaton.local\",\"password\":\"Demo1234!\",\"rol\":\"USER\"}"
```

Login (copia el `token` de la respuesta):

```powershell
curl.exe -X POST http://localhost:30090/api/auth/login `
  -H "Content-Type: application/json" `
  -d "{\"email\":\"demo@donaton.local\",\"password\":\"Demo1234!\"}"
```

Donaciones con token:

```powershell
curl.exe http://localhost:30090/api/donations `
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

Necesidades con token:

```powershell
curl.exe http://localhost:30090/api/v1/necessities `
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

---

## Paso 4 — Detener y eliminar el despliegue

```powershell
.\scripts\undeploy-k8s.ps1
```

Elimina todos los recursos del namespace `donaton` (pods, services, bases de datos, volúmenes).

Equivalente manual:

```powershell
kubectl delete -f k8s.yaml
kubectl delete namespace donaton --ignore-not-found
```

---

## Si cambias código o configuración

### Cambiaste código Java, React o KrakenD

```powershell
.\scripts\build-k8s-images.ps1
kubectl rollout restart deployment -n donaton
```

O reinicia solo lo que cambió:

```powershell
kubectl rollout restart deployment/bff-service deployment/api-gateway deployment/frontend -n donaton
```

> **Importante:** si cambias el frontend, debes **reconstruir la imagen** (`build-k8s-images.ps1`). Un simple restart sin rebuild seguirá sirviendo la imagen antigua.

### Cambiaste manifiestos Kubernetes (`*/k8s/*.yaml`)

```powershell
.\scripts\build-k8s-yaml.ps1
kubectl apply -f k8s.yaml
```

No edites `k8s.yaml` a mano: se **genera** uniendo los YAML modulares.

---

## Solución de problemas

| Problema | Causa probable | Qué hacer |
|----------|----------------|-----------|
| `connection refused` en `6443` | Kubernetes apagado | Activar K8s en Docker Desktop y esperar |
| Docker Desktop con ⚠️ | Docker no está corriendo bien | Reiniciar Docker Desktop |
| `ImagePullBackOff` | Imagen no construida | `.\scripts\build-k8s-images.ps1` |
| Pod `CrashLoopBackOff` | BD no lista o error Flyway | `kubectl logs <pod> -n donaton` |
| Frontend sin secciones nuevas | Imagen frontend antigua | Rebuild + `kubectl rollout restart deployment/frontend -n donaton` |
| Frontend no llama a la API | URL incorrecta en build | Rebuild con `VITE_API_BASE_URL=http://localhost:30090` |
| Gateway devuelve 500 en 401 | Config gateway antigua | Rebuild `donaton/api-gateway:latest` y reiniciar |
| 401 en necesidades/logística | Token ausente o expirado | Inicia sesión de nuevo |
| **500 en login/registro** | Backend aún arrancando tras abrir Docker Desktop | [Verificar pods](#verificar-pods-cada-vez-que-abres-docker-desktop); esperar 1–3 min |
| Pods tardan mucho | Arranque normal | Esperar 2–3 min; `kubectl get pods -n donaton -w` |
| Cambios no se ven en el navegador | Caché del navegador | Ctrl+F5 o ventana incógnito |

---

## Comandos útiles de referencia

```powershell
# Estado general
kubectl get pods,svc -n donaton

# Ver pods en tiempo real
kubectl get pods -n donaton -w

# Logs en vivo
kubectl logs -f deployment/api-gateway -n donaton
kubectl logs -f deployment/bff-service -n donaton
kubectl logs -f deployment/auth-service -n donaton

# Regenerar manifiesto raíz
.\scripts\build-k8s-yaml.ps1

# Ejecutar tests (no requiere K8s)
.\scripts\test-all.ps1
```

---

## Funcionalidades disponibles

| Funcionalidad | Estado |
|---------------|--------|
| Registro e inicio de sesión | ✅ |
| JWT validado en el BFF | ✅ |
| CRUD donaciones (vía gateway) | ✅ |
| CRUD necesidades (vía gateway) | ✅ |
| CRUD logística (vía gateway) | ✅ |
| API Gateway: CORS, rate limit, enrutado | ✅ |
| KrakenD valida JWT | ❌ (lo hace el BFF) |

---

## Docker Compose vs Kubernetes

| | Docker Compose | Kubernetes |
|---|----------------|------------|
| **Comando** | `docker compose up --build` | `build-k8s-images.ps1` + `deploy-k8s.ps1` |
| **Frontend** | http://localhost:5173 | http://localhost:30517 |
| **API Gateway** | http://localhost:8090 | http://localhost:30090 |
| **Uso recomendado** | Desarrollo local rápido | **Entrega / evaluación** |

Para la evaluación con Kubernetes, usa esta guía. Compose es opcional y no crea pods ni services de K8s.
