import { useState } from 'react'
import { ApiError } from '@/shared/api/client'
import { register } from '@/entities/user'

type RegisterFormProps = {
  onSuccess: () => void
}

export function RegisterForm({ onSuccess }: RegisterFormProps) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirm, setConfirm] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setError(null)
    if (password !== confirm) {
      setError('Las contraseñas no coinciden.')
      return
    }
    if (password.length < 8) {
      setError('La contraseña debe tener al menos 8 caracteres.')
      return
    }
    setLoading(true)
    try {
      await register({
        email: email.trim(),
        password,
        rol: 'USER',
      })
      onSuccess()
    } catch (err) {
      setError(err instanceof ApiError ? err.message : 'No se pudo registrar.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <form className="donaton-form" onSubmit={handleSubmit}>
      <div className="donaton-field">
        <label htmlFor="reg-email">Correo electrónico</label>
        <input
          id="reg-email"
          name="email"
          type="email"
          autoComplete="email"
          required
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
      </div>
      <div className="donaton-field">
        <label htmlFor="reg-password">Contraseña</label>
        <input
          id="reg-password"
          name="password"
          type="password"
          autoComplete="new-password"
          required
          minLength={8}
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </div>
      <div className="donaton-field">
        <label htmlFor="reg-confirm">Confirmar contraseña</label>
        <input
          id="reg-confirm"
          name="confirm"
          type="password"
          autoComplete="new-password"
          required
          value={confirm}
          onChange={(e) => setConfirm(e.target.value)}
        />
      </div>
      {error ? (
        <div className="donaton-alert donaton-alert--error" role="alert">
          {error}
        </div>
      ) : null}
      <button className="donaton-btn" type="submit" disabled={loading}>
        {loading ? 'Registrando…' : 'Crear cuenta'}
      </button>
    </form>
  )
}
