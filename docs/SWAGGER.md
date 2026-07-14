# Cómo usar Swagger (Kubernetes)

Guía para probar las APIs con **Swagger UI** sobre el stack desplegado en **Kubernetes**. Cada backend expone Swagger UI y un JSON OpenAPI 3 con [springdoc-openapi](https://springdoc.org/) (`2.8.6`).

> **Requisito:** stack en K8s corriendo — [EJECUTAR.md](EJECUTAR.md)  
> **Otras guías:** [logs](LOGGING.md) · [tests](TESTING.md) · [índice](README.md)

## Dos capas de API

| Capa               | Quién la consume      | Prefijo típico                   | Dónde documentar   |
| ------------------ | --------------------- | -------------------------------- | ------------------ |
| **Frontend (BFF)** | React en el navegador | `/api/auth`, `/api/donations`, … | Swagger del BFF    |
| **Microservicios** | BFF, pruebas directas | `/api/v1/*`                      | Swagger de cada MS |

El contrato del **frontend** no es igual al de los microservicios: el BFF adapta DTOs y rutas. Para desarrollo de UI, usa el Swagger del BFF. Para dominio aislado, el Swagger del microservicio.

Swagger ya viene embebido en cada servicio Spring Boot: **no hace falta rebuild de imágenes** solo para abrirlo. Necesitas pods `Running` y los NodePort de abajo.

---

## URLs en Kubernetes

Con el cluster desplegado (`kubectl get pods -n donaton` → pods `Running`), abre Swagger en el navegador:

| Servicio        | NodePort  | Swagger UI                                   | OpenAPI JSON                       |
| --------------- | --------- | -------------------------------------------- | ---------------------------------- |
| **BFF**         | **30080** | http://localhost:30080/swagger-ui/index.html | http://localhost:30080/v3/api-docs |
| **Auth**        | **30081** | http://localhost:30081/swagger-ui/index.html | http://localhost:30081/v3/api-docs |
| **Donations**   | **30082** | http://localhost:30082/swagger-ui/index.html | http://localhost:30082/v3/api-docs |
| **Necessities** | **30083** | http://localhost:30083/swagger-ui/index.html | http://localhost:30083/v3/api-docs |
| **Logistics**   | **30084** | http://localhost:30084/swagger-ui/index.html | http://localhost:30084/v3/api-docs |

Otros puertos K8s (no son Swagger):

| Servicio    | URL                    | Uso                      |
| ----------- | ---------------------- | ------------------------ |
| Frontend    | http://localhost:30517 | UI React                 |
| API Gateway | http://localhost:30090 | API que consume el front |

### Si no abren las URLs (NodePort antiguos)

Regenera y aplica manifiestos (sin borrar la DB):

```powershell
.\scripts\build-k8s-yaml.ps1
kubectl apply -f k8s.yaml
```

No hace falta rebuild de imágenes (solo cambia el Service de K8s).

### Alternativa: `port-forward`

```powershell
kubectl port-forward -n donaton svc/bff-service 8080:8080
# → http://localhost:8080/swagger-ui/index.html
```

---

## Flujos de prueba recomendados

### 1. API del frontend (BFF)

1. Abrir http://localhost:30080/swagger-ui/index.html
2. `POST /api/auth/login` con email y contraseña válidos.
3. Copiar el campo `token` de la respuesta.
4. Pulsar **Authorize** e ingresar el token (Swagger añade `Bearer`).
5. Probar el CRUD en `/api/donations` (y demás rutas protegidas).

`POST /api/auth/register` en el BFF no devuelve token; después del registro haz **login**.

### 2. Auth Service (JWT)

1. Abrir http://localhost:30081/swagger-ui/index.html
2. `POST /api/v1/auth/login` o `/register` → copiar `accessToken`.
3. **Authorize** con el token.
4. Probar `GET /api/v1/auth/me` o `POST /api/v1/auth/logout`.

Endpoints públicos (sin token): register, login, validate-credentials, validate-token, refresh-token.

### 3. Donations, Necessities (CRUD directo)

No requieren JWT en la versión actual del microservicio.

- Donations: http://localhost:30082/swagger-ui/index.html → `/api/v1/donations`
- Necessities: http://localhost:30083/swagger-ui/index.html → `/api/v1/necessities` (semilla Flyway con 10 ejemplos)

### 4. Logistics

Abrir http://localhost:30084/swagger-ui/index.html y en este orden:

1. `POST .../collection-centers`
2. `POST .../inventories` (usar el `centerId`)
3. `POST .../shipments` (estados: `PLANNED`, `IN_TRANSIT`, `DELIVERED`, `CANCELLED`)

---

## Autenticación en Swagger UI

| Servicio                          | Esquema      | Cuándo usar **Authorize**                    |
| --------------------------------- | ------------ | -------------------------------------------- |
| BFF                               | `bearerAuth` | Rutas protegidas (p. ej. `/api/donations/*`) |
| Auth                              | `bearerAuth` | `/me`, `/logout`                             |
| Donations, Necessities, Logistics | —            | No aplica (acceso abierto a nivel MS)        |

En **Authorize**, pegar solo el valor del token.

---

## Exportar OpenAPI (opcional)

```powershell
curl.exe -o bff-openapi.json http://localhost:30080/v3/api-docs
curl.exe -o auth-openapi.json http://localhost:30081/v3/api-docs
```

---

## Código fuente por servicio

- [backend/bff/README.md](../backend/bff/README.md)
- [backend/ms-auth/README.md](../backend/ms-auth/README.md)
- [backend/ms-donation/README.md](../backend/ms-donation/README.md)
- [backend/ms-necessity/README.md](../backend/ms-necessity/README.md)
- [backend/ms-logistic/README.md](../backend/ms-logistic/README.md)

---

## Notas

- Swagger UI es para **desarrollo y documentación**; no sustituye tests automatizados.
- Si un endpoint del BFF falla por upstream, comprueba que el microservicio correspondiente esté `Running`.
- El frontend (`:30517`) y el API Gateway (`:30090`) **no** sirven Swagger UI.
