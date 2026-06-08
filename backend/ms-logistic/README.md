# Logistics Service

`logistics-service` es el microservicio encargado de la gestion de logistica en Donaton. Administra centros de acopio, inventario por centro y envios usando Spring Boot, PostgreSQL, JPA y Flyway.

## Que hace

- Gestiona centros de acopio con identificacion y ubicacion.
- Mantiene inventario por centro con recurso y cantidad disponible.
- Registra envios con fecha, estado y centro involucrado.
- Persiste la informacion en PostgreSQL.
- Aplica migraciones con Flyway al iniciar.
- Carga datos demo de centros, inventario y envios mediante semilla Flyway.
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
  Contiene las migraciones Flyway para crear, indexar y poblar las tablas de logistica.

## Datos iniciales (semilla)

Al iniciar el servicio, Flyway ejecuta `V3__seed_logistics.sql`, que inserta datos de ejemplo en las tres tablas del dominio logístico.

El escenario simula una emergencia regional en el área de Valparaíso: centros de acopio activos, stock disponible por centro y envíos en distintos estados del flujo de distribución.

| Recurso | Cantidad |
| --- | ---: |
| Centros de acopio | 4 |
| Registros de inventario | 10 |
| Envíos | 5 |

### Centros de acopio

| Nombre | Ubicación |
| --- | --- |
| Centro Acopio Valparaíso Norte | Valparaíso, sector El Membrillo |
| Centro Acopio Viña del Mar | Viña del Mar, calle 14 Norte |
| Centro Acopio Quilpué | Quilpué, sector El Belloto |
| Centro Acopio Villa Alemana | Villa Alemana, sector Miraflores |

### Inventario (resumen)

- **Valparaíso Norte:** frazadas, colchonetas, kits de higiene.
- **Viña del Mar:** agua potable, leche en polvo, insumos médicos.
- **Quilpué:** alimentos no perecederos, ropa de invierno, pañales.
- **Villa Alemana:** mochilas escolares con útiles.

### Envíos (estados demo)

| Fecha | Estado | Centro |
| --- | --- | --- |
| 2026-06-06 | PLANNED | Valparaíso Norte |
| 2026-06-05 | IN_TRANSIT | Viña del Mar |
| 2026-06-04 | DELIVERED | Quilpué |
| 2026-06-03 | DELIVERED | Valparaíso Norte |
| 2026-06-02 | CANCELLED | Villa Alemana |

La semilla se carga automáticamente al levantar el microservicio (local o Docker). Para verificar los datos:

```bash
curl http://localhost:8084/api/v1/logistics/collection-centers
curl http://localhost:8084/api/v1/logistics/inventories
curl http://localhost:8084/api/v1/logistics/shipments
```

**Nota:** si la migración `V3` ya se ejecutó en una base existente, Flyway no la repetirá. Para recargar la semilla desde cero, elimina el volumen `logistics-pg-data` y vuelve a levantar los contenedores.

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

Listar centros de acopio (con semilla: 4 registros demo):

```bash
curl http://localhost:8084/api/v1/logistics/collection-centers
curl http://localhost:8084/api/v1/logistics/inventories
curl http://localhost:8084/api/v1/logistics/shipments
```

Crear un centro de acopio adicional:

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
