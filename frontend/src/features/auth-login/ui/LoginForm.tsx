import { useState } from 'react'
import { ApiError } from '@/shared/api/client'
import { login } from '@/entities/user'
import { useAuth } from '@/shared/lib/useAuth'

type LoginFormProps = {
  onSuccess: () => void
}

export function LoginForm({ onSuccess }: LoginFormProps) {
  const { setToken } = useAuth()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      const res = await login({ email: email.trim(), password })
      setToken(res.token)
      onSuccess()
    } catch (err) {
      setError(err instanceof ApiError ? err.message : 'No se pudo iniciar sesión.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <form className="donaton-form" onSubmit={handleSubmit}>
      <div className="donaton-field">
        <label htmlFor="login-email">Correo electrónico</label>
        <input
          id="login-email"
          name="email"
          type="email"
          autoComplete="email"
          required
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
      </div>
      <div className="donaton-field">
        <label htmlFor="login-password">Contraseña</label>
        <input
          id="login-password"
          name="password"
          type="password"
          autoComplete="current-password"
          required
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </div>
      {error ? (
        <div className="donaton-alert donaton-alert--error" role="alert">
          {error}
        </div>
      ) : null}
      <button className="donaton-btn" type="submit" disabled={loading}>
        {loading ? 'Entrando…' : 'Iniciar sesión'}
      </button>
    </form>
  )
}
