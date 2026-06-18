import { useCallback, useEffect, useState } from 'react'
import { getLogisticsRepository } from '../api/repository'
import type { CollectionCenter, CreateCollectionCenterRequest } from './types'

export function useCollectionCenterDetail(centerId: number) {
  const repository = getLogisticsRepository()
  const [center, setCenter] = useState<CollectionCenter | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [editError, setEditError] = useState<string | null>(null)
  const [editSuccess, setEditSuccess] = useState<string | null>(null)
  const [editLoading, setEditLoading] = useState(false)
  const [deleteLoading, setDeleteLoading] = useState(false)

  const load = useCallback(async () => {
    if (!Number.isFinite(centerId)) {
      setError('El centro solicitado no es válido.')
      setLoading(false)
      return
    }

    setLoading(true)
    setError(null)
    try {
      const data = await repository.getCenterById(centerId)
      setCenter(data)
    } catch (err) {
      setCenter(null)
      setError(err instanceof Error ? err.message : 'No se pudo cargar el centro.')
    } finally {
      setLoading(false)
    }
  }, [centerId, repository])

  useEffect(() => {
    void load()
  }, [load])

  async function save(values: CreateCollectionCenterRequest) {
    if (!center) return null

    setEditError(null)
    setEditSuccess(null)
    setEditLoading(true)
    try {
      const updated = await repository.updateCenter(center.idCentro, values)
      setCenter(updated)
      setEditSuccess('Centro actualizado correctamente.')
      return updated
    } catch (err) {
      setEditError(err instanceof Error ? err.message : 'No se pudo actualizar el centro.')
      return null
    } finally {
      setEditLoading(false)
    }
  }

  async function remove() {
    if (!center) return false

    setDeleteLoading(true)
    try {
      await repository.removeCenter(center.idCentro)
      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo eliminar el centro.')
      return false
    } finally {
      setDeleteLoading(false)
    }
  }

  return {
    center,
    loading,
    error,
    editError,
    editSuccess,
    editLoading,
    deleteLoading,
    load,
    save,
    remove,
  }
}
