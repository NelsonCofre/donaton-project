# Donaton — Frontend (documentación técnica)

Cliente web del proyecto **Donaton**, alineado con la arquitectura del README del monorepo: consumo del **BFF** por HTTP (JSON), sin acceso directo a microservicios desde el navegador.

## Stack

- **React 19** + **TypeScript**
- **Vite 8**
- **React Router 7** (enrutado del lado cliente)

## Arquitectura: Feature-Sliced Design (FSD)

Se adopta **FSD** para separar responsabilidades y respetar reglas de importación entre capas (de arriba hacia abajo: `app` → `pages` → `widgets` → `features` → `entities` → `shared`).

| Capa | Rol |
|------|-----|
| **app** | Arranque: estilos globales, layout con `Outlet`, router y ruta protegida. |
| **pages** | Rutas: acceso (`/login`), registro (`/register`), donaciones (`/donaciones`). La raíz `/` redirige a `/login`. |
| **widgets** | Bloques compuestos reutilizables (p. ej. tablero de donaciones). |
| **features** | Acciones de usuario concretas (formulario de login, registro, listado, alta). |
| **entities** | Modelo de negocio y llamadas API por dominio (`user`, `donation`). |
| **shared** | Infraestructura: cliente HTTP, configuración, utilidades. |

### Árbol principal (`src/`)

```
src/
├── app/
│   ├── App.tsx
│   ├── layouts/MainLayout.tsx
│   ├── routes/ProtectedRoute.tsx
│   └── styles/global.css
├── pages/
│   ├── login/
│   ├── register/
│   └── donations/
├── widgets/
│   └── donations-dashboard/
├── features/
│   ├── auth-login/
│   ├── auth-register/
│   ├── donation-list/
│   └── donation-create/
├── entities/
│   ├── user/
│   └── donation/
├── shared/
│   ├── api/client.ts
│   ├── config/env.ts
│   └── lib/authStorage.ts
├── main.tsx
└── vite-env.d.ts
```

Cada slice expone un **API público** vía `index.ts` en la raíz del slice (importar desde `@/pages/login`, `@/entities/user`, etc.).

## Modelo de datos (alineado al README raíz)

### Usuario (`entities/user`)

- `idUsuario`, `email`, `rol` (`ADMIN` | `USER`) en modelo y respuestas; la contraseña no se expone en el cliente salvo en formularios.
- **Registro:** el front envía siempre `rol: "USER"` hasta que exista gestión de roles; `POST /api/auth/register` → esperado `user` creado (contrato BFF).
- **Login:** `POST /api/auth/login` → esperado `{ token, user }` (ajustar al contrato real del BFF).

### Donaciones (`entities/donation`)

Basado en donante, recurso y donación del informe:

- **Listado:** `GET /api/donations` → arreglo de `Donacion`.
- **Alta:** `POST /api/donations` con cuerpo agregado para la UI: `nombreDonante`, `contactoDonante`, `tipoRecurso`, `cantidad`, `fecha` (el BFF puede mapearlo a tablas `Donante` / `Recurso` / `Donacion` / `Donacion_Recurso`).

Los estados de donación en tipos TypeScript son orientativos hasta que el backend los defina (`PENDIENTE`, `RECIBIDA`, etc.).

## Variables de entorno

| Variable | Descripción |
|----------|-------------|
| `VITE_API_BASE_URL` | Origen del API Gateway/BFF para auth y donaciones. En K8s: `http://localhost:30090`. |
| `VITE_NECESSITY_API_BASE_URL` | Origen de necesidades. Vacío usa mock local; en K8s se apunta al gateway `http://localhost:30090`. |
| `VITE_LOGISTICS_API_BASE_URL` | Origen de logística. Vacío usa mock local; en K8s se apunta al gateway `http://localhost:30090`. |

Copia `.env.example` a `.env` y ajusta la URL en desarrollo.

## Autenticación en cliente

Tras login exitoso se guarda el JWT en `localStorage` (`shared/lib/authStorage.ts`). Las peticiones subsiguientes envían `Authorization: Bearer …` desde `shared/api/client.ts`. Un `401` limpia el token almacenado.

## Rutas

| Ruta | Comportamiento |
|------|----------------|
| `/` | Redirección a **`/login`**. Con sesión activa, el layout redirige a **`/donaciones`**. |
| `/login` | Página solo de inicio de sesión. |
| `/register` | Página solo de registro. |
| `/iniciar-sesion`, `/registro` | Redirección a `/login` y `/register` (compatibilidad). |
| `/donaciones` | Listado y alta de donaciones; **solo con sesión**. Sin token, redirección a `/login`. |

Con sesión: enlace a donaciones y **Cerrar sesión** (borra el token y vuelve a `/login`). Sin sesión: **Iniciar sesión** y **Registro** en la barra; la marca **Donaton** enlaza a `/login`.

## Alias de importación

- `@/*` → `src/*` (configurado en `vite.config.ts` y `tsconfig.app.json`).

## Scripts

```bash
npm run dev      # servidor de desarrollo
npm run build    # compilación TypeScript + bundle producción
npm run preview  # vista previa del build
npm run lint     # ESLint
```

## Contenedor (Docker)

Imagen multi-stage: **Node** ejecuta `npm ci` + `npm run build`; **Nginx** sirve `dist/` con `try_files` para el SPA (`Dockerfile`, `nginx.conf`).

Desde la raíz del monorepo (junto a `docker-compose.yml`):

```bash
docker compose build frontend
docker compose up -d frontend
```

La app queda en el puerto del host **`FRONTEND_PORT`** (por defecto **5173**), mapeado al 80 del contenedor.

Las variables `VITE_*_API_BASE_URL` se resuelven **en tiempo de build** (Vite). Deben ser URLs que el **navegador** pueda alcanzar desde tu máquina; en Kubernetes apuntan al API Gateway (`http://localhost:30090`). Puedes pasarlas al construir:

```bash
VITE_API_BASE_URL=http://localhost:8090 \
VITE_NECESSITY_API_BASE_URL=http://localhost:8090 \
VITE_LOGISTICS_API_BASE_URL=http://localhost:8090 \
docker compose build frontend
```

O definir las variables `VITE_*_API_BASE_URL` y opcionalmente `FRONTEND_PORT` en un archivo `.env` en la raíz del repo (Compose las lee automáticamente).

## Estado del backend

Los endpoints anteriores son **contratos previstos** para cuando exista el BFF. Si el servidor no responde, la página de donaciones muestra mensaje de error; al conectar el BFF, conviene unificar DTOs con los controladores reales.

## Referencias

- [Feature-Sliced Design](https://feature-sliced.design/)
- README de arquitectura del monorepo (raíz del repositorio).
