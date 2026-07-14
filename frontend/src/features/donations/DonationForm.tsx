import { useState } from 'react'
import type { CreateDonacionRequest } from '@/entities/donation'

export type DonationFormValues = CreateDonacionRequest

const emptyValues: DonationFormValues = {
  nombreDonante: '',
  contactoDonante: '',
  tipoRecurso: '',
  cantidad: 1,
  fecha: new Date().toISOString().slice(0, 10),
}

type DonationFormProps = {
  title: string
  submitLabel: string
  initialValues?: DonationFormValues
  error: string | null
  success: string | null
  loading: boolean
  onSubmit: (values: DonationFormValues) => void
  onCancel?: () => void
}

export function DonationForm({
  title,
  submitLabel,
  initialValues,
  error,
  success,
  loading,
  onSubmit,
  onCancel,
}: DonationFormProps) {
  const [values, setValues] = useState<DonationFormValues>(initialValues ?? emptyValues)

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    onSubmit({
      nombreDonante: values.nombreDonante.trim(),
      contactoDonante: values.contactoDonante.trim(),
      tipoRecurso: values.tipoRecurso.trim(),
      cantidad: Math.max(1, Math.floor(Number(values.cantidad))),
      fecha: values.fecha,
    })
  }

  return (
    <form className="donaton-form donaton-form--wide" onSubmit={handleSubmit}>
      <h2>{title}</h2>
      <DonationField
        id="don-nombre"
        label="Nombre del donante"
        value={values.nombreDonante}
        onChange={(nombreDonante) => setValues({ ...values, nombreDonante })}
        required
      />
      <DonationField
        id="don-contacto"
        label="Contacto del donante"
        value={values.contactoDonante}
        onChange={(contactoDonante) => setValues({ ...values, contactoDonante })}
        placeholder="Teléfono o correo"
        required
      />
      <DonationField
        id="don-tipo"
        label="Tipo de recurso"
        value={values.tipoRecurso}
        onChange={(tipoRecurso) => setValues({ ...values, tipoRecurso })}
        placeholder="Ej. alimentos no perecederos"
        required
      />
      <div className="donaton-field">
        <label htmlFor="don-cantidad">Cantidad</label>
        <input
          id="don-cantidad"
          name="cantidad"
          type="number"
          min={1}
          required
          value={values.cantidad}
          onChange={(e) =>
            setValues({ ...values, cantidad: Number(e.target.value) })
          }
        />
      </div>
      <DonationField
        id="don-fecha"
        label="Fecha"
        type="date"
        value={values.fecha}
        onChange={(fecha) => setValues({ ...values, fecha })}
        required
      />
      {error ? (
        <div className="donaton-alert donaton-alert--error" role="alert">
          {error}
        </div>
      ) : null}
      {success ? (
        <p className="donaton-banner donaton-banner--ok">{success}</p>
      ) : null}
      <div className="donaton-form-actions">
        {onCancel ? (
          <button
            type="button"
            className="donaton-btn donaton-btn--secondary"
            onClick={onCancel}
            disabled={loading}
          >
            Cancelar
          </button>
        ) : null}
        <button className="donaton-btn" type="submit" disabled={loading}>
          {loading ? 'Guardando…' : submitLabel}
        </button>
      </div>
    </form>
  )
}

function DonationField({
  id,
  label,
  value,
  onChange,
  type = 'text',
  placeholder,
  required,
}: {
  id: string
  label: string
  value: string | number
  onChange: (value: string) => void
  type?: string
  placeholder?: string
  required?: boolean
}) {
  return (
    <div className="donaton-field">
      <label htmlFor={id}>{label}</label>
      <input
        id={id}
        name={id}
        type={type}
        required={required}
        placeholder={placeholder}
        value={value}
        onChange={(e) => onChange(e.target.value)}
      />
    </div>
  )
}
