# BFF Service

`bff-service` es el Backend for Frontend de Donaton. Su responsabilidad es recibir las solicitudes del frontend, validarlas y reenviarlas a los microservicios internos con un contrato mas simple para la UI.

## Que hace

- Expone endpoints pensados para el frontend.
- Se comunica con `auth-service` y `donation-service` usando `RestClient`.
- Traduce requests y responses entre el formato del frontend y el formato interno de los microservicios.
- Valida el token JWT en las rutas de donaciones antes de reenviar la solicitud.
- Centraliza CORS y manejo de errores hacia servicios externos.

## Flujo general

Frontend -> `bff-service` -> microservicio interno

Hoy el flujo principal es:

- `POST /api/auth/login` y `POST /api/auth/register`
  El BFF recibe la solicitud del frontend, la transforma y la reenvia a `auth-service`.

- `GET/POST/PUT/DELETE /api/donations`
  El BFF valida el token con `auth-service` y luego reenvia la operacion a `donation-service`.

## Componentes principales

- `controller/`
  Define los endpoints publicos del BFF.
  - `AuthBffController`: login y registro.
  - `DonationBffController`: CRUD de donaciones.

- `client/`
  Encapsula las llamadas HTTP a otros servicios.
  - `AuthServiceClient`
  - `DonationServiceClient`
  - `RestClientHelper`

- `mapper/`
  Convierte DTOs del frontend a DTOs internos y viceversa.
  - `AuthMapper`
  - `DonationMapper`

- `security/`
  Contiene `JwtAuthFilter`, que protege las rutas `/api/donations` validando el token contra `auth-service`.

- `config/`
  Configuracion de propiedades, CORS, filtros, `RestClient` y `OpenApiConfig` (documentacion Swagger).

- `dto/`
  Separa contratos del frontend (`dto/api`) de contratos internos hacia microservicios (`dto/auth`, `dto/donation`).

- `exception/`
  Manejo centralizado de errores del BFF y errores de servicios externos.

## Endpoints expuestos

- `/api/auth/login`
- `/api/auth/register`
- `/api/donations`
- `/api/donations/{id}`

## Documentacion API (Swagger)

Con el servicio en ejecucion (puerto `8080`):

| Recurso | URL |
| --- | --- |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |

La documentacion refleja la integracion **actual** del BFF: auth y donaciones. Necesidades y logistica apareceran cuando se agreguen sus controllers.

Flujo sugerido para probar donaciones:

1. Levantar `auth-service`, `donation-service` y `bff-service`.
2. Ejecutar `POST /api/auth/login` y copiar el campo `token`.
3. Pulsar **Authorize** en Swagger UI e ingresar el token.
4. Probar el CRUD en `/api/donations`.

## Configuracion

Las URLs de los servicios internos y el origen permitido para CORS se configuran en `application.properties`:

- `app.services.auth.base-url`
- `app.services.donation.base-url`
- `app.cors.allowed-origins`

Por defecto:

- `auth-service` -> `http://localhost:8081`
- `donation-service` -> `http://localhost:8082`
- frontend -> `http://localhost:5173`

## Objetivo tecnico

Este servicio evita que el frontend consuma microservicios directamente. Con eso se logra:

- menor acoplamiento entre frontend y backend
- contratos mas simples para la interfaz
- centralizacion de seguridad y CORS
- mejor punto de entrada para integrar mas servicios en el futuro
