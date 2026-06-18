import { useCallback, useEffect, useState } from 'react'
import { getLogisticsRepository } from '../api/repository'
import type { InventoryItem } from './types'

export function useInventoriesList() {
  const repository = getLogisticsRepository()
  const [items, setItems] = useState<InventoryItem[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await repository.listInventory()
      setItems(data)
    } catch (err) {
      setItems([])
      setError(err instanceof Error ? err.message : 'No se pudo cargar el inventario.')
    } finally {
      setLoading(false)
    }
  }, [repository])

  useEffect(() => {
    void load()
  }, [load])

  return { items, loading, error, load }
}
