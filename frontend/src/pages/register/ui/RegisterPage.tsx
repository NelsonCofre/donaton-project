import { Link, useNavigate } from 'react-router-dom'
import { RegisterForm } from '@/features/auth-register'

export function RegisterPage() {
  const navigate = useNavigate()

  return (
    <div className="donaton-auth-shell">
      <section className="donaton-auth-screen donaton-auth-screen--panel">
        <header className="donaton-auth-header">
          <p className="donaton-auth-eyebrow">Alta de usuarios</p>
          <h1>Crear cuenta</h1>
          <p className="donaton-muted donaton-auth-subtitle">
            Habilita acceso al panel operativo con una cuenta preparada para trabajo colaborativo.
          </p>
        </header>
        <div className="donaton-auth-feature-list">
          <div className="donaton-auth-feature">
            <strong>Ingreso rápido</strong>
            <span>Registro simple con foco en acceso inmediato al sistema.</span>
          </div>
          <div className="donaton-auth-feature">
            <strong>Escalable</strong>
            <span>La experiencia ya considera crecimiento futuro por módulos y roles.</span>
          </div>
        </div>
      </section>
      <div className="donaton-card donaton-card--auth">
        <div className="donaton-auth-card__header">
          <p className="donaton-auth-card__eyebrow">Registro</p>
          <h2>Nueva cuenta</h2>
        </div>
        <RegisterForm onSuccess={() => navigate('/login')} />
        <p className="donaton-auth-switch">
          ¿Ya tienes cuenta? <Link to="/login">Iniciar sesión</Link>
        </p>
      </div>
    </div>
  )
}
