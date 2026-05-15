# Donation Service (Donaton)

Microservicio **Spring Boot** del caso Donaton: **CRUD de donaciones** (recurso, cantidad, origen, fecha, centro de acopio), persistencia en **PostgreSQL** con **JPA** y **Flyway**, exposición **REST** y despliegue local con **Docker Compose** (sin API Gateway ni pgAdmin).

---

## Inicio rápido

Desde la carpeta **`backend/donation-service`**:

| Objetivo | Comando | Resultado |
|----------|---------|-----------|
| **Ejecutar** (API + Postgres en Docker) | `docker compose up --build` | API en `http://localhost:8082` |
| **Probar** (tests automatizados) | `gradlew.bat test` (Windows) o `./gradlew test` | Tests JUnit/MockMvc con H2 |
| **Probar** (CRUD manual + BD) | Ver [Pruebas manuales y consola Docker](#pruebas-manuales-y-consola-docker) | `curl` + `psql` vía `docker compose exec` |

Este microservicio usa **JDK 21** (ver `gradle.properties`). Los demás servicios del repo pueden usar otra versión.

---

## Índice

1. [Requisitos](#requisitos)
2. [API REST](#api-rest-apiv1donations)
3. [Ejecutar el microservicio](#ejecutar-el-microservicio)
4. [Probar el microservicio](#probar-el-microservicio)
5. [Despliegue con Docker](#despliegue-con-docker)
6. [Migraciones (Flyway)](#migraciones-flyway)
7. [Configuración y variables de entorno](#configuración-y-variables-de-entorno)
8. [Estructura del código](#estructura-del-código)
9. [Inventario de archivos](#inventario-de-archivos)
10. [Orden sugerido de ejecución (checklist)](#orden-sugerido-de-ejecución-checklist)
11. [Git y entrega (Parcial 2)](#git-y-entrega-parcial-2)
12. [Registro de avances (bitácora)](#registro-de-avances-bitácora)

---

## Requisitos

- **Docker** y **Docker Compose** (recomendado para ejecutar API + PostgreSQL), **o**
- **JDK 21** + **Gradle Wrapper** (`gradlew`) para desarrollo y tests locales de **este** microservicio.

Gradle usa **toolchain Java 21** (`gradle.properties` → `jdkToolchainVersion=21`) y el plugin **Foojay** en `settings.gradle` para provisionar el JDK si no está instalado. Si falla la descarga (red/certificados), instala [Eclipse Temurin 21](https://adoptium.net/) y selecciónalo en el IDE (**Java: Configure Java Runtime**).

### Errores en el IDE (Cursor / VS Code)

Si ves muchos errores como **`java.lang.SuppressWarnings cannot be resolved`**, **`Object() is undefined`** o avisos en **`@RequiredArgsConstructor`**, el código puede estar bien y fallar solo el **Language Server de Java** (no encuentra el **JDK** o no importó Gradle/Lombok).

1. Instala un **JDK 21 completo** (no solo JRE): [Temurin 21](https://adoptium.net/).
2. En Cursor: `Ctrl+Shift+P` → **Java: Configure Java Runtime** → elige **JDK 21** como predeterminado.
3. `Ctrl+Shift+P` → **Java: Clean Java Language Server Workspace** → **Restart and delete**.
4. Abre la carpeta del repo **`donaton-project`** (no solo un archivo suelto) y espera a que Gradle importe `backend/donation-service`.
5. Comprueba en terminal (debe pasar):

   ```bash
   cd backend/donation-service
   gradlew.bat compileJava
   ```

6. El workspace incluye en `.vscode/settings.json`: import Gradle automático y **Lombok** habilitado (`java.jdt.ls.lombokSupport.enabled`).

Extensiones recomendadas: **Extension Pack for Java** (Microsoft). Sin JDK válido en el IDE, Lombok parece “roto” aunque `build.gradle` ya declare `annotationProcessor` para Lombok.

---

## API REST (`/api/v1/donations`)

| Método | Ruta | Descripción | Código HTTP |
|--------|------|-------------|-------------|
| `POST` | `/api/v1/donations` | Crear donación | `201` |
| `GET` | `/api/v1/donations` | Listar todas | `200` |
| `GET` | `/api/v1/donations/{id}` | Obtener por id | `200` / `404` |
| `PUT` | `/api/v1/donations/{id}` | Actualizar (reemplazo completo) | `200` / `404` |
| `DELETE` | `/api/v1/donations/{id}` | Eliminar | `204` / `404` |

Cuerpo JSON de entrada/salida (campos):

```json
{
  "resourceName": "Kit de higiene",
  "quantity": 50,
  "origin": "Municipalidad Central",
  "donationDate": "2026-05-14",
  "warehouseName": "Centro de acopio Norte"
}
```

- Validación incorrecta (p. ej. `quantity` &lt; 1): **400**
- Id inexistente: **404**

---

## Ejecutar el microservicio

### Con Docker (recomendado)

1. Opcional: archivo **`.env`** en esta carpeta (no se versiona) para puertos/credenciales; si no existe, se usan los defaults de `docker-compose.yml`.
2. Levantar stack:

   ```bash
   docker compose up --build
   ```

   En segundo plano: `docker compose up --build -d`

3. **API:** `http://localhost:8082` (variable `DONATION_SERVICE_PORT` en host).
4. **Postgres en el host:** puerto **5436** por defecto (`POSTGRES_PUBLISH_PORT`).

### Sin Docker (Gradle + Postgres local)

PostgreSQL accesible con base `donation_db` y credenciales por defecto `donation` / `donation` (ver `application.properties`).

```bash
./gradlew bootRun
```

Windows: `gradlew.bat bootRun`  
Requiere **JDK 21** alineado con `gradle.properties`.

Flyway aplica migraciones al arrancar contra PostgreSQL.

---

## Probar el microservicio

### Tests automatizados

```bash
./gradlew test
```

Windows: `gradlew.bat test`  
Usa el JDK 21 del toolchain (sin parámetros extra si `gradle.properties` está en 21).

- Perfil **`test`**: H2 en memoria, `spring.flyway.enabled=false`, `ddl-auto=create-drop`.
- Incluye test de contexto y **integración API** (`DonationApiIntegrationTest`).

### Pruebas manuales y consola Docker

Guía para demostrar el CRUD con el stack levantado y comprobar datos en Postgres **sin pgAdmin**.

#### 1. Preparación

```bash
docker compose up --build -d
docker compose ps
```

URL base: `http://localhost:8082`

```powershell
# PowerShell
$base = "http://localhost:8082/api/v1/donations"
```

```bash
# bash
BASE=http://localhost:8082/api/v1/donations
```

#### 2. Flujo CRUD con `curl`

Sustituye `1` por el `id` devuelto en el `POST`.

```bash
# POST — 201
curl -s -X POST "$BASE" -H "Content-Type: application/json" \
  -d '{"resourceName":"Agua embotellada","quantity":120,"origin":"Empresa XYZ","donationDate":"2026-05-14","warehouseName":"Centro Norte"}'

# GET list — 200
curl -s "$BASE"

# GET by id — 200
curl -s "$BASE/1"

# PUT — 200
curl -s -X PUT "$BASE/1" -H "Content-Type: application/json" \
  -d '{"resourceName":"Agua embotellada","quantity":80,"origin":"Empresa XYZ","donationDate":"2026-05-15","warehouseName":"Centro Sur"}'

# DELETE — 204
curl -s -o /dev/null -w "%{http_code}" -X DELETE "$BASE/1"
```

PowerShell (crear):

```powershell
Invoke-RestMethod -Method Post -Uri $base -ContentType "application/json" `
  -Body '{"resourceName":"Agua embotellada","quantity":120,"origin":"Empresa XYZ","donationDate":"2026-05-14","warehouseName":"Centro Norte"}'
```

#### 3. Consola Docker / Postgres

```bash
docker compose exec postgres-donation psql -U donation -d donation_db -c "\dt"
docker compose exec postgres-donation psql -U donation -d donation_db \
  -c "SELECT id, resource_name, quantity, origin, donation_date, warehouse_name FROM donations ORDER BY id;"
docker compose exec postgres-donation psql -U donation -d donation_db \
  -c "SELECT installed_rank, version, description, success FROM flyway_schema_history ORDER BY installed_rank;"
```

Sesión interactiva: `docker compose exec -it postgres-donation psql -U donation -d donation_db` (salir: `\q`).

#### 4. Logs

```bash
docker compose logs -f donation-service
```

---

## Despliegue con Docker

### Diseño de contenedores

Dos servicios en **`docker-compose.yml`** (proyecto Compose: `donation-service-stack`):

| Servicio | Rol |
|----------|-----|
| `postgres-donation` | PostgreSQL 16, volumen `donation-pg-data`, healthcheck `pg_isready` |
| `donation-service` | API Spring Boot (imagen desde `Dockerfile`) |

- **Red:** `donation-net` (bridge); JDBC usa host `postgres-donation`.
- **Arranque:** la app espera Postgres **healthy** (`depends_on`).
- **Runtime:** JAR como usuario **`nobody`** (no root).

### Dockerfile (imagen de la app)

| Etapa | Base | Acción |
|-------|------|--------|
| **builder** | `eclipse-temurin:21-jdk-alpine` | Gradle Wrapper → `dependencies` → `bootJar -x test` |
| **runtime** | `eclipse-temurin:21-jre-alpine` | Solo `app.jar`, `EXPOSE 8082`, `java -jar` |

Solo imagen de la app:

```bash
docker build -t donation-service:local .
```

### Docker Compose (comandos)

```bash
docker compose up --build -d    # levantar
docker compose ps             # estado
docker compose logs -f donation-service
docker compose down           # parar (conserva volumen)
docker compose down -v        # parar y borrar datos Postgres
```

---

## Migraciones (Flyway)

- Scripts: `src/main/resources/db/migration/`
- **`V1__create_donations_table.sql`:** tabla `donations` + `CHECK (quantity > 0)`
- **`V2__index_donations_donation_date.sql`:** índice por `donation_date`
- JPA: `ddl-auto=validate` (Hibernate no altera el esquema en PostgreSQL)
- Historial: tabla `flyway_schema_history`

Si editas una migración ya aplicada en Docker, usa `docker compose down -v` y vuelve a levantar.

---

## Configuración y variables de entorno

Enfoque **12-factor**: configuración por **variables de entorno** (`application.properties` con placeholders; Compose inyecta valores en contenedor). Archivo **`.env`** opcional en esta carpeta (en `.gitignore`).

### Aplicación (Spring)

| Variable | Default local |
|----------|----------------|
| `SPRING_APPLICATION_NAME` | `donation-service` |
| `SERVER_PORT` | `8082` |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/donation_db` |
| `SPRING_DATASOURCE_USERNAME` | `donation` |
| `SPRING_DATASOURCE_PASSWORD` | `donation` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `validate` |
| `SPRING_FLYWAY_ENABLED` | `true` |
| `SPRING_FLYWAY_LOCATIONS` | `classpath:db/migration` |
| `LOGGING_LEVEL_ROOT` | `INFO` |

### Compose / Postgres

| Variable | Default |
|----------|---------|
| `POSTGRES_USER` / `POSTGRES_PASSWORD` / `POSTGRES_DB` | `donation` / `donation` / `donation_db` |
| `POSTGRES_PUBLISH_PORT` | `5436` |
| `DONATION_SERVICE_PORT` | `8082` |
| `SERVER_PORT` (puerto interno del contenedor app) | `8082` |

---

## Estructura del código

| Capa | Paquete / rol |
|------|----------------|
| `entity` | Modelo JPA `Donation` |
| `repository` | `DonationRepository` (Spring Data) |
| `service` | `DonationService` + `DonationServiceImpl` |
| `controller` | REST `/api/v1/donations` |
| `dto` | `DonationRequestDto`, `DonationResponseDto` |
| `config` | `SecurityConfiguration` (endpoints abiertos en encargo) |
| `exception` | `ResourceNotFoundException`, `GlobalExceptionHandler` |

**Seguridad:** sin autenticación en esta versión académica; en producción integrar JWT/OAuth2.

**Patrones:** Repository (persistencia), DTO (contrato API), capa de servicio (lógica de negocio).

---

## Inventario de archivos

| Área | Ubicación principal |
|------|---------------------|
| API | `controller/DonationController.java` |
| Dominio / BD | `entity/`, `repository/`, `db/migration/` |
| Negocio | `service/` |
| Contratos | `dto/` |
| Config | `application.properties`, `config/SecurityConfiguration.java` |
| Contenedores | `Dockerfile`, `docker-compose.yml` |
| Build | `build.gradle`, `settings.gradle`, `gradlew` |
| Tests | `src/test/java/...`, `application-test.properties` |

---

## Orden sugerido de ejecución (checklist)

Secuencia recomendada para implementar y validar `donation-service`. Marca cada paso al completarlo (útil para el equipo y la defensa oral).

| # | Paso | Qué validar | Estado |
|---|------|-------------|--------|
| 1 | **Alinear alcance** | CRUD, 5 endpoints, capas (entity, repository, service, controller, DTO) | Hecho |
| 2 | **Inventario** | Revisar qué archivos existen y qué falta | Hecho — [Inventario](#inventario-de-archivos) |
| 3 | **Diseño de contenedores** | Decisión: Postgres + app en Compose (no monolito) | Hecho — [Despliegue con Docker](#despliegue-con-docker) |
| 4 | **Variables 12-factor** | `application.properties` + Compose + `.env` opcional | Hecho — [Configuración](#configuración-y-variables-de-entorno) |
| 5 | **Migraciones Flyway** | `V1` tabla, `V2` índice, `ddl-auto=validate` | Hecho — [Migraciones](#migraciones-flyway) |
| 6 | **Dockerfile** | Build multi-etapa, JAR, Java 21 | Hecho — [Dockerfile](#dockerfile-imagen-de-la-app) |
| 7 | **Docker Compose** | Red, volumen, healthcheck, `depends_on` | Hecho — [Docker Compose](#docker-compose-comandos) |
| 8 | **Pruebas manuales** | `curl` + `psql` sin pgAdmin | Hecho — [Pruebas manuales](#pruebas-manuales-y-consola-docker) |
| 9 | **README (Parcial 2)** | Instrucciones ejecutar y probar el microservicio | Hecho — este documento |
| 10 | **Git y entrega** | Rama, PR, pantallazos en GitHub | Pendiente — [Git y entrega](#git-y-entrega-parcial-2) |
| 11 | **Verificación final** | `gradlew test` y `docker compose up --build` + CRUD manual | Pendiente de evidencia en PR |

**Comandos de cierre (paso 11):**

```bash
gradlew.bat test
docker compose up --build -d
docker compose ps
```

Luego ejecutar el flujo de [pruebas manuales](#pruebas-manuales-y-consola-docker) y adjuntar capturas en el PR (paso 10).

---

## Git y entrega (Parcial 2)

Este microservicio se versiona en **GitHub** dentro del repositorio del proyecto. A nivel de código Java **no hay pasos extra** para la entrega: el componente ya está listo; lo pendiente es el flujo de **rama, PR y evidencias (pantallazos)** acordado con el equipo.

### Flujo recomendado (equipo)

1. Trabajar en una rama de feature (p. ej. `feature/donation-service` o la que defina el equipo).
2. Commits con mensajes claros sobre el microservicio (`donation-service`).
3. Abrir un **Pull Request** hacia la rama principal acordada (`main`, `develop`, etc.).
4. Adjuntar en el PR **capturas de pantalla** que evidencien:
   - stack Docker levantado (`docker compose ps` o logs);
   - prueba manual del CRUD (Postman, `curl` o similar);
   - consulta en Postgres vía `docker compose exec ... psql` (sin pgAdmin).
5. Merge tras revisión del equipo.

### Registro de este entregable (completar al abrir el PR)

| Campo | Valor |
|-------|--------|
| **Rama** | _pendiente — indicar al crear el PR_ |
| **Pull Request** | _pendiente — enlace al PR en GitHub_ |
| **Pantallazos** | _pendiente — se adjuntarán en el PR (Docker, API, `psql`)_ |
| **Fecha de cierre PR** | _pendiente_ |

> Cuando tengas el enlace del PR y las capturas, actualiza esta tabla (o pide al equipo que lo haga en la misma rama del README).

### Entrega global del curso (fuera de esta carpeta)

El **ZIP** de la Parcial 2 y el archivo **`repositorios.txt`** (enlaces a repos) suelen armarse en la **raíz del encargo / monorepo**, no solo en `donation-service`. Este README documenta el microservicio; el índice de repositorios lo lleva el entregable grupal.

---

## Registro de avances (bitácora)

| Fecha | Avance |
|-------|--------|
| **2026-05-14** | CRUD completo (5 endpoints), Flyway V1, Security abierta, Docker inicial, tests MockMvc + H2. |
| **2026-05-15** | Toolchain Java (Foojay), `-PjdkToolchainVersion` opcional. |
| **2026-05-26** | **JDK 21** solo en `donation-service`: `gradle.properties`, default en `build.gradle`, Docker Temurin 21, README actualizado. Sin cambios en `auth-service` ni otros microservicios. |
| **2026-05-16–17** | Inventario README, diseño contenedores, red `donation-net`, usuario `nobody`. |
| **2026-05-18** | Configuración 12-factor en `application.properties` y Compose. |
| **2026-05-19** | Migración Flyway V2 (índice por fecha). |
| **2026-05-20–21** | Dockerfile optimizado (caché Gradle); Compose (`restart`, `start_period`, nombre de proyecto). |
| **2026-05-22** | Pruebas manuales documentadas (`curl`, `psql`, códigos HTTP). |
| **2026-05-23** | **README Parcial 2 (paso 9):** reorganización con **inicio rápido**, **índice**, secciones claras de **ejecutar** y **probar**, despliegue Docker unificado y cumplimiento del encargo (instrucciones de uso del microservicio). |
| **2026-05-24** | **Git y entrega (paso 10):** sección **Git y entrega (Parcial 2)** con flujo de rama/PR/pantallazos y tabla pendiente de completar al abrir el PR. Sin cambios de código Java en este paso. |
| **2026-05-25** | **Checklist (paso 11):** sección **Orden sugerido de ejecución** con tabla de pasos 1–11, enlaces a secciones del README y comandos de verificación final. Pasos 1–9 marcados hechos; paso 10 (PR/pantallazos) pendiente. |

*Añade filas al cerrar PRs o entregas. Actualiza la tabla de Git cuando tengas enlace del PR y capturas.*
