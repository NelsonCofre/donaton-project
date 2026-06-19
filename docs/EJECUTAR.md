# Cómo ejecutar Donaton (Kubernetes)

Guía paso a paso para levantar y comprobar el proyecto en **Docker Desktop + Kubernetes**.

---

## ¿Está funcional?

**Sí**, para el flujo principal:

```
Navegador → Frontend → API Gateway (KrakenD) → BFF → auth-service / donation-service
```

| Funcionalidad | Estado |
|---------------|--------|
| Registro e inicio de sesión | ✅ |
| JWT validado en el BFF | ✅ |
| CRUD de donaciones (vía gateway) | ✅ |
| API Gateway (KrakenD): CORS, rate limit, enrutado | ✅ |
| logistics-service / necessity-service en el cluster | ✅ desplegados |
| logistics / necessity integrados en el BFF o gateway | ❌ pendiente |

---

## Requisitos previos

1. **Docker Desktop** instalado y en ejecución.
2. **Kubernetes habilitado** en Docker Desktop:
   - *Settings* → *Kubernetes* → marcar **Enable Kubernetes** → *Apply & Restart*.
3. **PowerShell** (o terminal) abierto en la **raíz del repositorio**:
   ```
   C:\...\donaton-project\donaton-project
   ```
4. Comprobar que el cluster responde:

```powershell
kubectl cluster-info
kubectl get nodes
```

Si aparece `connection refused` en el puerto `6443`, Kubernetes no está activo: vuelve al paso 2.

---

## Paso 1 — Construir imágenes Docker

Kubernetes **no compila** el código. Primero hay que crear las imágenes locales:

```powershell
.\scripts\build-k8s-images.ps1
```

Esto ejecuta `docker build` para:

| Imagen | Componente |
|--------|------------|
| `donaton/ms-auth:latest` | Autenticación |
| `donaton/ms-donation:latest` | Donaciones |
| `donaton/ms-logistic:latest` | Logística |
| `donaton/ms-necessity:latest` | Necesidades |
| `donaton/bff:latest` | BFF |
| `donaton/api-gateway:latest` | KrakenD |
| `donaton/frontend:latest` | React (auth, donaciones, necesidades y logística apuntan a `http://localhost:30090`) |

> La primera vez puede tardar varios minutos (descarga de dependencias Gradle/npm).

---

## Paso 2 — Desplegar en Kubernetes

```powershell
.\scripts\deploy-k8s.ps1
```

Equivalente manual:

```powershell
kubectl apply -f k8s.yaml
kubectl wait --for=condition=ready pod -l app.kubernetes.io/part-of=donaton -n donaton --timeout=300s
```

Ver estado de los pods:

```powershell
kubectl get pods -n donaton
```

Todos deben quedar en **`Running`** (puede tardar 1–3 minutos la primera vez por Flyway y arranque de Java).

Si un pod falla:

```powershell
kubectl logs <nombre-del-pod> -n donaton
kubectl describe pod <nombre-del-pod> -n donaton
```

---

## Paso 3 — Abrir la aplicación

| Servicio | URL |
|----------|-----|
| **Frontend** | http://localhost:30517 |
| **API Gateway** | http://localhost:30090 |

### Prueba en el navegador

1. Abre http://localhost:30517
2. Ve a **Registrarse** → crea un usuario (contraseña mínimo 8 caracteres)
3. **Iniciar sesión**
4. Entra a **donaciones**, **necesidades** o **logística** y crea o lista registros

En DevTools → *Network*, las peticiones deben ir a `http://localhost:30090/api/...`.
No deberían aparecer llamadas directas a `30083` o `30084`; el navegador entra por KrakenD y KrakenD reenvía al BFF.

### Prueba rápida por consola

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

---

## Paso 4 — Detener y eliminar el despliegue

```powershell
.\scripts\undeploy-k8s.ps1
```

O manualmente:

```powershell
kubectl delete -f k8s.yaml
```

---

## Si cambias código o manifiestos K8s

**Código Java / React / KrakenD:**

```powershell
.\scripts\build-k8s-images.ps1
kubectl rollout restart deployment -n donaton
```

Solo el frontend (tras cambiar UI o `VITE_API_BASE_URL`):

```powershell
docker build -t donaton/frontend:latest --build-arg "VITE_API_BASE_URL=http://localhost:30090" ./frontend
kubectl rollout restart deployment/frontend -n donaton
```

**Archivos en `*/k8s/*.yaml`:**

```powershell
.\scripts\build-k8s-yaml.ps1
kubectl apply -f k8s.yaml
```

---

## Solución de problemas

| Problema | Causa probable | Qué hacer |
|----------|----------------|-----------|
| `connection refused` en `6443` | K8s apagado | Activar Kubernetes en Docker Desktop |
| `ImagePullBackOff` | Imagen no construida | `.\scripts\build-k8s-images.ps1` |
| Pod `CrashLoopBackOff` | BD no lista o error Flyway | `kubectl logs <pod> -n donaton` |
| Frontend no llama a la API | URL incorrecta en build | Rebuild frontend con `VITE_API_BASE_URL=http://localhost:30090` |
| Gateway devuelve 500 en login/401 | Imagen gateway antigua | Rebuild `donaton/api-gateway:latest` y reiniciar deployment |
| Pods tardan mucho | Arranque normal de Spring + Postgres | Esperar 2–3 min; `kubectl get pods -n donaton -w` |

---

## Arquitectura en el cluster

```
Namespace: donaton

  frontend:30517  →  UI React
  api-gateway:30090  →  KrakenD  →  bff-service (interno)
                                      ├── auth-service:8081
                                      └── donation-service:8082
```

Los microservicios **no** exponen 8081/8082 al host; el acceso público es solo por el **gateway** (30090) y el **frontend** (30517).

---

## Comandos de referencia

```powershell
# Estado general
kubectl get pods,svc -n donaton

# Logs en vivo
kubectl logs -f deployment/api-gateway -n donaton
kubectl logs -f deployment/bff-service -n donaton

# Regenerar manifiesto raíz
.\scripts\build-k8s-yaml.ps1
```

Documentación ampliada: [KUBERNETES.md](KUBERNETES.md)
