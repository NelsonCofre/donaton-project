import { createBrowserRouter, Navigate, RouterProvider } from 'react-router-dom'
import { CollectionCenterDetailPage } from '@/pages/collection-center-detail'
import { CollectionCentersPage } from '@/pages/collection-centers'
import { DonationCreatePage } from '@/pages/donation-create'
import { DonationDetailPage } from '@/pages/donation-detail'
import { DonationsListPage } from '@/pages/donations-list'
import { InventoryPage } from '@/pages/inventory'
import { LoginPage } from '@/pages/login'
import { NecessityCreatePage } from '@/pages/necessity-create'
import { NecessityDetailPage } from '@/pages/necessity-detail'
import { NecessitiesPage } from '@/pages/necessities'
import { RegisterPage } from '@/pages/register'
import { ShipmentsPage } from '@/pages/shipments'
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
            <NecessitiesPage />
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
