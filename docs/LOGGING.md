# Cómo ver los logs de Donaton

Guía para **revisar los logs** del BFF y de los microservicios tras el despliegue.

> **Otras guías:** [correr la app (K8s)](EJECUTAR.md) · [Swagger](SWAGGER.md) · [tests](TESTING.md) · [índice](README.md)

---

## Qué se registra

Los logs usan SLF4J / Logback (Spring Boot) en:

| Servicio | Ejemplos de mensajes |
|----------|----------------------|
| **bff-service** | Login/registro vía BFF, CRUD orquestado, JWT rechazado, errores upstream |
| **auth-service** | Registro, login, logout, token inválido |
| **donation-service** | Crear / actualizar / eliminar donación |
| **necessity-service** | Crear / actualizar / eliminar necesidad |
| **logistics-service** | Centros, inventario y envíos (CRUD) |

| Nivel | Significado |
|-------|-------------|
| `INFO` | Operación normal (arranque, login, create, list) |
| `WARN` | Rechazo esperado (JWT expirado, validación, 401/404) |
| `ERROR` | Error inesperado del servidor (con stack trace) |

Variables de entorno (Compose y K8s):

- `LOGGING_LEVEL_ROOT` — nivel global (por defecto `INFO`)
- `LOGGING_LEVEL_APP` — nivel del paquete `com.donaton` (por defecto `INFO`)

---

## Importante

1. Los logs **no aparecen solos**: hay que usar la app (login, CRUD, etc.) mientras el comando de logs está escuchando.
2. Si cambiaste el código de logging, debes **rebuild de imágenes** y reiniciar pods/contenedores. Sin rebuild seguirás viendo la imagen antigua.
3. **No borres la base de datos** al actualizar: evita `docker compose down -v` y `.\scripts\undeploy-k8s.ps1`.

---

## Opción A — Kubernetes (método principal)

### 1. Actualizar código de logs (si acabas de cambiar el backend)

```powershell
.\scripts\build-k8s-images.ps1
kubectl apply -f k8s.yaml
kubectl rollout restart deployment/bff-service deployment/auth-service deployment/donation-service deployment/necessity-service deployment/logistics-service -n donaton
```

Espera a que los pods estén `Running`:

```powershell
kubectl get pods -n donaton
```

### 2. Escuchar logs (deja la terminal abierta)

BFF:

```powershell
kubectl logs -f deployment/bff-service -n donaton
```

Microservicios (abre una terminal por servicio si quieres ver varios a la vez):

```powershell
kubectl logs -f deployment/auth-service -n donaton
kubectl logs -f deployment/donation-service -n donaton
kubectl logs -f deployment/necessity-service -n donaton
kubectl logs -f deployment/logistics-service -n donaton
```

Últimas líneas sin seguir en vivo:

```powershell
kubectl logs deployment/bff-service -n donaton --tail=50
```

### 3. Generar actividad desde la UI

1. Abre http://localhost:30517  
2. Inicia sesión o regístrate  
3. Crea / lista donaciones, necesidades o logística  

En la terminal del BFF deberías ver algo como:

```text
INFO  ... AuthBffService : BFF login solicitado email=...
INFO  ... AuthBffService : BFF login completado email=...
INFO  ... DonationBffService : BFF listó N donaciones
```

Un `WARN` de JWT inválido/expirado es **normal** si el navegador tenía un token viejo en sesión:

```text
WARN  ... JwtAuthFilter : JWT inválido o expirado en /api/donations: ...
```

Tras un login nuevo, las peticiones protegidas deberían pasar sin ese `WARN`.

### 4. Filtrar (PowerShell)

```powershell
kubectl logs deployment/bff-service -n donaton --tail=200 | Select-String "BFF login|BFF creó|JWT|WARN|ERROR"
kubectl logs deployment/auth-service -n donaton --tail=200 | Select-String "Login|Usuario registrado|WARN"
```

---

## Opción B — Docker Compose

### 1. Rebuild sin borrar volúmenes (DB)

```powershell
docker compose up --build -d bff-service auth-service donation-service necessity-service logistics-service
```

### 2. Ver logs

```powershell
docker compose logs -f bff-service
docker compose logs -f auth-service donation-service
```

### 3. Generar actividad

Abre http://localhost:5173 y usa login / CRUD. Los mensajes aparecerán en la terminal de `logs -f`.

Filtrar:

```powershell
docker compose logs bff-service --tail=200 | Select-String "BFF login|BFF creó|JWT"
```

---

## Checklist rápido (K8s)

1. Pods en `Running`: `kubectl get pods -n donaton`  
2. Terminal con `kubectl logs -f deployment/bff-service -n donaton`  
3. UI en http://localhost:30517 → login y una acción de negocio  
4. Confirmar líneas `INFO` del BFF (y del microservicio correspondiente si también lo estás mirando)  

---

## Problemas frecuentes

| Síntoma | Causa probable | Qué hacer |
|---------|----------------|-----------|
| Solo ves el arranque de Spring Boot | No hay requests a la API | Usa la UI o haz curl al gateway |
| No aparecen mensajes `BFF login` / `BFF creó` | Imagen antigua | Rebuild + `rollout restart` |
| `WARN` 401 JWT al abrir donaciones | Token viejo en el navegador | Vuelve a iniciar sesión |
| `Found 2 pods, using pod/...` | Normal tras restart | Elige el pod nuevo o espera a que quede 1 réplica |
| `GracefulShutdown` | Pod anterior cerrándose | Esperado tras `rollout restart` |
