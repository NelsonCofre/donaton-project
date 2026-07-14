import { useEffect, useState } from 'react'
import type {
  CollectionCenter,
  CreateShipmentRequest,
  ShipmentStatus,
} from '@/entities/logistics'
import { FieldGroup, InlineMessage } from '@/shared/ui'

const shipmentStates: ShipmentStatus[] = [
  'PREPARACION',
  'EN_TRANSITO',
  'ENTREGADO',
  'CANCELADO',
]

type ShipmentFormProps = {
  centers: CollectionCenter[]
  initialValues?: CreateShipmentRequest
  error: string | null
  success: string | null
  loading: boolean
  submitLabel: string
  onSubmit: (values: CreateShipmentRequest) => void
  onCancel?: () => void
}

export function ShipmentForm({
  centers,
  initialValues,
  error,
  success,
  loading,
  submitLabel,
  onSubmit,
  onCancel,
}: ShipmentFormProps) {
  const [values, setValues] = useState<CreateShipmentRequest>(
    initialValues ?? {
      idCentro: centers[0]?.idCentro ?? 0,
      fecha: new Date().toISOString().slice(0, 10),
      estado: 'PREPARACION',
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
    })
  }

  return (
    <form className="donaton-form donaton-form--wide" onSubmit={handleSubmit}>
      <FieldGroup>
        <div className="donaton-field">
          <label htmlFor="shipment-center">Centro</label>
          <select
            id="shipment-center"
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
          <label htmlFor="shipment-date">Fecha</label>
          <input
            id="shipment-date"
            type="date"
            value={values.fecha}
            onChange={(event) => setValues({ ...values, fecha: event.target.value })}
          />
        </div>
      </FieldGroup>
      <div className="donaton-field">
        <label htmlFor="shipment-status">Estado</label>
        <select
          id="shipment-status"
          value={values.estado}
          onChange={(event) =>
            setValues({ ...values, estado: event.target.value as ShipmentStatus })
          }
        >
          {shipmentStates.map((state) => (
            <option key={state} value={state}>
              {state}
            </option>
          ))}
        </select>
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
