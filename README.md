# 📦 Donaton - Arquitectura de Microservicios (Informe Evaluación 2)

---

## 🧩 Descripción del Proyecto

Donaton es una plataforma orientada a la gestión de ayuda humanitaria, permitiendo registrar donaciones, necesidades en terreno y coordinar la logística de distribución.

El sistema implementa una arquitectura de **microservicios contenedorizados con Docker**, incorporando un **Backend for Frontend (BFF)** y un **API Gateway (KrakenD)**.
El frontend está desarrollado en **React + TypeScript** y el backend en **Java 21 con Spring Boot**, utilizando **Gradle (Groovy DSL)** como herramienta de construcción.

---

# 🏗️ Arquitectura del Sistema

## 🔷 Flujo de comunicación

Frontend → BFF → API Gateway (KrakenD) → Microservicios

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
git checkout develop
git pull origin develop
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

---

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
