# 📦 Donaton - Arquitectura de Microservicios (Informe Evaluación 2)

---

## ⚙️ Ejecutar con Docker Compose (raíz del repositorio)

El **Backend for Frontend (BFF)** está en `backend/bff-service`: expone la API que consume el cliente (`/api/auth/*`, `/api/donations`), valida el JWT donde aplica y actúa como cliente HTTP hacia los microservicios.

### Requisitos

- Docker Desktop o Docker Engine con Docker Compose habilitado.
- Ejecutar los comandos desde la carpeta raíz del repositorio.

### Levantar el entorno

Desde la raíz del proyecto:

```bash
docker compose up --build
```

En segundo plano:

```bash
docker compose up --build -d
```

### Servicios y puertos

| Servicio | Puerto en el host | Rol |
|----------|-------------------|-----|
| `frontend` | **5173** | UI estática servida con Nginx (`http://localhost:5173`) |
| `bff-service` | **8080** | BFF Spring Boot: única API HTTP para el navegador |
| `auth-service` | **8081** | Autenticación y emisión de JWT |
| `donation-service` | **8082** | CRUD de donaciones |
| `logistics-service` | **8084** | CRUD de centros de acopio, inventario y envíos |
| `postgres-auth` | **5435** | Base de datos del auth-service |
| `postgres-donation` | **5436** | Base de datos del donation-service |
| `postgres-logistics` | **5437** | Base de datos del logistics-service |

El código fuente de los microservicios vive en `backend/ms-auth`, `backend/ms-donation` y `backend/ms-logistic`; en `docker-compose.yml` conservan los nombres de servicio `auth-service`, `donation-service` y `logistics-service` para la red interna de Docker.

### Flujo recomendado

1. Ejecutar `docker compose up --build`.
2. Abrir el frontend en `http://localhost:5173`.
3. El frontend consume el BFF en `http://localhost:8080`.
4. El BFF reenvía las solicitudes a:
   - `auth-service` para login, registro y validación de token.
   - `donation-service` para el CRUD de donaciones.

**Logística (`logistics-service`):** el servicio ya está disponible en Docker en el puerto **8084**, pero el BFF aún no lo integra. Hasta entonces, se prueba directamente contra la API del microservicio (ver sección siguiente).

### Levantar solo logística

Desde la raíz del proyecto:

```bash
docker compose up --build postgres-logistics logistics-service
```

En segundo plano:

```bash
docker compose up --build -d postgres-logistics logistics-service
```

Comprobar que responde:

```bash
curl http://localhost:8084/api/v1/logistics/collection-centers
```

Respuesta esperada al iniciar: `[]` (lista vacía).

### Comandos útiles

Revisar el estado de los contenedores:

```bash
docker compose ps
```

Ver logs de un servicio:

```bash
docker compose logs -f bff-service
docker compose logs -f logistics-service
```

Detener el entorno:

```bash
docker compose down
```

Detener y eliminar volúmenes:

```bash
docker compose down -v
```

**Variable del front (build):** la imagen del frontend inyecta la URL del BFF en tiempo de **build** con `VITE_API_BASE_URL` (por defecto `http://localhost:8080` en `docker-compose.yml`). Si cambias el host o puerto del BFF, reconstruye el servicio `frontend` con el valor correcto para que el navegador pueda llamarlo.

**CORS:** el BFF admite orígenes configurables (`APP_CORS_ALLOWED_ORIGINS`); alinealo con la URL desde la que sirves el frontend.

---

## 🧩 Descripción del Proyecto

Donaton es una plataforma orientada a la gestión de ayuda humanitaria, permitiendo registrar donaciones, necesidades en terreno y coordinar la logística de distribución.

El sistema implementa una arquitectura de **microservicios contenedorizados con Docker**, incorporando un **Backend for Frontend (BFF)** y un **API Gateway (KrakenD)**.
El frontend está desarrollado en **React + TypeScript** y el backend en **Java 21 con Spring Boot**, utilizando **Gradle (Groovy DSL)** como herramienta de construcción.

---

# 🏗️ Arquitectura del Sistema

## 🔷 Flujo de comunicación

**Documentación / objetivo:** Frontend → BFF → API Gateway (KrakenD) → Microservicios.

**Implementación actual en `docker-compose.yml`:** el navegador solo habla con el **frontend** y el **BFF**; el BFF llama por HTTP a **auth-service** y **donation-service**. **logistics-service** está definido en el compose y expone su API en el puerto **8084**, pero aún no es consumido por el BFF ni por el frontend (sin KrakenD en este compose; el gateway puede incorporarse después).

---

## 🧱 Patrones Arquitectónicos

### ✔ Microservicios

Separación por dominios:

- auth-service
- donations-service
- needs-service
- logistics-service

---

### ✔ API Gateway Pattern (KrakenD)

- Enrutamiento de solicitudes
- Seguridad (validación JWT)
- Centralización de acceso

---

### ✔ Backend for Frontend (BFF)

- Orquesta múltiples microservicios
- Reduce llamadas desde frontend
- Adapta respuestas a la UI
- Utiliza **WebClient** para comunicación entre servicios

---

### ✔ Database per Service

Cada microservicio posee su propia base de datos independiente, evitando el acoplamiento.

---

# 🐳 Contenerización con Docker

Cada componente se ejecuta en su propio contenedor:

- Frontend
- BFF
- KrakenD
- Microservicios
- Bases de datos

Actualmente el `docker-compose.yml` raíz levanta el stack integrado local con:

- frontend
- bff-service
- auth-service
- postgres-auth
- donation-service
- postgres-donation
- logistics-service
- postgres-logistics

Con `docker compose up --build` se levantan todos los servicios anteriores. Para probar solo logística, usar `docker compose up --build postgres-logistics logistics-service`.

---

# 🗄️ Bases de Datos por Microservicio

Se implementa el patrón **Database per Service**, donde cada microservicio tiene su propia base de datos en Docker.

## ✔ Distribución

- auth-service → auth-db
- donations-service → donations-db
- needs-service → needs-db
- logistics-service → logistics-db

---

## 🔗 Conexión

Los microservicios se conectan a su base de datos mediante el nombre del contenedor:

```properties
spring.datasource.url=jdbc:postgresql://auth-db:5432/auth-db
```

---

## ⚠️ Restricciones

- No se comparten bases de datos
- No existen relaciones entre bases de datos
- Comunicación solo vía HTTP

---

## ✔ Beneficios

- Bajo acoplamiento
- Escalabilidad independiente
- Aislamiento de fallos

---

# 🛠️ Migraciones de Base de Datos con Flyway

El proyecto utiliza **Flyway** para gestionar las migraciones de base de datos de cada microservicio.

Flyway permite versionar los cambios de la base de datos mediante archivos SQL, evitando crear tablas manualmente en PostgreSQL.  
Al iniciar cada microservicio, Spring Boot detecta Flyway, busca las migraciones pendientes y las ejecuta automáticamente sobre la base de datos correspondiente.

---

## ✔ ¿Para qué usamos Flyway?

Flyway se utiliza para:

- Crear las tablas iniciales de cada microservicio.
- Versionar los cambios de la base de datos.
- Evitar diferencias entre las bases de datos de los integrantes del equipo.
- Permitir que el proyecto sea reproducible localmente.
- Mantener controlado el historial de cambios de la base de datos.

---

## 🔄 Flujo de migración

```txt
Docker Compose levanta PostgreSQL
↓
Spring Boot inicia el microservicio
↓
Flyway detecta los archivos SQL
↓
Flyway ejecuta las migraciones pendientes
↓
PostgreSQL crea o actualiza las tablas
↓
Flyway registra el historial en flyway_schema_history
```

# 🔄 Comunicación entre Servicios

La comunicación se realiza mediante **HTTP REST** utilizando **Spring WebClient**.

## ✔ WebClient

- Cliente HTTP no bloqueante
- Permite llamadas asincrónicas
- Usado principalmente en el BFF

---

## 📌 Ejemplo

```java
WebClient webClient = WebClient.create("http://donations-service");

webClient.get()
    .uri("/donaciones")
    .retrieve()
    .bodyToMono(String.class);
```

---

# 🧠 Patrones de Diseño

### ✔ Repository Pattern

Encapsula acceso a datos.

### ✔ Factory Method

Creación de objetos.

### ✔ Circuit Breaker (Resilience4j)

Manejo de fallos entre servicios.

---

# 🔐 Microservicio de Autenticación

## 📌 Responsabilidad

Gestión de usuarios y autenticación mediante JWT.

## 🧩 Entidad

### Usuario

| Atributo   | Tipo               |
| ---------- | ------------------ |
| id_usuario | PK                 |
| email      | String             |
| password   | String             |
| rol        | ENUM (ADMIN, USER) |

## 🔗 Relaciones

- Usuario independiente
- Rol como ENUM

---

# 🎁 Microservicio de Donaciones

## 📌 Responsabilidad

Gestión del ingreso de recursos.

## 🧩 Entidades

### Donante

| Atributo   | Tipo   |
| ---------- | ------ |
| id_donante | PK     |
| nombre     | String |
| contacto   | String |

---

### Recurso

| Atributo   | Tipo   |
| ---------- | ------ |
| id_recurso | PK     |
| tipo       | String |

---

### Donacion

| Atributo    | Tipo    |
| ----------- | ------- |
| id_donacion | PK      |
| fecha       | Date    |
| cantidad    | Integer |
| estado      | ENUM    |
| id_donante  | FK      |

---

### Donacion_Recurso

| Atributo    | Tipo |
| ----------- | ---- |
| id          | PK   |
| id_donacion | FK   |
| id_recurso  | FK   |

---

## 🔗 Relaciones

- Donante **1:N** Donacion
- Donacion **N:M** Recurso
- Donacion **1:N** Donacion_Recurso
- Recurso **1:N** Donacion_Recurso

---

# 📍 Microservicio de Necesidades

## 📌 Responsabilidad

Registrar necesidades.

## 🧩 Entidades

### Necesidad

| Atributo     | Tipo    |
| ------------ | ------- |
| id_necesidad | PK      |
| cantidad     | Integer |
| prioridad    | ENUM    |
| estado       | ENUM    |
| id_ubicacion | FK      |

---

### Recurso

| Atributo   | Tipo   |
| ---------- | ------ |
| id_recurso | PK     |
| tipo       | String |

---

### Ubicacion

| Atributo     | Tipo   |
| ------------ | ------ |
| id_ubicacion | PK     |
| direccion    | String |

---

### Necesidad_Recurso

| Atributo     | Tipo |
| ------------ | ---- |
| id           | PK   |
| id_necesidad | FK   |
| id_recurso   | FK   |

---

## 🔗 Relaciones

- Necesidad **N:1** Ubicacion
- Necesidad **N:M** Recurso
- Necesidad **1:N** Necesidad_Recurso
- Recurso **1:N** Necesidad_Recurso

---

# 🚚 Microservicio de Logística

## 📌 Responsabilidad

Gestión de almacenamiento y distribución.

## 🧩 Entidades

### CentroAcopio

| Atributo  | Tipo   |
| --------- | ------ |
| id_centro | PK     |
| nombre    | String |
| ubicacion | String |

---

### Inventario

| Atributo      | Tipo    |
| ------------- | ------- |
| id_inventario | PK      |
| id_centro     | FK      |
| recurso       | String  |
| cantidad      | Integer |

---

### Envio

| Atributo  | Tipo |
| --------- | ---- |
| id_envio  | PK   |
| fecha     | Date |
| estado    | ENUM |
| id_centro | FK   |

---

## 🔗 Relaciones

- CentroAcopio **1:N** Inventario
- CentroAcopio **1:N** Envio
- Inventario **N:1** CentroAcopio
- Envio **N:1** CentroAcopio

---

# 🔄 Git Flow

## 🌿 Ramas

- main → producción
- develop → integración

---

## 🌱 Flujo de trabajo

1. Antes de crear una feature:

```bash
git checkout development
git pull origin development
```

2. Crear rama:

```bash
git checkout -b feature/nombre-feature
```

3. Desarrollar

4. Subir cambios:

```bash
git push origin feature/nombre-feature
```

5. Crear Pull Request:

api/auth/register
api/auth/login


- feature → develop
- Permite revisión de código

6. Merge a develop

7. Cuando esté estable:

- merge develop → main

---

## 🎯 Objetivo

- Trabajo colaborativo
- Control de versiones
- Estabilidad

---

# 🔒 Seguridad

- JWT
- Validación en KrakenD
- Control de roles

---

# 📈 Escalabilidad

Cada microservicio escala de forma independiente.

---

# 🚀 Tecnologías

- Java 21
- Spring Boot 3.3.x
- Gradle (Groovy DSL)
- PostgreSQL
- Docker
- KrakenD
- React
- TypeScript
- WebClient
- Resilience4j
- Flyway

---

# Requerimientos funcionales

Describen qué debe hacer el sistema en relación con la ayuda humanitaria y la gestión operativa de donaciones, necesidades y logística.

## Plataforma general

- Permitir registrar y dar seguimiento a donaciones de recursos.
- Permitir registrar necesidades en terreno asociadas a ubicaciones y recursos.
- Permitir coordinar la logística de almacenamiento y distribución (centros, inventario, envíos).

## Autenticación y usuarios (auth-service)

- Gestionar usuarios con identificación por correo electrónico y contraseña.
- Asignar roles (ADMIN, USER) para distinguir permisos de uso.
- Autenticar credenciales y emitir tokens JWT para sesiones de API.
- Validar el JWT en las rutas protegidas según el diseño de seguridad (BFF y/o API Gateway).

## Donaciones (donation-service)

- Administrar donantes (datos de contacto y nombre).
- Administrar el catálogo de recursos (tipo de recurso).
- Registrar donaciones con fecha, cantidad, estado y relación con el donante.
- Asociar cada donación con uno o más recursos según el modelo de dominio.

## Necesidades (needs-service)

- Registrar necesidades con cantidad, prioridad y estado.
- Vincular necesidades a una ubicación y a los recursos requeridos.

## Logística (logistics-service)

- Gestionar centros de acopio (identificación y ubicación).
- Mantener inventario por centro (recurso y cantidad disponible).
- Registrar envíos con fecha, estado y centro involucrado.

## Cliente web (frontend y BFF)

- Exponer al navegador una API consolidada vía el BFF (por ejemplo rutas de autenticación y donaciones), reduciendo acoplamiento del front con múltiples microservicios.
- Orquestar en el BFF las llamadas HTTP a los microservicios y adaptar las respuestas a las necesidades de la interfaz.

## Integración entre servicios

- Comunicar dominios únicamente por HTTP/REST, sin compartir bases de datos entre microservicios.



# Requerimientos no funcionales

Describen cómo debe comportarse el sistema (calidades, restricciones técnicas y operativas), más allá de las funciones de negocio.

## Arquitectura y despliegue

- Implementar arquitectura de microservicios separada por dominio (autenticación, donaciones, necesidades, logística).
- Contenerizar cada componente con Docker y permitir orquestación local mediante Docker Compose.
- Aplicar el patrón Database per Service: cada microservicio con su propia instancia/base PostgreSQL, sin relaciones entre bases de datos de otros servicios.

## API y experiencia de cliente

- Utilizar un Backend for Frontend (BFF) como punto de entrada HTTP para el navegador, con CORS configurable según el origen del frontend.
- Contemplar un API Gateway (KrakenD) para enrutamiento centralizado y validación de JWT a nivel de entrada (según el despliegue elegido).

## Seguridad

- Autenticación basada en JWT (stateless).
- Autorización por roles para operaciones sensibles o administrativas.

## Resiliencia y rendimiento de integración

- Usar cliente HTTP no bloqueante (WebClient) en la orquestación del BFF.
- Incorporar patrón Circuit Breaker (Resilience4j) para degradación controlada ante fallos entre servicios.

## Datos y reproducibilidad

- Versionar el esquema de cada base con Flyway, ejecutando migraciones al iniciar cada servicio para entornos reproducibles.

## Escalabilidad y mantenibilidad

- Permitir escalar servicios de forma independiente según la carga de cada dominio.
- Mantener bajo acoplamiento entre equipos y componentes y aislar fallos entre microservicios.

## Stack tecnológico

- Backend en Java 21 y Spring Boot; construcción con Gradle (Groovy DSL).
- Frontend en React y TypeScript.
- Persistencia en PostgreSQL por servicio.


# ✅ Conclusión

La arquitectura permite construir un sistema:

- Escalable
- Desacoplado
- Mantenible
- Preparado para crecimiento

---

# 👨‍💻 Integrantes

- Nelson Cofré
- Nicolas Sanchez
- Mario Cofré

---
