# Logistics Service

`logistics-service` es el microservicio encargado de la gestion de logistica en Donaton. Administra centros de acopio, inventario por centro y envios usando Spring Boot, PostgreSQL, JPA y Flyway.

## Que hace

- Gestiona centros de acopio con identificacion y ubicacion.
- Mantiene inventario por centro con recurso y cantidad disponible.
- Registra envios con fecha, estado y centro involucrado.
- Persiste la informacion en PostgreSQL.
- Aplica migraciones con Flyway al iniciar.
- Expone una API REST pensada para ser consumida por el BFF (integracion pendiente).

## Flujo general

**Objetivo:** Frontend -> BFF -> `logistics-service` -> base de datos `logistics_db`

**Estado actual:** el microservicio responde de forma autonoma en el puerto `8084`. El BFF y el frontend aun no reenvian solicitudes a logistica; las pruebas se realizan directamente contra esta API.

El servicio recibe requests REST, delega la logica a la capa de servicio y usa los repositorios JPA para acceder a la base de datos.

## Componentes principales

- `controller/`
  `LogisticsController` expone los endpoints REST del CRUD.

- `service/`
  `LogisticsService` define el contrato de negocio y `LogisticsServiceImpl` implementa la logica de creacion, consulta, actualizacion y eliminacion.

- `repository/`
  `CentroAcopioRepository`, `InventarioRepository` y `EnvioRepository` encapsulan el acceso a datos.

- `model/`
  `CentroAcopio`, `Inventario` y `Envio` representan las entidades persistidas. `EstadoEnvio` define los estados validos del envio.

- `dto/`
  DTOs de request/response separan el contrato HTTP del modelo interno.

- `config/`
  `SecurityConfiguration` deja los endpoints accesibles en la version actual.

- `exception/`
  `GlobalExceptionHandler` centraliza errores de validacion y recursos no encontrados.

- `db/migration/`
  Contiene las migraciones Flyway para crear e indexar las tablas de logistica.

## Endpoints expuestos

### Centros de acopio

- `POST /api/v1/logistics/collection-centers`
- `GET /api/v1/logistics/collection-centers`
- `GET /api/v1/logistics/collection-centers/{id}`
- `PUT /api/v1/logistics/collection-centers/{id}`
- `DELETE /api/v1/logistics/collection-centers/{id}`

### Inventario

- `POST /api/v1/logistics/inventories`
- `GET /api/v1/logistics/inventories`
- `GET /api/v1/logistics/inventories/{id}`
- `PUT /api/v1/logistics/inventories/{id}`
- `DELETE /api/v1/logistics/inventories/{id}`

### Envios

- `POST /api/v1/logistics/shipments`
- `GET /api/v1/logistics/shipments`
- `GET /api/v1/logistics/shipments/{id}`
- `PUT /api/v1/logistics/shipments/{id}`
- `DELETE /api/v1/logistics/shipments/{id}`

## Configuracion

La configuracion principal vive en `application.properties`:

- `spring.datasource.*` para PostgreSQL
- `spring.jpa.*` para JPA/Hibernate
- `spring.flyway.*` para migraciones
- `server.port`

Por defecto:

- puerto `8084`
- base de datos `logistics_db`
- usuario `logistic`

## Docker

El codigo fuente vive en `backend/ms-logistic`. En `docker-compose.yml` el servicio se llama `logistics-service` y se construye desde esa carpeta.

Desde la raiz del proyecto:

```bash
docker compose up --build postgres-logistics logistics-service
```

En segundo plano:

```bash
docker compose up --build -d postgres-logistics logistics-service
```

Esto construye la imagen, levanta `postgres-logistics` (puerto **5437** en el host) y expone `logistics-service` en el puerto **8084**.

Variables opcionales en compose:

- `LOGISTICS_SERVICE_PORT` (default `8084`)
- `LOGISTICS_SERVER_PORT` (default `8084`)
- `LOGISTICS_POSTGRES_PUBLISH_PORT` (default `5437`)
- `LOGISTICS_POSTGRES_USER` / `LOGISTICS_POSTGRES_PASSWORD` / `LOGISTICS_POSTGRES_DB`
- `LOGISTICS_SPRING_APPLICATION_NAME` (default `logistics-service`)

### Probar la API

Listar centros de acopio (lista vacia al iniciar):

```bash
curl http://localhost:8084/api/v1/logistics/collection-centers
```

Crear un centro de acopio:

```bash
curl -X POST http://localhost:8084/api/v1/logistics/collection-centers \
  -H "Content-Type: application/json" \
  -d '{"name":"Centro Norte","location":"Av. Principal 100, Santiago"}'
```

Ver logs del contenedor:

```bash
docker compose logs -f logistics-service
```

## Objetivo tecnico

Este servicio separa la gestion logistica del resto del sistema y mantiene una responsabilidad unica. Con eso se logra:

- bajo acoplamiento
- persistencia aislada por dominio
- API clara para el BFF
- escalabilidad independiente del modulo de logistica
