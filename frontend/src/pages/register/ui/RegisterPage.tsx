import { Link, useNavigate } from 'react-router-dom'
import { RegisterForm } from '@/features/auth-register'

export function RegisterPage() {
  const navigate = useNavigate()

  return (
    <div className="donaton-auth-screen">
      <header className="donaton-auth-header">
        <p className="donaton-auth-eyebrow">Donaton</p>
        <h1>Crear cuenta</h1>
        <p className="donaton-muted donaton-auth-subtitle">
          Regístrate con email y contraseña. El rol queda como usuario; la gestión
          de roles se implementará más adelante.
        </p>
      </header>
      <div className="donaton-card donaton-card--auth">
        <RegisterForm onSuccess={() => navigate('/login')} />
      </div>
      <p className="donaton-auth-switch">
        ¿Ya tienes cuenta? <Link to="/login">Iniciar sesión</Link>
      </p>
    </div>
  )
}
