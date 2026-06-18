import { Link, useNavigate } from 'react-router-dom'
import { LoginForm } from '@/features/auth-login'

export function LoginPage() {
  const navigate = useNavigate()

  return (
    <div className="donaton-auth-shell">
      <section className="donaton-auth-screen donaton-auth-screen--panel">
        <header className="donaton-auth-header">
          <p className="donaton-auth-eyebrow">Panel operativo</p>
          <h1>Iniciar sesión</h1>
          <p className="donaton-muted donaton-auth-subtitle">
            Accede al centro de control para coordinar donaciones, necesidades y logística.
          </p>
        </header>
        <div className="donaton-auth-feature-list">
          <div className="donaton-auth-feature">
            <strong>Visibilidad operativa</strong>
            <span>Consulta el estado de la ayuda en un solo lugar.</span>
          </div>
          <div className="donaton-auth-feature">
            <strong>Flujos claros</strong>
            <span>Gestiona registros, prioridades y despachos con una UI estructurada.</span>
          </div>
          <div className="donaton-auth-feature">
            <strong>Acceso seguro</strong>
            <span>Trabaja con autenticación centralizada y experiencia consistente.</span>
          </div>
        </div>
      </section>
      <div className="donaton-card donaton-card--auth">
        <div className="donaton-auth-card__header">
          <p className="donaton-auth-card__eyebrow">Acceso</p>
          <h2>Credenciales de usuario</h2>
        </div>
        <LoginForm onSuccess={() => navigate('/donaciones')} />
        <p className="donaton-auth-switch">
          ¿No tienes cuenta? <Link to="/register">Crear cuenta</Link>
        </p>
      </div>
    </div>
  )
}
