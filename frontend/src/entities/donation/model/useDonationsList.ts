import { useCallback, useEffect, useState } from 'react'
import { ApiError } from '@/shared/api/client'
import { getDonationRepository } from '../api/repository'
import type { Donacion } from './types'

export function useDonationsList() {
  const repository = getDonationRepository()
  const [donaciones, setDonaciones] = useState<Donacion[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await repository.list()
      setDonaciones(data)
    } catch (err) {
      setDonaciones([])
      setError(
        err instanceof ApiError
          ? err.message
          : 'No se pudieron cargar las donaciones. Inténtalo de nuevo en unos instantes.',
      )
    } finally {
      setLoading(false)
    }
  }, [repository])

  useEffect(() => {
    void load()
  }, [load])

  return {
    donaciones,
    loading,
    error,
    load,
  }
}
