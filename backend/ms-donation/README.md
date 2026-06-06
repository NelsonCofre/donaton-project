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
  Contiene las migraciones Flyway para crear e indexar la tabla de donaciones.

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

## Objetivo tecnico

Este servicio separa la gestion de donaciones del resto del sistema y mantiene una responsabilidad unica. Con eso se logra:

- bajo acoplamiento
- persistencia aislada por dominio
- API clara para el BFF
- escalabilidad independiente del modulo de donaciones
