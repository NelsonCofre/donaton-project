# Patrones de Diseño del Proyecto

Este archivo resume los patrones de diseño y patrones arquitectónicos que están presentes en el proyecto hasta ahora, junto con la parte del código donde se observan.

## 1. Repository Pattern

Se usa para encapsular el acceso a base de datos mediante Spring Data JPA. Este patrón evita que la lógica de negocio dependa directamente de consultas SQL o detalles de persistencia, dejando esa responsabilidad concentrada en repositorios.

Se encuentra en:

- `backend/ms-auth/src/main/java/com/donaton/auth/repository/UserAccountRepository.java`
- `backend/ms-auth/src/main/java/com/donaton/auth/repository/RefreshTokenRepository.java`
- `backend/ms-auth/src/main/java/com/donaton/auth/repository/BlacklistedTokenRepository.java`
- `backend/ms-donation/src/main/java/com/donaton/donation/repository/DonationRepository.java`

## 2. Service Layer Pattern

Se usa para separar la lógica de negocio de los controladores y de la persistencia. En este proyecto permite que reglas como autenticación, generación de tokens, revocación o CRUD de donaciones no queden mezcladas con HTTP ni con acceso a base de datos.

Se encuentra en:

- `backend/ms-auth/src/main/java/com/donaton/auth/service/AuthService.java`
- `backend/ms-auth/src/main/java/com/donaton/auth/service/RefreshTokenService.java`
- `backend/ms-auth/src/main/java/com/donaton/auth/service/TokenBlacklistService.java`
- `backend/ms-donation/src/main/java/com/donaton/donation/service/DonationService.java`
- `backend/ms-donation/src/main/java/com/donaton/donation/service/DonationServiceImpl.java`

## 3. Controller Pattern

Se usa para manejar las solicitudes HTTP y delegar el trabajo a otras capas. Su función principal es recibir requests, validar la entrada a nivel web y entregar la ejecución a servicios o clientes, manteniendo liviana la capa de entrada.

Se encuentra en:

- `backend/ms-auth/src/main/java/com/donaton/auth/controller/AuthController.java`
- `backend/ms-donation/src/main/java/com/donaton/donation/controller/DonationController.java`
- `backend/bff-service/src/main/java/com/donaton/bff/controller/AuthBffController.java`
- `backend/bff-service/src/main/java/com/donaton/bff/controller/DonationBffController.java`

## 4. DTO Pattern

Se usa para separar los contratos de entrada y salida del modelo interno. Esto ayuda a controlar qué datos se exponen, adaptar estructuras según el consumidor y evitar que las entidades del dominio se usen directamente como contrato HTTP.

Se encuentra en:

- `backend/ms-auth/src/main/java/com/donaton/auth/dto/`
- `backend/ms-donation/src/main/java/com/donaton/donation/dto/`
- `backend/bff-service/src/main/java/com/donaton/bff/dto/api/`
- `backend/bff-service/src/main/java/com/donaton/bff/dto/auth/`
- `backend/bff-service/src/main/java/com/donaton/bff/dto/donation/`

## 5. Mapper Pattern

Se usa para transformar datos entre contratos del frontend y contratos internos de los microservicios. En el proyecto es especialmente importante en el BFF, porque allí se adapta el formato que espera el frontend al formato que manejan `auth-service` y `donation-service`.

Se encuentra en:

- `backend/bff-service/src/main/java/com/donaton/bff/mapper/AuthMapper.java`
- `backend/bff-service/src/main/java/com/donaton/bff/mapper/DonationMapper.java`

## 6. Backend for Frontend (BFF)

Se usa para centralizar una API pensada para el frontend y desacoplarlo de los microservicios internos. Este patrón permite que el navegador no hable directamente con cada servicio, sino con una capa intermedia que adapta contratos, aplica validaciones y simplifica la integración.

Se encuentra en:

- `backend/bff-service/`

Piezas principales:

- `backend/bff-service/src/main/java/com/donaton/bff/controller/`
- `backend/bff-service/src/main/java/com/donaton/bff/client/`
- `backend/bff-service/src/main/java/com/donaton/bff/mapper/`

## 7. Filter Pattern

Se usa para interceptar solicitudes antes de que lleguen al controlador, especialmente en autenticación y validación de token. Su valor en este proyecto es que permite resolver seguridad de forma transversal sin repetir esa lógica en cada endpoint.

Se encuentra en:

- `backend/ms-auth/src/main/java/com/donaton/auth/security/JwtAuthenticationFilter.java`
- `backend/bff-service/src/main/java/com/donaton/bff/security/JwtAuthFilter.java`

## 8. Context / Provider Pattern

Se usa en React para compartir el estado de autenticación entre componentes. Gracias a este patrón, la aplicación puede saber globalmente si un usuario está autenticado, acceder al token y ejecutar acciones como login o logout sin pasar props manualmente por muchos niveles.

Se encuentra en:

- `frontend/src/shared/lib/auth-context.ts`
- `frontend/src/shared/lib/authContext.tsx`
- `frontend/src/shared/lib/useAuth.ts`

## 9. Protected Route Pattern

Se usa para proteger rutas del frontend y redirigir al login cuando el usuario no está autenticado. En este proyecto controla el acceso a la sección de donaciones y apoya la navegación segura desde la interfaz.

Se encuentra en:

- `frontend/src/app/routes/ProtectedRoute.tsx`

## 10. Layered Architecture

El backend está organizado por capas: controlador, servicio, repositorio, modelo, DTO, configuración y excepciones. Más que un patrón puntual, es una forma de estructurar el código para que cada responsabilidad quede en una parte clara del sistema.

Se encuentra en:

- `backend/ms-auth/src/main/java/com/donaton/auth/`
- `backend/ms-donation/src/main/java/com/donaton/donation/`

## 11. Builder Pattern

Se usa para construir objetos de manera más clara, apoyado por Lombok. En este caso ayuda a crear entidades y respuestas con una sintaxis más legible, especialmente cuando hay varios campos que asignar.

Se encuentra en:

- `backend/ms-donation/src/main/java/com/donaton/donation/model/Donation.java`
- `backend/ms-donation/src/main/java/com/donaton/donation/service/DonationServiceImpl.java`

## 12. Exception Handler Pattern

Se usa para centralizar el manejo de errores y responder de forma consistente. Esto evita duplicar manejo de excepciones en cada controlador y hace que las respuestas de error sean más uniformes para frontend y clientes.

Se encuentra en:

- `backend/ms-auth/src/main/java/com/donaton/auth/exception/ApiExceptionHandler.java`
- `backend/ms-donation/src/main/java/com/donaton/donation/exception/GlobalExceptionHandler.java`
- `backend/bff-service/src/main/java/com/donaton/bff/exception/BffExceptionHandler.java`

## Resumen

Los patrones más visibles en el proyecto hasta ahora son:

- Repository
- Service Layer
- Controller
- DTO
- Mapper
- BFF
- Filter
- Context / Provider
- Protected Route
- Layered Architecture
- Builder
- Exception Handler
