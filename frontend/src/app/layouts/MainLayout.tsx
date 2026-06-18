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
        <div className="donaton-nav__inner">
          <div className="donaton-nav__brand-block">
            <NavLink
              to={isAuthenticated ? '/donaciones' : '/login'}
              className="donaton-nav__brand"
            >
              Donaton
            </NavLink>
            <p className="donaton-nav__subtitle">
              Centro operativo para donaciones, necesidades y logística.
            </p>
          </div>
          {isAuthenticated ? (
            <div className="donaton-nav__links">
              <div className="donaton-nav__group">
                <span className="donaton-nav__group-label">Operación</span>
                <NavLink to="/donaciones" className={linkClass} end>
                  Donaciones
                </NavLink>
                <NavLink to="/necesidades" className={linkClass}>
                  Necesidades
                </NavLink>
              </div>
              <div className="donaton-nav__group">
                <span className="donaton-nav__group-label">Logística</span>
                <NavLink to="/logistica/centros" className={linkClass}>
                  Centros
                </NavLink>
                <NavLink to="/logistica/inventario" className={linkClass}>
                  Inventario
                </NavLink>
                <NavLink to="/logistica/envios" className={linkClass}>
                  Envios
                </NavLink>
              </div>
              <button
                type="button"
                className="donaton-nav__logout"
                onClick={handleLogout}
              >
                Cerrar sesión
              </button>
            </div>
          ) : (
            <div className="donaton-nav__links donaton-nav__links--public">
              <NavLink to="/login" className={linkClass}>
                Iniciar sesión
              </NavLink>
              <NavLink to="/register" className={linkClass}>
                Registro
              </NavLink>
            </div>
          )}
        </div>
      </header>
      <main
        className={`donaton-main${isAuthPage ? ' donaton-main--auth' : ''}`}
      >
        <Outlet />
      </main>
    </div>
  )
}
