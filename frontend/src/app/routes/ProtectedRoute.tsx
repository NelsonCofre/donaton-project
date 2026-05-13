import type { ReactNode } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { getStoredToken } from '@/shared/lib/authStorage'

type ProtectedRouteProps = {
  children: ReactNode
}

export function ProtectedRoute({ children }: ProtectedRouteProps) {
  const location = useLocation()
  if (!getStoredToken()) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />
  }
  return children
}
