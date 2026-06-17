# Auth Service

`auth-service` es el microservicio encargado de la autenticacion y gestion basica de usuarios en Donaton. Expone endpoints para registro, inicio de sesion, validacion de token, refresco de sesion y consulta del usuario autenticado.

## Que hace

- Registra usuarios con correo, contrasena y rol.
- Autentica credenciales y genera access token JWT.
- Genera y valida refresh tokens.
- Permite validar tokens desde otros servicios, como el BFF.
- Mantiene una blacklist para invalidar tokens cerrados o revocados.
- Protege endpoints autenticados con Spring Security y filtro JWT.

## Flujo general

Cliente o BFF -> `auth-service` -> base de datos `auth_db`

Flujos principales:

- `POST /api/v1/auth/register`
  Crea un usuario y devuelve tokens de autenticacion.

- `POST /api/v1/auth/login`
  Valida credenciales y devuelve access token + refresh token.

- `POST /api/v1/auth/validate-token`
  Verifica que el token sea valido y no este revocado.

- `POST /api/v1/auth/refresh-token`
  Emite una nueva sesion usando un refresh token valido.

- `POST /api/v1/auth/logout`
  Revoca refresh tokens y agrega el access token a la blacklist.

- `GET /api/v1/auth/me`
  Devuelve el perfil del usuario autenticado.

## Componentes principales

- `controller/`
  `AuthController` expone la API publica del servicio.

- `service/`
  `AuthService` concentra la logica de autenticacion.
  `RefreshTokenService` administra refresh tokens.
  `TokenBlacklistService` administra tokens invalidados.

- `security/`
  `SecurityConfig`, `JwtAuthenticationFilter`, `JwtService` y `CustomUserDetailsService` implementan la seguridad JWT.

- `config/`
  `OpenApiConfig` define los metadatos de la documentacion Swagger/OpenAPI y el esquema JWT (`bearerAuth`).

- `repository/`
  Acceso a persistencia con `UserAccountRepository`, `RefreshTokenRepository` y `BlacklistedTokenRepository`.

- `model/`
  Define entidades y enums como `UserAccount`, `RefreshToken`, `BlacklistedToken`, `Role` y `Permission`.

- `dto/`
  Contratos de entrada y salida para login, registro, validacion y respuestas de usuario.

- `exception/`
  Manejo centralizado de errores mediante `ApiExceptionHandler`.

## Endpoints expuestos

- `/api/v1/auth/register`
- `/api/v1/auth/login`
- `/api/v1/auth/validate-credentials`
- `/api/v1/auth/validate-token`
- `/api/v1/auth/refresh-token`
- `/api/v1/auth/logout`
- `/api/v1/auth/me`

## Documentacion API (Swagger)

Con el servicio en ejecucion (puerto `8081`):

| Recurso | URL |
| --- | --- |
| Swagger UI | http://localhost:8081/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8081/v3/api-docs |

Flujo sugerido para probar endpoints protegidos:

1. Ejecutar `POST /api/v1/auth/login` o `POST /api/v1/auth/register`.
2. Copiar el valor de `accessToken` de la respuesta.
3. Pulsar **Authorize** en Swagger UI e ingresar el token (Swagger agrega el prefijo `Bearer`).
4. Probar `GET /api/v1/auth/me` o `POST /api/v1/auth/logout`.

Los endpoints de registro, login, validacion y refresh no requieren token. `/me` y `/logout` si.

## Configuracion

La configuracion principal vive en `application.properties`:

- `spring.datasource.*` para PostgreSQL
- `spring.flyway.*` para migraciones
- `security.jwt.secret`
- `security.jwt.access-token-expiration-minutes`
- `security.jwt.refresh-token-expiration-days`

Por defecto:

- puerto `8081`
- base de datos `auth_db`
- usuario `admin`

## Objetivo tecnico

Este servicio centraliza la autenticacion del sistema y evita que otros microservicios gestionen usuarios o tokens por separado. Con eso se logra:

- seguridad unificada
- validacion centralizada de JWT
- sesion desacoplada del frontend
- reutilizacion desde el BFF y otros servicios
