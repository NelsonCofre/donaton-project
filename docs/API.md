# Documentación API (Swagger / OpenAPI)

Índice central de la documentación interactiva de las APIs de Donaton. Cada backend expone **Swagger UI** y un JSON OpenAPI 3 generado con [springdoc-openapi](https://springdoc.org/) (`2.8.6`).

## Dos capas de API

| Capa | Quién la consume | Prefijo típico | Dónde documentar |
|------|------------------|----------------|------------------|
| **Frontend (BFF)** | React en el navegador | `/api/auth`, `/api/donations` | Swagger del BFF (`:8080`) |
| **Microservicios** | BFF, pruebas directas, integraciones | `/api/v1/*` | Swagger de cada MS (`:8081`–`:8084`) |

El contrato del **frontend** no es igual al de los microservicios: el BFF adapta DTOs y rutas. Para desarrollo de UI, usar el Swagger del BFF. Para desarrollo de dominio o pruebas aisladas, usar el Swagger del microservicio correspondiente.

**Estado actual del BFF:** integra **autenticación** y **donaciones**. Necesidades y logística están disponibles en sus microservicios pero aún no se reenvían desde el BFF.

---

## Levantar el entorno

Desde la raíz del repositorio:

```bash
docker compose up --build
```

Stack completo (frontend, BFF, auth, donation, necessity, logistics y bases de datos):

```bash
docker compose up --build -d
```

Solo un microservicio (ejemplo logística):

```bash
docker compose up --build postgres-logistics logistics-service
```

---

## Tabla de servicios

| Servicio | Código fuente | Puerto (host) | Swagger UI | OpenAPI JSON |
|----------|---------------|---------------|------------|--------------|
| **BFF** | `backend/bff` | **8080** | http://localhost:8080/swagger-ui/index.html | http://localhost:8080/v3/api-docs |
| **Auth** | `backend/ms-auth` | **8081** | http://localhost:8081/swagger-ui/index.html | http://localhost:8081/v3/api-docs |
| **Donations** | `backend/ms-donation` | **8082** | http://localhost:8082/swagger-ui/index.html | http://localhost:8082/v3/api-docs |
| **Necessities** | `backend/ms-necessity` | **8083** | http://localhost:8083/swagger-ui/index.html | http://localhost:8083/v3/api-docs |
| **Logistics** | `backend/ms-logistic` | **8084** | http://localhost:8084/swagger-ui/index.html | http://localhost:8084/v3/api-docs |

Documentación detallada por módulo:

- [backend/bff/README.md](../backend/bff/README.md)
- [backend/ms-auth/README.md](../backend/ms-auth/README.md)
- [backend/ms-donation/README.md](../backend/ms-donation/README.md)
- [backend/ms-necessity/README.md](../backend/ms-necessity/README.md)
- [backend/ms-logistic/README.md](../backend/ms-logistic/README.md)

---

## Flujos de prueba recomendados

### 1. API del frontend (BFF)

**Requisitos:** `auth-service`, `donation-service` y `bff-service` en ejecución.

1. Abrir http://localhost:8080/swagger-ui/index.html
2. `POST /api/auth/login` con email y contraseña válidos.
3. Copiar el campo `token` de la respuesta.
4. Pulsar **Authorize** e ingresar el token (Swagger añade el prefijo `Bearer`).
5. Probar el CRUD en `/api/donations`.

`POST /api/auth/register` no devuelve token en el BFF; solo datos del usuario. Para probar donaciones, usar **login** después del registro.

### 2. Auth Service (JWT)

**Requisitos:** `auth-service` en ejecución.

1. Abrir http://localhost:8081/swagger-ui/index.html
2. `POST /api/v1/auth/login` o `/register` → copiar `accessToken`.
3. **Authorize** con el token.
4. Probar `GET /api/v1/auth/me` o `POST /api/v1/auth/logout`.

Endpoints públicos (sin token): register, login, validate-credentials, validate-token, refresh-token.

### 3. Donations, Necessities (CRUD directo)

No requieren JWT en la versión actual.

- **Donations:** http://localhost:8082/swagger-ui/index.html → CRUD `/api/v1/donations`
- **Necessities:** http://localhost:8083/swagger-ui/index.html → CRUD `/api/v1/necessities`  
  Al iniciar con Flyway, la semilla carga **10 necesidades de ejemplo** (área Valparaíso). `GET /api/v1/necessities` debería listarlas.

### 4. Logistics (tres recursos)

**Requisitos:** `logistics-service` en ejecución.

Abrir http://localhost:8084/swagger-ui/index.html y seguir este orden:

1. `POST /api/v1/logistics/collection-centers` → crear centro de acopio.
2. `POST /api/v1/logistics/inventories` → usar el `centerId` del paso anterior.
3. `POST /api/v1/logistics/shipments` → envío con estado `PLANNED`, `IN_TRANSIT`, `DELIVERED` o `CANCELLED`.

---

## Autenticación en Swagger UI

| Servicio | Esquema | Cuándo usar **Authorize** |
|----------|---------|---------------------------|
| BFF | `bearerAuth` | Rutas `/api/donations/*` |
| Auth | `bearerAuth` | `/me`, `/logout` |
| Donations, Necessities, Logistics | — | No aplica (acceso abierto hoy) |

En **Authorize**, pegar solo el valor del token; Swagger UI agrega `Bearer ` automáticamente.

---

## Exportar especificaciones OpenAPI (opcional)

Para entregables o versionado del contrato, se puede descargar el JSON desde cada servicio en ejecución:

```bash
curl -o bff-openapi.json http://localhost:8080/v3/api-docs
curl -o auth-openapi.json http://localhost:8081/v3/api-docs
curl -o donation-openapi.json http://localhost:8082/v3/api-docs
curl -o necessity-openapi.json http://localhost:8083/v3/api-docs
curl -o logistic-openapi.json http://localhost:8084/v3/api-docs
```

Opcionalmente guardar los archivos en `docs/openapi/` para incluirlos en el repositorio.

---

## Notas

- Swagger UI es para **desarrollo y documentación**; no sustituye pruebas automatizadas ni SonarQube.
- Si un endpoint del BFF falla con error de servicio externo, comprobar que el microservicio upstream correspondiente esté levantado.
- En Kubernetes, los puertos pueden diferir; ver [KUBERNETES.md](KUBERNETES.md) (BFF en `:30080`).
