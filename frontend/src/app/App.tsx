import { createBrowserRouter, Navigate, RouterProvider } from 'react-router-dom'
import { DonationCreatePage } from '@/pages/donation-create'
import { DonationDetailPage } from '@/pages/donation-detail'
import { DonationsListPage } from '@/pages/donations-list'
import { LoginPage } from '@/pages/login'
import { RegisterPage } from '@/pages/register'
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
