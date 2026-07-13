import { useState } from 'react'
import {
  getLogisticsRepository,
  type CreateShipmentRequest,
  type Shipment,
} from '@/entities/logistics'

export function useShipmentManagement(onChanged: () => Promise<void>) {
  const repository = getLogisticsRepository()
  const [editing, setEditing] = useState<Shipment | null>(null)
  const [deletingId, setDeletingId] = useState<number | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)
  const [saving, setSaving] = useState(false)

  async function save(values: CreateShipmentRequest) {
    setError(null); setSuccess(null); setSaving(true)
    try {
      if (editing) {
        await repository.updateShipment(editing.idEnvio, values)
        setSuccess('Envio actualizado correctamente.')
        setEditing(null)
      } else {
        await repository.createShipment(values)
        setSuccess('Envio creado correctamente.')
      }
      await onChanged()
    } catch (cause) {
      setError(cause instanceof Error ? cause.message : 'No se pudo guardar el envio.')
    } finally { setSaving(false) }
  }

  async function remove(id: number) {
    if (!window.confirm('¿Eliminar este envio?')) return
    setDeletingId(id)
    try {
      await repository.removeShipment(id)
      if (editing?.idEnvio === id) setEditing(null)
      await onChanged()
    } catch (cause) {
      setError(cause instanceof Error ? cause.message : 'No se pudo eliminar el envio.')
    } finally { setDeletingId(null) }
  }

  return { editing, setEditing, deletingId, error, success, saving, save, remove }
}
