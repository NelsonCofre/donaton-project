# Flujo de tests y cobertura

> **Levantar el proyecto con Kubernetes:** [EJECUTAR.md](EJECUTAR.md) · Referencia técnica: [KUBERNETES.md](KUBERNETES.md)

Este proyecto usa dos flujos de pruebas:

- **Backend Java:** JUnit 5, Spring Boot Test, Mockito/MockMvc, H2 y JaCoCo.
- **Frontend React:** Vitest, React Testing Library, jsdom y coverage con V8.

## Estado actual de la suite (~86 tests)

| Módulo | Tests | Archivos | Enfoque |
|--------|-------|----------|---------|
| `ms-auth` | 12 | 3 | JWT unitario + API integración |
| `ms-donation` | 12 | 3 | Servicio unitario + CRUD + 404/400 |
| `ms-necessity` | 11 | 3 | Servicio unitario + CRUD + 404/400 |
| `ms-logistic` | 8 | 2 | CRUD centros/inventario/envíos + errores |
| `bff` | 24 | 5 | Mappers, filtro JWT y contexto Spring |
| `frontend` | 19 | 9 | UI compartida, mappers y auth storage |
| **Total** | **86** | **25** | |

---

## Cómo ver los tests

Hay tres formas de revisar los tests: leer el código, ver resultados en consola o abrir reportes HTML.

### 1. Ver el código fuente (en el IDE)

**Backend** — cada módulo tiene sus tests bajo `src/test/java`:

```text
backend/ms-auth/src/test/java/com/donaton/auth/
backend/ms-donation/src/test/java/com/donaton/donation/
backend/ms-necessity/src/test/java/com/donaton/necessity/
backend/ms-logistic/src/test/java/com/donaton/logistics/
backend/bff/src/test/java/com/donaton/bff/
```

Ejemplos de archivos:

| Archivo | Qué prueba |
|---------|------------|
| `AuthApiIntegrationTest.java` | Register, login, validate-token, `/me` |
| `JwtServiceTest.java` | Generación y validación de JWT |
| `DonationServiceImplTest.java` | Lógica de negocio de donaciones |
| `DonationMapperTest.java` | Mapeo frontend ↔ microservicio en BFF |
| `JwtAuthFilterTest.java` | Filtro de autenticación del BFF |

**Frontend** — archivos `*.test.ts` / `*.test.tsx` junto al código:

```text
frontend/src/shared/ui/StatusBadge.test.tsx
frontend/src/shared/ui/EmptyState.test.tsx
frontend/src/shared/lib/authStorage.test.ts
frontend/src/entities/donation/lib/mapDonacion.test.ts
frontend/src/features/donation-status-chip/ui/DonationStatusChip.test.tsx
```

En Cursor/VS Code: `Ctrl+P` y busca por nombre, por ejemplo `DonationMapperTest` o `StatusBadge.test`.

---

### 2. Ejecutar tests y ver resultado en consola

**Todo el proyecto** (desde la raíz):

```powershell
.\scripts\test-all.ps1
```

Si todo pasa, verás `BUILD SUCCESSFUL` en cada backend, un resumen por módulo y al final una tabla con el **gran total** (backend + frontend):

```text
    [OK] 12 passed — ms-auth (12 tests)
    ...
==> Resumen de tests
    Backend (5 módulos):
      ms-auth         12 tests (12 passed)
      ...
      TOTAL backend   67 tests (67 passed)

    Frontend:
      vitest          19 tests (19 passed)

    GRAN TOTAL:      86 tests (86 passed)
```

Para listar **cada test por nombre** (backend y frontend):

```powershell
.\scripts\test-all.ps1 -VerboseTests
```

El total correcto es **86 tests** (67 backend + 19 frontend). El resumen final lo calcula leyendo los reportes de Gradle y el JSON de Vitest.

**Un módulo backend:**

```powershell
cd backend\ms-auth
.\gradlew.bat test
```

**Frontend:**

```powershell
cd frontend
npm test
```

**Modo watch** (frontend, re-ejecuta al guardar):

```powershell
cd frontend
npm run test:watch
```

---

### 3. Ver reportes HTML (recomendado para revisar o mostrar al profe)

Los reportes HTML se generan **después** de ejecutar los tests. Si no existen, corre primero `.\gradlew.bat test` o `npm test`.

#### Resultados de tests — Backend

Abre en el navegador (doble clic o arrastrar al Chrome/Edge):

```text
backend\ms-auth\build\reports\tests\test\index.html
backend\ms-donation\build\reports\tests\test\index.html
backend\ms-necessity\build\reports\tests\test\index.html
backend\ms-logistic\build\reports\tests\test\index.html
backend\bff\build\reports\tests\test\index.html
```

Ahí puedes ver:

- Cuántos tests pasaron o fallaron
- Tiempo de ejecución por clase
- Detalle de cada test (clic en el nombre de la clase)

#### Cobertura de código — Backend (JaCoCo)

Generar reporte:

```powershell
cd backend\ms-auth
.\gradlew.bat test jacocoTestReport
```

Abrir:

```text
backend\<modulo>\build\reports\jacoco\test\html\index.html
```

Muestra porcentaje de líneas/ramas cubiertas y qué código falta por testear (en verde/rojo).

#### Cobertura de código — Frontend

```powershell
cd frontend
npm run coverage
```

Abrir:

```text
frontend\coverage\index.html
```

---

## Flujo recomendado paso a paso

```text
1. Ejecutar suite completa
   .\scripts\test-all.ps1

2. Si quieres revisar un módulo en detalle
   cd backend\bff
   .\gradlew.bat test

3. Abrir reporte HTML de resultados
   backend\bff\build\reports\tests\test\index.html

4. (Opcional) Ver cobertura
   .\gradlew.bat jacocoTestReport          → backend
   cd frontend && npm run coverage         → frontend
```

---

## Ejecutar con verificación estricta de 80%

Cuando la cobertura alcance el umbral configurado:

```powershell
.\scripts\test-all.ps1 -CoverageCheck
```

Esto ejecuta:

- `./gradlew jacocoTestCoverageVerification` en cada backend.
- `npm run coverage` en frontend.

> **Nota:** hoy los tests pasan, pero la cobertura global puede estar por debajo del 80%. Usa `-CoverageCheck` solo cuando quieras validar el umbral estricto.

---

## Perfil test de backend

Los microservicios con base de datos usan `application-test.properties` con H2 en memoria y Flyway desactivado. Los tests **no** dependen de PostgreSQL, Docker ni Kubernetes.

El BFF usa URLs locales falsas en su perfil `test`; para pruebas de controllers/clients se mockean dependencias externas.

Archivos de configuración:

```text
backend/ms-auth/src/test/resources/application-test.properties
backend/ms-donation/src/test/resources/application-test.properties
backend/ms-necessity/src/test/resources/application-test.properties
backend/ms-logistic/src/test/resources/application-test.properties
backend/bff/src/test/resources/application-test.properties
```

---

## Problemas frecuentes (Windows / OneDrive)

### Vitest: `Timeout waiting for worker to respond` o fallo en `EmptyState.test.tsx`

En Windows, sobre todo si el proyecto está en **OneDrive**, Vitest puede tardar mucho en arrancar workers (`forks`) y parecer que falla aunque los demás tests pasen.

El proyecto ya está configurado para mitigarlo:

- Pool **`threads`** (no `forks`) en `frontend/vite.config.ts` y en los scripts `npm test`.
- **`maxWorkers: 1`** y **`fileParallelism: false`** (un archivo a la vez).
- El **React Compiler** se desactiva durante tests (acelera el arranque).

Si sigue fallando:

1. Cierra otras apps pesadas y vuelve a ejecutar `npm test` solo en `frontend/`.
2. Excluye `node_modules` de la sincronización de OneDrive, o mueve el repo fuera de OneDrive.
3. Ejecuta de nuevo: `npm test` en `frontend/` — deberías ver **9 archivos / 19 tests**.

### El script termina con error aunque el backend pasó

`test-all.ps1` ahora propaga el código de salida: si frontend falla, el script termina con error (no solo muestra "Flujo finalizado").

---

## Herramientas usadas

| Capa | Herramienta | Para qué |
|------|-------------|----------|
| Backend | JUnit 5 | Framework de tests |
| Backend | Mockito | Mocks en tests unitarios |
| Backend | MockMvc | Tests de API REST |
| Backend | H2 | Base de datos en memoria para tests |
| Backend | JaCoCo | Reporte de cobertura |
| Frontend | Vitest | Runner de tests |
| Frontend | React Testing Library | Tests de componentes |
| Frontend | jsdom | Simula el DOM del navegador |
