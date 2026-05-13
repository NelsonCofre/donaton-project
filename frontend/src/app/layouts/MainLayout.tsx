import { NavLink, Outlet, Navigate, useLocation, useNavigate } from 'react-router-dom'
import { clearStoredToken, getStoredToken } from '@/shared/lib/authStorage'

const linkClass = ({ isActive }: { isActive: boolean }) =>
  isActive ? 'active' : undefined

const PUBLIC_AUTH_PATHS = ['/', '/login', '/register'] as const

export function MainLayout() {
  const location = useLocation()
  const navigate = useNavigate()
  const token = getStoredToken()
  const path = location.pathname

  if (
    token &&
    (PUBLIC_AUTH_PATHS as readonly string[]).includes(path)
  ) {
    return <Navigate to="/donaciones" replace />
  }

  const isAuthPage = path === '/login' || path === '/register'

  return (
    <div
      className={`donaton-layout${isAuthPage ? ' donaton-layout--auth' : ''}`}
    >
      <header className="donaton-nav">
        <NavLink
          to={token ? '/donaciones' : '/login'}
          className="donaton-nav__brand"
        >
          Donaton
        </NavLink>
        {token ? (
          <>
            <NavLink to="/donaciones" className={linkClass} end>
              Donaciones
            </NavLink>
            <button
              type="button"
              className="donaton-nav__logout"
              onClick={() => {
                clearStoredToken()
                navigate('/login', { replace: true })
              }}
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
