import { useState } from 'react'
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
      destino: '',
      detalle: '',
      cantidad: 1,
      fecha: new Date().toISOString().slice(0, 10),
      estado: 'PREPARACION',
    },
  )

  function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    onSubmit({
      ...values,
      destino: values.destino.trim(),
      detalle: values.detalle.trim(),
      cantidad: Math.max(1, Math.floor(Number(values.cantidad))),
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
      <FieldGroup>
        <div className="donaton-field">
          <label htmlFor="shipment-destination">Destino</label>
          <input
            id="shipment-destination"
            value={values.destino}
            onChange={(event) => setValues({ ...values, destino: event.target.value })}
          />
        </div>
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
      </FieldGroup>
      <div className="donaton-field">
        <label htmlFor="shipment-detail">Detalle</label>
        <input
          id="shipment-detail"
          value={values.detalle}
          onChange={(event) => setValues({ ...values, detalle: event.target.value })}
        />
      </div>
      <div className="donaton-field">
        <label htmlFor="shipment-amount">Cantidad</label>
        <input
          id="shipment-amount"
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
