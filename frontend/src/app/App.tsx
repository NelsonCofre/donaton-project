import { createBrowserRouter, Navigate, RouterProvider } from 'react-router-dom'
import { LoginPage, RegisterPage } from '@/pages/auth'
import {
  DonationCreatePage,
  DonationDetailPage,
  DonationsListPage,
} from '@/pages/donations'
import {
  NecessitiesListPage,
  NecessityCreatePage,
  NecessityDetailPage,
} from '@/pages/necessities'
import {
  CollectionCenterDetailPage,
  CollectionCentersPage,
  InventoryPage,
  ShipmentsPage,
} from '@/pages/logistics'
import { AuthProvider } from '@/shared/lib/authContext'
import { MainLayout } from './layouts/MainLayout'
import { ProtectedRoute } from './routes/ProtectedRoute'

const router = createBrowserRouter([
  {
    element: <MainLayout />,
    children: [
      { index: true, element: <Navigate to="/login" replace /> },
      { path: 'login', element: <LoginPage /> },
      { path: 'register', element: <RegisterPage /> },
      {
        path: 'iniciar-sesion',
        element: <Navigate to="/login" replace />,
      },
      { path: 'registro', element: <Navigate to="/register" replace /> },
      {
        path: 'donaciones',
        element: (
          <ProtectedRoute>
            <DonationsListPage />
          </ProtectedRoute>
        ),
      },
      {
        path: 'donaciones/nueva',
        element: (
          <ProtectedRoute>
            <DonationCreatePage />
          </ProtectedRoute>
        ),
      },
      {
        path: 'donaciones/:id',
        element: (
          <ProtectedRoute>
            <DonationDetailPage />
          </ProtectedRoute>
        ),
      },
      {
        path: 'necesidades',
        element: (
          <ProtectedRoute>
            <NecessitiesListPage />
          </ProtectedRoute>
        ),
      },
      {
        path: 'necesidades/nueva',
        element: (
          <ProtectedRoute>
            <NecessityCreatePage />
          </ProtectedRoute>
        ),
      },
      {
        path: 'necesidades/:id',
        element: (
          <ProtectedRoute>
            <NecessityDetailPage />
          </ProtectedRoute>
        ),
      },
      {
        path: 'logistica/centros',
        element: (
          <ProtectedRoute>
            <CollectionCentersPage />
          </ProtectedRoute>
        ),
      },
      {
        path: 'logistica/centros/:id',
        element: (
          <ProtectedRoute>
            <CollectionCenterDetailPage />
          </ProtectedRoute>
        ),
      },
      {
        path: 'logistica/inventario',
        element: (
          <ProtectedRoute>
            <InventoryPage />
          </ProtectedRoute>
        ),
      },
      {
        path: 'logistica/envios',
        element: (
          <ProtectedRoute>
            <ShipmentsPage />
          </ProtectedRoute>
        ),
      },
    ],
  },
])

export function App() {
  return (
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  )
}
