import { useCallback, useEffect, useState } from 'react'
import { getLogisticsRepository } from '../api/repository'
import type { CollectionCenter } from './types'

export function useCollectionCentersList() {
  const repository = getLogisticsRepository()
  const [centers, setCenters] = useState<CollectionCenter[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await repository.listCenters()
      setCenters(data)
    } catch (err) {
      setCenters([])
      setError(err instanceof Error ? err.message : 'No se pudieron cargar los centros.')
    } finally {
      setLoading(false)
    }
  }, [repository])

  useEffect(() => {
    void load()
  }, [load])

  return { centers, loading, error, load }
}
