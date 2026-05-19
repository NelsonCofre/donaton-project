import { NavLink, Outlet, Navigate, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '@/shared/lib/useAuth'

const linkClass = ({ isActive }: { isActive: boolean }) =>
  isActive ? 'active' : undefined

const PUBLIC_AUTH_PATHS = ['/', '/login', '/register'] as const

export function MainLayout() {
  const location = useLocation()
  const navigate = useNavigate()
  const { isAuthenticated, clearToken } = useAuth()
  const path = location.pathname

  if (
    isAuthenticated &&
    (PUBLIC_AUTH_PATHS as readonly string[]).includes(path)
  ) {
    return <Navigate to="/donaciones" replace />
  }

  const isAuthPage = path === '/login' || path === '/register'

  function handleLogout() {
    clearToken()
    navigate('/login', { replace: true })
  }

  return (
    <div className={`donaton-layout${isAuthPage ? ' donaton-layout--auth' : ''}`}>
      <header className="donaton-nav">
        <NavLink
          to={isAuthenticated ? '/donaciones' : '/login'}
          className="donaton-nav__brand"
        >
          Donaton
        </NavLink>
        {isAuthenticated ? (
          <>
            <NavLink to="/donaciones" className={linkClass} end>
              Donaciones
            </NavLink>
            <button
              type="button"
              className="donaton-nav__logout"
              onClick={handleLogout}
            >
              Cerrar sesión
            </button>
          </>
        ) : (
          <>
            <NavLink to="/login" className={linkClass}>
              Iniciar sesión
            </NavLink>
            <NavLink to="/register" className={linkClass}>
              Registro
            </NavLink>
          </>
        )}
      </header>
      <main
        className={`donaton-main${isAuthPage ? ' donaton-main--auth' : ''}`}
      >
        <Outlet />
      </main>
    </div>
  )
}
