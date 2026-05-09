# auth-service

Microservicio backend **solo autenticación**: **registro** y **login** (JWT). Base de datos PostgreSQL **`user_db`**.

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/health` | Estado del servicio |
| POST | `/api/auth/register` | Registro (`email`, `password`, `name` opcional) → **201** |
| POST | `/api/auth/login` | Login (`email`, `password`) → **200** + `{ user, token }` |

JSON en **snake_case** (`created_at`, etc.).

## Local (sin Docker)

1. Crear la base `user_db` en PostgreSQL (usuario/contraseña por defecto `admin`/`admin` o los que configures).
2. `cd backend/auth-service`
3. `./gradlew bootRun` (Windows: `gradlew.bat bootRun`)

Puerto: **8081**.

Variables útiles: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET` (≥ 32 caracteres), `APP_CORS_ALLOWED_ORIGINS`.

## Docker Compose

Desde la raíz del repo: `docker compose up --build` incluye **postgres-auth** (`user_db`) y **auth-service**.
