import { useState } from 'react'
import type { CreateCollectionCenterRequest } from '@/entities/logistics'
import { FieldGroup, InlineMessage } from '@/shared/ui'

export type CollectionCenterFormValues = CreateCollectionCenterRequest

const emptyValues: CollectionCenterFormValues = {
  nombre: '',
  ubicacion: '',
  responsable: '',
  telefono: '',
  capacidad: 100,
}

type CollectionCenterFormProps = {
  title: string
  submitLabel: string
  initialValues?: CollectionCenterFormValues
  error: string | null
  success: string | null
  loading: boolean
  onSubmit: (values: CollectionCenterFormValues) => void
  onCancel?: () => void
}

export function CollectionCenterForm({
  title,
  submitLabel,
  initialValues,
  error,
  success,
  loading,
  onSubmit,
  onCancel,
}: CollectionCenterFormProps) {
  const [values, setValues] = useState<CollectionCenterFormValues>(initialValues ?? emptyValues)

  function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    onSubmit({
      ...values,
      nombre: values.nombre.trim(),
      ubicacion: values.ubicacion.trim(),
      responsable: values.responsable.trim(),
      telefono: values.telefono.trim(),
      capacidad: Math.max(1, Math.floor(Number(values.capacidad))),
    })
  }

  return (
    <form className="donaton-form donaton-form--wide" onSubmit={handleSubmit}>
      <h2>{title}</h2>
      <FieldGroup>
        <InputField
          id="center-name"
          label="Nombre"
          value={values.nombre}
          onChange={(nombre) => setValues({ ...values, nombre })}
        />
        <InputField
          id="center-location"
          label="Ubicacion"
          value={values.ubicacion}
          onChange={(ubicacion) => setValues({ ...values, ubicacion })}
        />
      </FieldGroup>
      <FieldGroup>
        <InputField
          id="center-manager"
          label="Responsable"
          value={values.responsable}
          onChange={(responsable) => setValues({ ...values, responsable })}
        />
        <InputField
          id="center-phone"
          label="Telefono"
          value={values.telefono}
          onChange={(telefono) => setValues({ ...values, telefono })}
        />
      </FieldGroup>
      <div className="donaton-field">
        <label htmlFor="center-capacity">Capacidad</label>
        <input
          id="center-capacity"
          type="number"
          min={1}
          value={values.capacidad}
          onChange={(event) => setValues({ ...values, capacidad: Number(event.target.value) })}
        />
      </div>
      {error ? <InlineMessage tone="error">{error}</InlineMessage> : null}
      {success ? <InlineMessage tone="success">{success}</InlineMessage> : null}
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

function InputField({
  id,
  label,
  value,
  onChange,
}: {
  id: string
  label: string
  value: string | number
  onChange: (value: string) => void
}) {
  return (
    <div className="donaton-field">
      <label htmlFor={id}>{label}</label>
      <input id={id} value={value} onChange={(event) => onChange(event.target.value)} />
    </div>
  )
}
