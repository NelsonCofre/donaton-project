# Donaton — Frontend

Cliente web de **Donaton**. El navegador solo habla con el **API Gateway / BFF** (nunca directo a los microservicios).

## Stack

- React 19 + TypeScript
- Vite 8
- React Router 7

## Arquitectura (simplificada)

Flujo que debes poder explicar:

```text
main.tsx
  → app/App.tsx          (router + AuthProvider)
    → MainLayout         (nav / logout)
      → ProtectedRoute   (exige JWT)
        → pages/*        (pantalla completa)
          → features/*   (formularios y piezas de UI con lógica)
          → entities/*   (tipos, hooks y API por dominio)
            → shared/api (cliente HTTP + JWT)
```

| Capa | Rol |
|------|-----|
| **app** | Arranque, layout, router y ruta protegida |
| **pages** | Pantallas reales por dominio (auth, donaciones, necesidades, logística) |
| **features** | Componentes de acción/UI agrupados por dominio |
| **entities** | Modelo + hooks + llamadas API (`user`, `donation`, `necessity`, `logistics`) |
| **shared** | Cliente HTTP, auth storage/context, UI reutilizable |

> Ya no existe la capa `widgets/`. Cada página contiene la composición de la pantalla.

### Árbol principal (`src/`)

```text
src/
├── app/
│   ├── App.tsx
│   ├── layouts/MainLayout.tsx
│   ├── routes/ProtectedRoute.tsx
│   └── styles/global.css
├── pages/
│   ├── auth/           LoginPage, RegisterPage
│   ├── donations/      listado, crear, detalle
│   ├── necessities/    listado, crear, detalle
│   └── logistics/      centros, inventario, envíos
├── features/
│   ├── auth/
│   ├── donations/
│   ├── necessities/
│   └── logistics/
├── entities/
│   ├── user/
│   ├── donation/
│   ├── necessity/
│   └── logistics/
├── shared/
│   ├── api/client.ts
│   ├── config/env.ts
│   ├── lib/            auth context, storage, hooks
│   └── ui/             PageHeader, SectionCard, StatusBadge, …
└── main.tsx
```

## Rutas

| Ruta | Página |
|------|--------|
| `/login`, `/register` | Auth |
| `/donaciones` | Listado |
| `/donaciones/nueva` | Crear |
| `/donaciones/:id` | Detalle / editar / eliminar |
| `/necesidades` … | CRUD necesidades |
| `/logistica/centros` … | Centros, inventario, envíos |

## Variables de entorno

| Variable | Descripción |
|----------|-------------|
| `VITE_API_BASE_URL` | Gateway para auth y donaciones (K8s: `http://localhost:30090`) |
| `VITE_NECESSITY_API_BASE_URL` | Necesidades; vacío = mock local |
| `VITE_LOGISTICS_API_BASE_URL` | Logística; vacío = mock local |

Copia `.env.example` a `.env` para desarrollo local.

## Scripts

```bash
npm install
npm run dev      # http://localhost:5173
npm run build
npm run test
npm run lint
```

## Auth

1. Login → BFF `/api/auth/login` → JWT en `localStorage` (vía `AuthProvider`)
2. `ProtectedRoute` bloquea rutas privadas sin token
3. `shared/api/client.ts` envía `Authorization: Bearer …`
4. Ante 401, limpia token y fuerza logout
