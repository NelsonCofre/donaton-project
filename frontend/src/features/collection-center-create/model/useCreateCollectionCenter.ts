import { useState } from 'react'
import { getLogisticsRepository, type CreateCollectionCenterRequest } from '@/entities/logistics'

export function useCreateCollectionCenter(onCreated: () => Promise<void>) {
  const repository = getLogisticsRepository()
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)
  async function create(values: CreateCollectionCenterRequest) {
    setError(null); setSuccess(null); setLoading(true)
    try {
      await repository.createCenter(values)
      setSuccess('Centro creado correctamente.')
      await onCreated()
    } catch (cause) {
      setError(cause instanceof Error ? cause.message : 'No se pudo crear el centro.')
    } finally { setLoading(false) }
  }
  return { create, error, success, loading }
}
