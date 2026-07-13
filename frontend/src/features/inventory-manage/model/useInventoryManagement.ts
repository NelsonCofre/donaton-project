import { useState } from 'react'
import {
  getLogisticsRepository,
  type CreateInventoryItemRequest,
  type InventoryItem,
} from '@/entities/logistics'

export function useInventoryManagement(onChanged: () => Promise<void>) {
  const repository = getLogisticsRepository()
  const [editing, setEditing] = useState<InventoryItem | null>(null)
  const [deletingId, setDeletingId] = useState<number | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)
  const [saving, setSaving] = useState(false)

  async function save(values: CreateInventoryItemRequest) {
    setError(null); setSuccess(null); setSaving(true)
    try {
      if (editing) {
        await repository.updateInventory(editing.idInventario, values)
        setSuccess('Inventario actualizado correctamente.')
        setEditing(null)
      } else {
        await repository.createInventory(values)
        setSuccess('Inventario creado correctamente.')
      }
      await onChanged()
    } catch (cause) {
      setError(cause instanceof Error ? cause.message : 'No se pudo guardar el inventario.')
    } finally { setSaving(false) }
  }

  async function remove(id: number) {
    if (!window.confirm('¿Eliminar este item de inventario?')) return
    setDeletingId(id)
    try {
      await repository.removeInventory(id)
      if (editing?.idInventario === id) setEditing(null)
      await onChanged()
    } catch (cause) {
      setError(cause instanceof Error ? cause.message : 'No se pudo eliminar el inventario.')
    } finally { setDeletingId(null) }
  }

  return { editing, setEditing, deletingId, error, success, saving, save, remove }
}
