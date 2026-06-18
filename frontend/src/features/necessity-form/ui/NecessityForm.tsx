import { useState } from 'react'
import type {
  CreateNecesidadRequest,
  NecessityPriority,
  NecessityStatus,
} from '@/entities/necessity'
import { FieldGroup, InlineMessage } from '@/shared/ui'

export type NecessityFormValues = CreateNecesidadRequest

const emptyValues: NecessityFormValues = {
  titulo: '',
  descripcion: '',
  recurso: '',
  cantidad: 1,
  prioridad: 'MEDIA',
  estado: 'ABIERTA',
  ubicacion: '',
  fechaReporte: new Date().toISOString().slice(0, 10),
}

type NecessityFormProps = {
  title: string
  submitLabel: string
  initialValues?: NecessityFormValues
  error: string | null
  success: string | null
  loading: boolean
  onSubmit: (values: NecessityFormValues) => void
  onCancel?: () => void
}

const priorityOptions: NecessityPriority[] = ['BAJA', 'MEDIA', 'ALTA', 'CRITICA']
const statusOptions: NecessityStatus[] = ['ABIERTA', 'EN_PROCESO', 'CUBIERTA', 'CANCELADA']

export function NecessityForm({
  title,
  submitLabel,
  initialValues,
  error,
  success,
  loading,
  onSubmit,
  onCancel,
}: NecessityFormProps) {
  const [values, setValues] = useState<NecessityFormValues>(initialValues ?? emptyValues)

  function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    onSubmit({
      ...values,
      titulo: values.titulo.trim(),
      descripcion: values.descripcion.trim(),
      recurso: values.recurso.trim(),
      ubicacion: values.ubicacion.trim(),
      cantidad: Math.max(1, Math.floor(Number(values.cantidad))),
    })
  }

  return (
    <form className="donaton-form donaton-form--wide" onSubmit={handleSubmit}>
      <h2>{title}</h2>
      <InputField
        id="necessity-title"
        label="Titulo"
        value={values.titulo}
        onChange={(titulo) => setValues({ ...values, titulo })}
      />
      <InputField
        id="necessity-description"
        label="Descripcion"
        value={values.descripcion}
        onChange={(descripcion) => setValues({ ...values, descripcion })}
      />
      <FieldGroup>
        <InputField
          id="necessity-resource"
          label="Recurso"
          value={values.recurso}
          onChange={(recurso) => setValues({ ...values, recurso })}
        />
        <InputField
          id="necessity-location"
          label="Ubicacion"
          value={values.ubicacion}
          onChange={(ubicacion) => setValues({ ...values, ubicacion })}
        />
      </FieldGroup>
      <FieldGroup>
        <div className="donaton-field">
          <label htmlFor="necessity-amount">Cantidad</label>
          <input
            id="necessity-amount"
            type="number"
            min={1}
            value={values.cantidad}
            onChange={(event) => setValues({ ...values, cantidad: Number(event.target.value) })}
          />
        </div>
        <div className="donaton-field">
          <label htmlFor="necessity-date">Fecha de reporte</label>
          <input
            id="necessity-date"
            type="date"
            value={values.fechaReporte}
            onChange={(event) => setValues({ ...values, fechaReporte: event.target.value })}
          />
        </div>
      </FieldGroup>
      <FieldGroup>
        <div className="donaton-field">
          <label htmlFor="necessity-priority">Prioridad</label>
          <select
            id="necessity-priority"
            value={values.prioridad}
            onChange={(event) =>
              setValues({ ...values, prioridad: event.target.value as NecessityPriority })
            }
          >
            {priorityOptions.map((option) => (
              <option key={option} value={option}>
                {option}
              </option>
            ))}
          </select>
        </div>
        <div className="donaton-field">
          <label htmlFor="necessity-status">Estado</label>
          <select
            id="necessity-status"
            value={values.estado}
            onChange={(event) =>
              setValues({ ...values, estado: event.target.value as NecessityStatus })
            }
          >
            {statusOptions.map((option) => (
              <option key={option} value={option}>
                {option}
              </option>
            ))}
          </select>
        </div>
      </FieldGroup>
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
