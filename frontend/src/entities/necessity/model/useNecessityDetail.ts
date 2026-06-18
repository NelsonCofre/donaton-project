import { useCallback, useEffect, useState } from 'react'
import { getNecessityRepository } from '../api/repository'
import type { CreateNecesidadRequest, Necesidad } from './types'

export function useNecessityDetail(necessityId: number) {
  const repository = getNecessityRepository()
  const [necesidad, setNecesidad] = useState<Necesidad | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [editError, setEditError] = useState<string | null>(null)
  const [editSuccess, setEditSuccess] = useState<string | null>(null)
  const [editLoading, setEditLoading] = useState(false)
  const [deleteLoading, setDeleteLoading] = useState(false)

  const load = useCallback(async () => {
    if (!Number.isFinite(necessityId)) {
      setError('La necesidad solicitada no es válida.')
      setLoading(false)
      return
    }

    setLoading(true)
    setError(null)
    try {
      const data = await repository.getById(necessityId)
      setNecesidad(data)
    } catch (err) {
      setNecesidad(null)
      setError(err instanceof Error ? err.message : 'No se pudo cargar la necesidad.')
    } finally {
      setLoading(false)
    }
  }, [necessityId, repository])

  useEffect(() => {
    void load()
  }, [load])

  async function save(values: CreateNecesidadRequest) {
    if (!necesidad) return null

    setEditError(null)
    setEditSuccess(null)
    setEditLoading(true)
    try {
      const updated = await repository.update(necesidad.idNecesidad, values)
      setNecesidad(updated)
      setEditSuccess('Necesidad actualizada correctamente.')
      return updated
    } catch (err) {
      setEditError(err instanceof Error ? err.message : 'No se pudo actualizar la necesidad.')
      return null
    } finally {
      setEditLoading(false)
    }
  }

  async function remove() {
    if (!necesidad) return false

    setDeleteLoading(true)
    try {
      await repository.remove(necesidad.idNecesidad)
      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo eliminar la necesidad.')
      return false
    } finally {
      setDeleteLoading(false)
    }
  }

  return {
    necesidad,
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
