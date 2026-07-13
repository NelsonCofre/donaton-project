import { useState } from 'react'
import type { CreateDonacionRequest } from '../model/types'

export type DonationFormValues = CreateDonacionRequest
const emptyValues: DonationFormValues = {
  nombreDonante: '', contactoDonante: '', tipoRecurso: '', cantidad: 1,
  fecha: new Date().toISOString().slice(0, 10),
}
type Props = {
  title: string; submitLabel: string; initialValues?: DonationFormValues
  error: string | null; success: string | null; loading: boolean
  onSubmit: (values: DonationFormValues) => void; onCancel?: () => void
}
export function DonationForm({ title, submitLabel, initialValues, error, success, loading, onSubmit, onCancel }: Props) {
  const [values, setValues] = useState(initialValues ?? emptyValues)
  function submit(event: React.FormEvent) {
    event.preventDefault()
    onSubmit({ ...values, nombreDonante: values.nombreDonante.trim(), contactoDonante: values.contactoDonante.trim(), tipoRecurso: values.tipoRecurso.trim(), cantidad: Math.max(1, Math.floor(Number(values.cantidad))) })
  }
  const field = (id: string, label: string, key: 'nombreDonante' | 'contactoDonante' | 'tipoRecurso' | 'fecha', type = 'text') => (
    <div className="donaton-field"><label htmlFor={id}>{label}</label><input id={id} type={type} required value={values[key]} onChange={(event) => setValues({ ...values, [key]: event.target.value })} /></div>
  )
  return <form className="donaton-form donaton-form--wide" onSubmit={submit}>
    <h2>{title}</h2>
    {field('don-nombre', 'Nombre del donante', 'nombreDonante')}
    {field('don-contacto', 'Contacto del donante', 'contactoDonante')}
    {field('don-tipo', 'Tipo de recurso', 'tipoRecurso')}
    <div className="donaton-field"><label htmlFor="don-cantidad">Cantidad</label><input id="don-cantidad" type="number" min={1} required value={values.cantidad} onChange={(event) => setValues({ ...values, cantidad: Number(event.target.value) })} /></div>
    {field('don-fecha', 'Fecha', 'fecha', 'date')}
    {error ? <div className="donaton-alert donaton-alert--error" role="alert">{error}</div> : null}
    {success ? <p className="donaton-banner donaton-banner--ok">{success}</p> : null}
    <div className="donaton-form-actions">
      {onCancel ? <button type="button" className="donaton-btn donaton-btn--secondary" onClick={onCancel} disabled={loading}>Cancelar</button> : null}
      <button className="donaton-btn" type="submit" disabled={loading}>{loading ? 'Guardando…' : submitLabel}</button>
    </div>
  </form>
}
