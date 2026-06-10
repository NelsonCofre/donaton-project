# Donation Service

`donation-service` es el microservicio encargado del CRUD de donaciones en Donaton. Administra el registro, consulta, actualizacion y eliminacion de donaciones usando Spring Boot, PostgreSQL, JPA y Flyway.

## Que hace

- Registra donaciones con recurso, cantidad, origen, fecha y centro de acopio.
- Lista todas las donaciones o busca una por id.
- Permite actualizar y eliminar registros existentes.
- Persiste la informacion en PostgreSQL.
- Aplica migraciones con Flyway al iniciar.
- Expone una API REST consumida por el BFF.

## Flujo general

Frontend -> BFF -> `donation-service` -> base de datos `donation_db`

El servicio recibe requests REST, delega la logica a la capa de servicio y usa `DonationRepository` para acceder a la base de datos.

## Componentes principales

- `controller/`
  `DonationController` expone los endpoints REST del CRUD.

- `service/`
  `DonationService` define el contrato de negocio y `DonationServiceImpl` implementa la logica de creacion, consulta, actualizacion y eliminacion.

- `repository/`
  `DonationRepository` encapsula el acceso a datos con Spring Data JPA.

- `model/`
  `Donation` representa la entidad persistida en la tabla `donations`.

- `dto/`
  `DonationRequestDto` y `DonationResponseDto` separan el contrato HTTP del modelo interno.

- `config/`
  `SecurityConfiguration` deja los endpoints accesibles en la version actual.

- `exception/`
  `GlobalExceptionHandler` centraliza errores de validacion y recursos no encontrados.

- `db/migration/`
  Contiene las migraciones Flyway para crear, indexar y poblar la tabla de donaciones.

## Datos iniciales (semilla)

Al iniciar el servicio, Flyway ejecuta `V3__seed_donations.sql`, que inserta **10 donaciones de ejemplo** en la tabla `donations`.

El escenario complementa la emergencia regional en el area de Valparaiso (semilla de `ms-necessity`): donaciones recibidas en centros de acopio, con origenes de empresas, municipalidades, donantes individuales y organizaciones.

| Recurso | Cantidad | Origen | Centro de acopio |
| --- | ---: | --- | --- |
| Frazadas | 250 | Empresa Textiles Pacífico S.A. | Centro Acopio Valparaíso Norte |
| Agua potable (litros) | 3000 | Supermercados del Mar Ltda. | Centro Acopio Viña del Mar |
| Alimentos no perecederos | 500 | Cruz Roja Chile – Regional Valparaíso | Centro Acopio Valparaíso Norte |
| Kits de higiene personal | 200 | Municipalidad de Viña del Mar | Centro Acopio Viña del Mar |
| Insumos médicos (vendajes y antiséptico) | 80 | Farmacias Cruz del Sur | Centro Acopio Quilpué |
| Pañales talla M | 350 | María González (donante individual) | Centro Acopio Villa Alemana |
| Colchonetas | 120 | Constructora Andina S.A. | Centro Acopio Valparaíso Norte |
| Leche en polvo | 180 | ONG Alimenta Chile | Centro Acopio Quilpué |
| Ropa de invierno (adulto) | 400 | Municipalidad de Quilpué | Centro Acopio Quilpué |
| Mochilas escolares con útiles | 150 | Colegio San Agustín (colecta solidaria) | Centro Acopio Villa Alemana |

La semilla se carga automaticamente al levantar el microservicio (local o Docker). Para verificar los datos:

```bash
curl http://localhost:8082/api/v1/donations
```

**Nota:** si la migracion `V3` ya se ejecuto en una base existente, Flyway no la repetira. Para recargar la semilla desde cero, elimina el volumen `donation-pg-data` y vuelve a levantar los contenedores.

## Endpoints expuestos

- `POST /api/v1/donations`
- `GET /api/v1/donations`
- `GET /api/v1/donations/{id}`
- `PUT /api/v1/donations/{id}`
- `DELETE /api/v1/donations/{id}`

## Configuracion

La configuracion principal vive en `application.properties`:

- `spring.datasource.*` para PostgreSQL
- `spring.jpa.*` para JPA/Hibernate
- `spring.flyway.*` para migraciones
- `server.port`

Por defecto:

- puerto `8082`
- base de datos `donation_db`
- usuario `donation`

## Docker

El codigo fuente vive en `backend/ms-donation`. En `docker-compose.yml` el servicio se llama `donation-service` y se construye desde esa carpeta.

Desde la raiz del proyecto:

```bash
docker compose up --build postgres-donation donation-service
```

En segundo plano:

```bash
docker compose up --build -d postgres-donation donation-service
```

Esto construye la imagen, levanta `postgres-donation` (puerto **5436** en el host) y expone `donation-service` en el puerto **8082**.

Variables opcionales en compose:

- `DONATION_SERVICE_PORT` (default `8082`)
- `DONATION_SERVER_PORT` (default `8082`)
- `DONATION_POSTGRES_PUBLISH_PORT` (default `5436`)
- `DONATION_POSTGRES_USER` / `DONATION_POSTGRES_PASSWORD` / `DONATION_POSTGRES_DB`
- `DONATION_SPRING_APPLICATION_NAME` (default `donation-service`)

### Probar la API

Listar donaciones (10 registros de semilla al iniciar en base nueva):

```bash
curl http://localhost:8082/api/v1/donations
```

## Objetivo tecnico

Este servicio separa la gestion de donaciones del resto del sistema y mantiene una responsabilidad unica. Con eso se logra:

- bajo acoplamiento
- persistencia aislada por dominio
- API clara para el BFF
- escalabilidad independiente del modulo de donaciones
