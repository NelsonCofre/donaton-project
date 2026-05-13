# EXPLICACIÓN TÉCNICA DEL AUTH-SERVICE

## 1. Objetivo del microservicio

`auth-service` es el microservicio encargado de centralizar la autenticación y autorización del sistema Donaton. Su responsabilidad principal es:

- Registrar usuarios.
- Validar credenciales.
- Iniciar sesión.
- Emitir tokens JWT de acceso.
- Gestionar refresh tokens.
- Validar tokens.
- Resolver roles y permisos.
- Cerrar sesión revocando credenciales activas.

Siguiendo el enfoque de microservicios descrito en el `README.md`, este servicio encapsula su propia lógica de seguridad y persistencia, evitando acoplar autenticación con otros dominios como donaciones, necesidades o logística.

## 2. Arquitectura interna

El servicio fue organizado en capas para mantener separación de responsabilidades:

- `controller`: expone los endpoints HTTP del microservicio.
- `service`: concentra la lógica de negocio.
- `repository`: abstrae la persistencia mediante Spring Data JPA.
- `model`: define entidades y enums del dominio de autenticación.
- `dto`: define contratos de entrada y salida del API.
- `security`: encapsula JWT, filtros, carga de usuarios y configuración de Spring Security.
- `exception`: maneja errores de negocio y respuestas HTTP consistentes.

Esta estructura facilita evolución, pruebas, mantenimiento y reutilización del servicio dentro de una arquitectura distribuida.

## 3. Paquetes y clases principales

### 3.1 Controller

- `AuthController`

Expone los endpoints:

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/validate-credentials`
- `POST /api/v1/auth/validate-token`
- `POST /api/v1/auth/refresh-token`
- `POST /api/v1/auth/logout`
- `GET /api/v1/auth/me`

### 3.2 Service

- `AuthService`
  Orquesta el flujo principal de autenticación: registro, login, validación de credenciales, validación de token, refresh y logout.

- `RefreshTokenService`
  Administra creación, validación y revocación de refresh tokens persistidos.

- `TokenBlacklistService`
  Maneja la invalidación explícita de access tokens al hacer logout.

### 3.3 Repository

- `UserAccountRepository`
- `RefreshTokenRepository`
- `BlacklistedTokenRepository`

Todos usan Spring Data JPA y permiten que el dominio no dependa de consultas SQL manuales.

### 3.4 Security

- `SecurityConfig`
- `JwtService`
- `JwtAuthenticationFilter`
- `CustomUserDetails`
- `CustomUserDetailsService`
- `RestAuthenticationEntryPoint`

Estas clases implementan la seguridad stateless con JWT y la integración con Spring Security.

## 4. Modelo de dominio

### 4.1 `UserAccount`

Representa al usuario autenticable del sistema.

Campos principales:

- `id`
- `email`
- `passwordHash`
- `role`
- `enabled`
- `createdAt`
- `updatedAt`

Decisiones técnicas:

- El email es único y funciona como identificador lógico del usuario.
- La contraseña nunca se guarda en texto plano.
- El rol se persiste como `ENUM` para claridad y bajo acoplamiento.

### 4.2 `Role`

Roles implementados:

- `ADMIN`
- `USER`

Esto respeta la lógica de negocio descrita en el `README.md`, donde el usuario tiene un rol de tipo `ADMIN` o `USER`.

### 4.3 `Permission`

Permisos implementados:

- `USER_READ`
- `USER_WRITE`
- `USER_DELETE`
- `AUTH_MANAGE`

Asignación:

- `ADMIN`: posee todos los permisos definidos.
- `USER`: posee solo `USER_READ`.

Con esto el microservicio separa claramente rol y permiso:

- El rol agrupa capacidades.
- El permiso representa una acción concreta.

### 4.4 `RefreshToken`

Persistencia de refresh tokens para mantener sesiones renovables sin reingresar credenciales.

Campos:

- `id`
- `token`
- `user`
- `expiresAt`
- `revoked`
- `createdAt`

### 4.5 `BlacklistedToken`

Persistencia de access tokens invalidados por logout.

Campos:

- `id`
- `token`
- `expiresAt`
- `createdAt`

Esto permite una revocación explícita de JWT incluso cuando la arquitectura es stateless.

## 5. Flujo técnico de autenticación

### 5.1 Registro

Endpoint: `POST /api/v1/auth/register`

Flujo:

1. Se valida el DTO de entrada.
2. Se verifica que el email no exista.
3. Se cifra la contraseña con `BCryptPasswordEncoder`.
4. Se persiste el usuario.
5. Se genera un access token JWT.
6. Se genera un refresh token persistido.
7. Se responde con ambos tokens y datos básicos del usuario.

### 5.2 Inicio de sesión

Endpoint: `POST /api/v1/auth/login`

Flujo:

1. Se autentica con `AuthenticationManager`.
2. Spring Security consulta al usuario mediante `CustomUserDetailsService`.
3. La contraseña ingresada se compara contra el hash almacenado.
4. Si las credenciales son válidas, se emiten:
   - access token JWT
   - refresh token persistido

### 5.3 Validación de credenciales

Endpoint: `POST /api/v1/auth/validate-credentials`

Uso:

- Permite validar email y password sin crear sesión nueva.
- Es útil para integraciones internas o flujos previos a operaciones sensibles.

### 5.4 Generación de token JWT

La clase `JwtService` genera un access token firmado con HMAC.

Claims incluidos:

- `sub`: email del usuario
- `role`: rol del usuario
- `permissions`: lista de permisos
- `tokenType`: `"access"`

El token permite que otros microservicios o un gateway puedan validar identidad y autorización sin compartir sesión.

### 5.5 Validación de token

Endpoint: `POST /api/v1/auth/validate-token`

Flujo:

1. Se recibe el token.
2. Se verifica firma y expiración.
3. Se revisa si fue invalidado por logout.
4. Se verifica que sea un token de acceso.
5. Se obtiene el usuario asociado.
6. Se retorna el estado de validez, sujeto, rol y permisos.

### 5.6 Refresh token

Endpoint: `POST /api/v1/auth/refresh-token`

Flujo:

1. Se recibe el refresh token.
2. Se valida que exista, no esté revocado y no haya expirado.
3. Se invalida el refresh token usado.
4. Se emite un nuevo access token.
5. Se emite un nuevo refresh token.

Esta estrategia reduce reutilización de refresh tokens y mejora seguridad.

### 5.7 Logout

Endpoint: `POST /api/v1/auth/logout`

Flujo:

1. Se toma el access token desde el header `Authorization`.
2. Se agrega a blacklist hasta su expiración.
3. Se revocan los refresh tokens activos del usuario.
4. Si el body incluye un refresh token concreto, también se revoca explícitamente.

Esto garantiza que:

- el access token no vuelva a aceptarse,
- el refresh token no pueda renovar sesión.

## 6. Seguridad implementada

### 6.1 Password hashing

Se usa `BCryptPasswordEncoder`.

Razones:

- hashing unidireccional seguro,
- salt incorporado,
- estándar ampliamente aceptado para contraseñas.

Nunca se almacena la contraseña original.

### 6.2 Seguridad stateless

La configuración de Spring Security usa:

- `SessionCreationPolicy.STATELESS`
- JWT en header `Authorization`
- filtro `JwtAuthenticationFilter`

Esto es consistente con una arquitectura de microservicios, donde no se depende de sesión HTTP compartida.

### 6.3 Carga de usuario

`CustomUserDetailsService` resuelve al usuario desde base de datos usando el email.

`CustomUserDetails` transforma:

- rol,
- permisos,
- hash de contraseña,

al formato requerido por Spring Security.

### 6.4 Filtro JWT

`JwtAuthenticationFilter`:

1. Intercepta requests.
2. Busca `Bearer token`.
3. Verifica que no esté en blacklist.
4. Valida que sea token de acceso.
5. Extrae el sujeto.
6. Carga al usuario.
7. Inserta la autenticación en el `SecurityContext`.

### 6.5 Endpoints públicos y protegidos

Públicos:

- `register`
- `login`
- `validate-credentials`
- `validate-token`
- `refresh-token`

Protegidos:

- `logout`
- `me`

## 7. DTOs expuestos

### Entrada

- `RegisterRequest`
- `LoginRequest`
- `RefreshTokenRequest`
- `TokenValidationRequest`
- `LogoutRequest`

### Salida

- `AuthResponse`
- `TokenValidationResponse`
- `UserResponse`
- `LogoutResponse`

Los DTOs aíslan el contrato externo del modelo persistente, evitando exponer entidades JPA directamente.

## 8. Manejo de errores

Se agregaron excepciones específicas:

- `AuthenticationFailedException`
- `ConflictException`
- `InvalidTokenException`
- `ResourceNotFoundException`

`ApiExceptionHandler` traduce estas excepciones a respuestas HTTP consistentes.

Mapeo principal:

- `400` validación de DTO
- `401` credenciales o token inválido
- `404` recurso inexistente
- `409` conflicto de negocio
- `500` error inesperado

## 9. Persistencia y decisiones sobre base de datos

No se incorporaron componentes adicionales de infraestructura en esta etapa.

Para permitir que el servicio arranque y persista entidades en desarrollo:

- se mantuvo PostgreSQL como base principal,
- `spring.jpa.hibernate.ddl-auto=update` crea o ajusta tablas automáticamente en ejecución.

Importante:

- Esto facilita el desarrollo del microservicio en esta etapa.
- En una evolución productiva, lo recomendable es formalizar la evolución del esquema con una estrategia versionada.

## 10. Configuración relevante

Propiedades principales:

- `security.jwt.secret`
- `security.jwt.access-token-expiration-minutes`
- `security.jwt.refresh-token-expiration-days`
- `spring.datasource.*`

Se dejaron con soporte para variables de entorno y valores por defecto razonables.

## 11. Pruebas

Se agregó configuración de test con H2 en memoria para que el contexto de Spring levante sin depender de PostgreSQL externo.

Objetivo:

- permitir compilación,
- validar wiring de beans,
- evitar acoplar pruebas básicas a infraestructura externa.

El `build` del microservicio quedó pasando correctamente con Gradle.

## 12. Consideraciones de diseño orientadas a microservicios

- El servicio es autónomo y encapsula autenticación.
- Usa tokens para interoperar sin sesión compartida.
- Expone contratos HTTP claros mediante DTOs.
- Puede ser consumido por frontend, BFF o gateway.
- Los roles y permisos viajan en el JWT, reduciendo consultas repetidas en componentes externos.
- El logout se resolvió con blacklist para compensar la naturaleza stateless del JWT.

## 13. Posibles mejoras futuras

- Incorporar una estrategia versionada para crear tablas de usuarios, refresh tokens y blacklist.
- Añadir pruebas unitarias e integración por endpoint.
- Implementar auditoría de accesos.
- Añadir confirmación de email.
- Añadir recuperación de contraseña.
- Integrar validación de permisos por endpoint mediante `@PreAuthorize`.
- Separar permisos por dominio si el sistema crece.

## 14. Resumen final

`auth-service` quedó implementado como un microservicio de autenticación completo, con:

- repository
- model
- dto
- service
- controller
- roles
- permisos
- password hashing
- JWT
- refresh token
- validación de token
- logout

La solución está alineada con la lógica funcional descrita en el `README.md` y preparada para integrarse con el resto de la arquitectura cuando los otros componentes del sistema evolucionen.
