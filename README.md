# 📦 Donaton - Arquitectura de Microservicios (Informe Evaluación 2)

---

## 🧩 Descripción del Proyecto

Donaton es una plataforma orientada a la gestión de ayuda humanitaria, permitiendo registrar donaciones, necesidades en terreno y coordinar la logística de distribución.

El sistema se basa en una arquitectura de microservicios contenedorizados con Docker, incorporando un Backend for Frontend (BFF) y un API Gateway.

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

- Enrutamiento
- Seguridad
- Validación JWT

---

### ✔ Backend for Frontend (BFF)

- Orquesta múltiples microservicios
- Reduce llamadas desde frontend
- Adapta respuestas

---

### ✔ Database per Service

Cada microservicio posee su propia base de datos independiente.

---

# 🐳 Contenerización con Docker

Cada componente se ejecuta en su propio contenedor:

- BFF
- KrakenD
- Microservicios
- Bases de datos (una por microservicio)

---

# 🗄️ Bases de Datos por Microservicio

Cada microservicio tiene su propia base de datos dentro de su contenedor Docker.

## ✔ Distribución

- auth-service → auth-db
- donations-service → donations-db
- needs-service → needs-db
- logistics-service → logistics-db

## ⚠️ Restricciones

- No se comparten bases de datos
- No existen relaciones entre bases de datos
- Comunicación solo vía HTTP (REST)

---

# 🧠 Patrones de Diseño

### ✔ Repository Pattern

Encapsula acceso a datos.

### ✔ Factory Method

Creación de recursos.

### ✔ Circuit Breaker (Resilience4j)

Manejo de fallos.

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

- Usuario es independiente (no tiene relaciones con otras entidades internas)
- El rol se maneja como ENUM

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

| Atributo    | Tipo                                  |
| ----------- | ------------------------------------- |
| id_donacion | PK                                    |
| fecha       | Date                                  |
| cantidad    | Integer                               |
| estado      | ENUM (PENDIENTE, RECIBIDA, ENTREGADA) |
| id_donante  | FK                                    |

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
- Donacion **N:M** Recurso (mediante Donacion_Recurso)
- Recurso **1:N** Donacion_Recurso
- Donacion **1:N** Donacion_Recurso

---

# 📍 Microservicio de Necesidades

## 📌 Responsabilidad

Registrar necesidades en terreno.

## 🧩 Entidades

### Necesidad

| Atributo     | Tipo                       |
| ------------ | -------------------------- |
| id_necesidad | PK                         |
| cantidad     | Integer                    |
| prioridad    | ENUM (ALTA, MEDIA, BAJA)   |
| estado       | ENUM (PENDIENTE, CUBIERTA) |
| id_ubicacion | FK                         |

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
- Necesidad **N:M** Recurso (mediante Necesidad_Recurso)
- Recurso **1:N** Necesidad_Recurso
- Necesidad **1:N** Necesidad_Recurso

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

| Atributo  | Tipo                        |
| --------- | --------------------------- |
| id_envio  | PK                          |
| fecha     | Date                        |
| estado    | ENUM (EN_CAMINO, ENTREGADO) |
| id_centro | FK                          |

---

## 🔗 Relaciones

- CentroAcopio **1:N** Inventario
- CentroAcopio **1:N** Envio
- Inventario **N:1** CentroAcopio
- Envio **N:1** CentroAcopio

---

# 🔄 Git Flow

## 🌿 Ramas principales

- `main` → producción
- `develop` → integración

---

## 🌱 Flujo de trabajo

1. Antes de comenzar una nueva funcionalidad:

```bash
git checkout develop
git pull origin develop
```

2. Crear una nueva rama desde develop:

```bash
git checkout -b feature/nombre-feature
```

3. Desarrollar la funcionalidad y realizar commits

4. Subir la rama:

```bash
git push origin feature/nombre-feature
```

5. Crear Pull Request:

- Desde `feature/*` hacia `develop`
- Permite revisión de código antes de integrar

6. Una vez aprobado:

- Se realiza merge hacia `develop`

7. Cuando `develop` esté estable:

- Se realiza merge desde `develop` hacia `main`

---

## 🎯 Objetivo

- Mantener estabilidad en producción
- Permitir desarrollo paralelo
- Facilitar revisión de código
- Evitar conflictos

---

# 🔗 Comunicación entre Microservicios

- HTTP REST
- Orquestación mediante BFF
- Gateway: KrakenD

---

# 🔒 Seguridad

- JWT
- Validación en API Gateway
- Control de roles

---

# 📈 Escalabilidad

Cada microservicio escala de forma independiente.

---

# 🧠 Justificación del Modelo

- Eliminación de entidades innecesarias
- Uso de ENUM
- Separación de responsabilidades
- Bajo acoplamiento

---

# 🚀 Tecnologías

- Java 21
- Spring Boot 3.3.x
- PostgreSQL
- Docker
- KrakenD
- React
- TypeScript
- Zustand
- Resilience4j

---

# ✅ Conclusión

El sistema implementa una arquitectura moderna basada en microservicios, permitiendo escalabilidad, mantenibilidad y desacoplamiento.

---

# 👨‍💻 Integrantes

- Nelson Cofré
- Nicolas Sanchez
- Mario Cofré

---
