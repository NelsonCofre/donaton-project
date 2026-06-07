# Necessity Service

`necessity-service` es el microservicio encargado del CRUD de necesidades en terreno en Donaton. Administra el registro, consulta, actualizacion y eliminacion de necesidades reportadas usando Spring Boot, PostgreSQL, JPA y Flyway.

## Que hace

- Registra necesidades con recurso, cantidad, ubicacion, fecha y quien reporto.
- Lista todas las necesidades o busca una por id.
- Permite actualizar y eliminar registros existentes.
- Persiste la informacion en PostgreSQL.
- Aplica migraciones con Flyway al iniciar.
- Expone una API REST consumida por el BFF.

## Flujo general

Frontend -> BFF -> `necessity-service` -> base de datos `necessity_db`

El servicio recibe requests REST, delega la logica a la capa de servicio y usa `NecessityRepository` para acceder a la base de datos.

## Componentes principales

- `controller/`
  `NecessityController` expone los endpoints REST del CRUD.

- `service/`
  `NecessityService` define el contrato de negocio y `NecessityServiceImpl` implementa la logica de creacion, consulta, actualizacion y eliminacion.

- `repository/`
  `NecessityRepository` encapsula el acceso a datos con Spring Data JPA.

- `model/`
  `Necessity` representa la entidad persistida en la tabla `necessities`.

- `dto/`
  `NecessityRequestDto` y `NecessityResponseDto` separan el contrato HTTP del modelo interno.

- `config/`
  `SecurityConfiguration` deja los endpoints accesibles en la version actual.

- `exception/`
  `GlobalExceptionHandler` centraliza errores de validacion y recursos no encontrados.

- `db/migration/`
  Contiene las migraciones Flyway para crear, indexar y poblar la tabla de necesidades.

## Datos iniciales (semilla)

Al iniciar el servicio, Flyway ejecuta `V3__seed_necessities.sql`, que inserta **10 necesidades de ejemplo** en la tabla `necessities`.

El escenario simula una emergencia regional en el área de Valparaíso, con reportes de municipalidades y centros investigadores. Los registros cubren recursos como ropa, agua, alimentos, higiene e insumos médicos, en distintas ubicaciones y fechas.

| Recurso | Cantidad | Ubicación | Reportado por |
| --- | ---: | --- | --- |
| Frazadas | 200 | Valparaíso, sector Playa Ancha | Municipalidad de Valparaíso |
| Agua potable (litros) | 5000 | Viña del Mar, campamento provisional | Centro Investigador Regional Valparaíso |
| Alimentos no perecederos | 800 | Quilpué, sector El Belloto | Municipalidad de Quilpué |
| Kits de higiene personal | 350 | Villa Alemana, sector Colliguay | Centro Investigador Regional Valparaíso |
| Insumos médicos (vendajes y antiséptico) | 120 | Concón, posta rural | Centro Investigador Regional Valparaíso |
| Pañales talla M | 400 | Casablanca, albergue comunal | Municipalidad de Casablanca |
| Colchonetas | 150 | Valparaíso, sector Barrio Puerto | Municipalidad de Valparaíso |
| Leche en polvo | 300 | Viña del Mar, sector Reñaca Alto | Centro Investigador Regional Valparaíso |
| Ropa de invierno (adulto) | 600 | Quilpué, sector Los Eucaliptus | Municipalidad de Quilpué |
| Mochilas escolares con útiles | 180 | Villa Alemana, sector Miraflores | Municipalidad de Villa Alemana |

La semilla se carga automáticamente al levantar el microservicio (local o Docker). Para verificar los datos:

```bash
curl http://localhost:8083/api/v1/necessities
```

**Nota:** si la migracion `V3` ya se ejecuto en una base existente, Flyway no la repetira. Para recargar la semilla desde cero, elimina el volumen `necessity-pg-data` y vuelve a levantar los contenedores.

## Endpoints expuestos

- `POST /api/v1/necessities`
- `GET /api/v1/necessities`
- `GET /api/v1/necessities/{id}`
- `PUT /api/v1/necessities/{id}`
- `DELETE /api/v1/necessities/{id}`

## Configuracion

La configuracion principal vive en `application.properties`:

- `spring.datasource.*` para PostgreSQL
- `spring.jpa.*` para JPA/Hibernate
- `spring.flyway.*` para migraciones
- `server.port`

Por defecto:

- puerto `8083`
- base de datos `necessity_db`
- usuario `necessity`

## Objetivo tecnico

Este servicio separa la gestion de necesidades en terreno del resto del sistema y mantiene una responsabilidad unica. Con eso se logra:

- bajo acoplamiento
- persistencia aislada por dominio
- API clara para el BFF
- escalabilidad independiente del modulo de necesidades
