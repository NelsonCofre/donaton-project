import { useEffect, useState } from 'react'
import type { CollectionCenter, CreateInventoryItemRequest } from '@/entities/logistics'
import { InlineMessage } from '@/shared/ui'

type InventoryFormProps = {
  centers: CollectionCenter[]
  initialValues?: CreateInventoryItemRequest
  error: string | null
  success: string | null
  loading: boolean
  submitLabel: string
  onSubmit: (values: CreateInventoryItemRequest) => void
  onCancel?: () => void
}

export function InventoryForm({
  centers,
  initialValues,
  error,
  success,
  loading,
  submitLabel,
  onSubmit,
  onCancel,
}: InventoryFormProps) {
  const [values, setValues] = useState<CreateInventoryItemRequest>(
    initialValues ?? {
      idCentro: centers[0]?.idCentro ?? 0,
      recurso: '',
      cantidad: 1,
    },
  )

  useEffect(() => {
    if (initialValues || centers.length === 0) return
    const centerExists = centers.some((center) => center.idCentro === values.idCentro)
    if (!centerExists) {
      setValues((current) => ({
        ...current,
        idCentro: centers[0].idCentro,
      }))
    }
  }, [centers, initialValues, values.idCentro])

  function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    const selectedCenter = centers.some((center) => center.idCentro === values.idCentro)
    if (!selectedCenter) return
    onSubmit({
      ...values,
      recurso: values.recurso.trim(),
      cantidad: Math.max(1, Math.floor(Number(values.cantidad))),
    })
  }

  return (
    <form className="donaton-form" onSubmit={handleSubmit}>
      <div className="donaton-field">
        <label htmlFor="inventory-center">Centro</label>
        <select
          id="inventory-center"
          value={values.idCentro}
          onChange={(event) => setValues({ ...values, idCentro: Number(event.target.value) })}
        >
          {centers.map((center) => (
            <option key={center.idCentro} value={center.idCentro}>
              {center.nombre}
            </option>
          ))}
        </select>
      </div>
      <div className="donaton-field">
        <label htmlFor="inventory-resource">Recurso</label>
        <input
          id="inventory-resource"
          value={values.recurso}
          onChange={(event) => setValues({ ...values, recurso: event.target.value })}
        />
      </div>
      <div className="donaton-field">
        <label htmlFor="inventory-amount">Cantidad</label>
        <input
          id="inventory-amount"
          type="number"
          min={1}
          value={values.cantidad}
          onChange={(event) => setValues({ ...values, cantidad: Number(event.target.value) })}
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
        <button className="donaton-btn" type="submit" disabled={loading || centers.length === 0}>
          {loading ? 'Guardando…' : submitLabel}
        </button>
      </div>
    </form>
  )
}
