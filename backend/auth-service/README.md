# auth-service

Microservicio backend **solo autenticación**: **registro** y **login** (JWT). Base de datos PostgreSQL **`auth-db`**.

## Esquema (Flyway)

- **`V1__init_tables.sql`**: creación de tablas (`users`, `roles`, `permisos`, `usuarios`, tablas puente, `tokens`).
- **`V2__seed_init.sql`**: datos iniciales (roles de ejemplo).

Hibernate **`ddl-auto=none`**; el esquema lo aplica **Flyway** al arrancar.

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/health` | Estado del servicio |
| POST | `/api/auth/register` | Registro (`email`, `password`, **`name` obligatorio**) → **201** |
| POST | `/api/auth/login` | Login (`email`, `password`) → **200** + `{ user, token }` |

JSON en **snake_case** (`created_at`, etc.).

## Local (sin Docker)

1. Tener PostgreSQL accesible y crear la base **`auth-db`** (o usar Compose solo para Postgres).
2. Por defecto en `application.properties`: host **`localhost`**, puerto **`5435`**, usuario **`root`**, contraseña **`Informatica.25`** (ajústalos con variables de entorno si usas otros valores).
3. `cd backend/auth-service`
4. `./gradlew bootRun` (Windows: `gradlew.bat bootRun`)

Puerto: **8081**.

Variables útiles: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET` (≥ 32 caracteres), `APP_CORS_ALLOWED_ORIGINS`.

## Docker Compose

Desde la raíz de **donaton-project**: `docker compose up --build` levanta **postgres-auth** (crea la base vacía **`auth-db`**) y **auth-service**. Flyway crea tablas y datos semilla al iniciar el microservicio.

- Credenciales Postgres de auth: usuario **`root`**, contraseña **`Informatica.25`** (`docker-compose.yml`).
- Si cambiaste migraciones Flyway o el nombre de la base respecto a un despliegue anterior, puede hacer falta **`docker compose down -v`** para recrear el volumen y evitar conflictos con `flyway_schema_history` o checksums antiguos.
