import { useState } from 'react'
import { ApiError } from '@/shared/api/client'
import { createDonacion } from '@/entities/donation'

type CreateDonationFormProps = {
  onCreated: () => void
}

export function CreateDonationForm({ onCreated }: CreateDonationFormProps) {
  const [nombreDonante, setNombreDonante] = useState('')
  const [contactoDonante, setContactoDonante] = useState('')
  const [tipoRecurso, setTipoRecurso] = useState('')
  const [cantidad, setCantidad] = useState(1)
  const [fecha, setFecha] = useState(() => new Date().toISOString().slice(0, 10))
  const [error, setError] = useState<string | null>(null)
  const [ok, setOk] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setError(null)
    setOk(null)
    setLoading(true)
    try {
      await createDonacion({
        nombreDonante: nombreDonante.trim(),
        contactoDonante: contactoDonante.trim(),
        tipoRecurso: tipoRecurso.trim(),
        cantidad: Math.max(1, Math.floor(Number(cantidad))),
        fecha: new Date(fecha).toISOString(),
      })
      setOk('Donación registrada correctamente.')
      onCreated()
      setNombreDonante('')
      setContactoDonante('')
      setTipoRecurso('')
      setCantidad(1)
    } catch (err) {
      setError(err instanceof ApiError ? err.message : 'No se pudo crear la donación.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <form className="donaton-form" style={{ maxWidth: 520 }} onSubmit={handleSubmit}>
      <h2>Nueva donación</h2>
      <p className="donaton-muted">
        Datos alineados al modelo del informe: donante (nombre, contacto), recurso
        (tipo), donación (fecha, cantidad).
      </p>
      <div className="donaton-field">
        <label htmlFor="don-nombre">Nombre del donante</label>
        <input
          id="don-nombre"
          name="nombreDonante"
          required
          value={nombreDonante}
          onChange={(e) => setNombreDonante(e.target.value)}
        />
      </div>
      <div className="donaton-field">
        <label htmlFor="don-contacto">Contacto del donante</label>
        <input
          id="don-contacto"
          name="contactoDonante"
          required
          placeholder="Teléfono o correo"
          value={contactoDonante}
          onChange={(e) => setContactoDonante(e.target.value)}
        />
      </div>
      <div className="donaton-field">
        <label htmlFor="don-tipo">Tipo de recurso</label>
        <input
          id="don-tipo"
          name="tipoRecurso"
          required
          placeholder="Ej. alimentos no perecederos"
          value={tipoRecurso}
          onChange={(e) => setTipoRecurso(e.target.value)}
        />
      </div>
      <div className="donaton-field">
        <label htmlFor="don-cantidad">Cantidad</label>
        <input
          id="don-cantidad"
          name="cantidad"
          type="number"
          min={1}
          required
          value={cantidad}
          onChange={(e) => setCantidad(Number(e.target.value))}
        />
      </div>
      <div className="donaton-field">
        <label htmlFor="don-fecha">Fecha</label>
        <input
          id="don-fecha"
          name="fecha"
          type="date"
          required
          value={fecha}
          onChange={(e) => setFecha(e.target.value)}
        />
      </div>
      {error ? (
        <div className="donaton-alert donaton-alert--error" role="alert">
          {error}
        </div>
      ) : null}
      {ok ? <p className="donaton-muted">{ok}</p> : null}
      <button className="donaton-btn" type="submit" disabled={loading}>
        {loading ? 'Guardando…' : 'Registrar donación'}
      </button>
    </form>
  )
}
