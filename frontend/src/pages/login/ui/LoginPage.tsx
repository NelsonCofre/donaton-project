import { Link, useNavigate } from 'react-router-dom'
import { LoginForm } from '@/features/auth-login'

export function LoginPage() {
  const navigate = useNavigate()

  return (
    <div className="donaton-auth-screen">
      <header className="donaton-auth-header">
        <p className="donaton-auth-eyebrow">Donaton</p>
        <h1>Iniciar sesión</h1>
        <p className="donaton-muted donaton-auth-subtitle">
          Accede con tu correo y contraseña (modelo de usuario del informe).
        </p>
      </header>
      <div className="donaton-card donaton-card--auth">
        <LoginForm onSuccess={() => navigate('/donaciones')} />
      </div>
      <p className="donaton-auth-switch">
        ¿No tienes cuenta? <Link to="/register">Crear cuenta</Link>
      </p>
    </div>
  )
}
