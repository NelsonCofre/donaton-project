import { useCallback, useEffect, useState } from 'react'
import { getLogisticsRepository } from '../api/repository'
import type { Shipment } from './types'

export function useShipmentsList() {
  const repository = getLogisticsRepository()
  const [items, setItems] = useState<Shipment[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await repository.listShipments()
      setItems(data)
    } catch (err) {
      setItems([])
      setError(err instanceof Error ? err.message : 'No se pudieron cargar los envios.')
    } finally {
      setLoading(false)
    }
  }, [repository])

  useEffect(() => {
    void load()
  }, [load])

  return { items, loading, error, load }
}
