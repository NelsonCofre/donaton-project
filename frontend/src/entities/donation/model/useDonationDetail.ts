import { useCallback, useEffect, useState } from 'react'
import { ApiError } from '@/shared/api/client'
import { getDonationRepository } from '../api/repository'
import type { CreateDonacionRequest, Donacion } from './types'

export function useDonationDetail(donationId: number) {
  const repository = getDonationRepository()
  const [donacion, setDonacion] = useState<Donacion | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [editError, setEditError] = useState<string | null>(null)
  const [editSuccess, setEditSuccess] = useState<string | null>(null)
  const [editLoading, setEditLoading] = useState(false)
  const [deleteLoading, setDeleteLoading] = useState(false)

  const load = useCallback(async () => {
    if (!Number.isFinite(donationId)) {
      setError('La donación solicitada no es válida.')
      setLoading(false)
      return
    }

    setLoading(true)
    setError(null)
    try {
      const data = await repository.getById(donationId)
      setDonacion(data)
    } catch (err) {
      setDonacion(null)
      setError(
        err instanceof ApiError
          ? err.message
          : 'No se pudo cargar el detalle de la donación.',
      )
    } finally {
      setLoading(false)
    }
  }, [donationId, repository])

  useEffect(() => {
    void load()
  }, [load])

  async function save(values: CreateDonacionRequest) {
    if (!donacion) return null

    setEditError(null)
    setEditSuccess(null)
    setEditLoading(true)
    try {
      const updated = await repository.update(donacion.idDonacion, {
        ...values,
        fecha: new Date(values.fecha).toISOString(),
      })
      setDonacion(updated)
      setEditSuccess('Donación actualizada correctamente.')
      return updated
    } catch (err) {
      setEditError(
        err instanceof ApiError ? err.message : 'No se pudo actualizar la donación.',
      )
      return null
    } finally {
      setEditLoading(false)
    }
  }

  async function remove() {
    if (!donacion) return false

    setDeleteLoading(true)
    setError(null)
    try {
      await repository.remove(donacion.idDonacion)
      return true
    } catch (err) {
      setError(err instanceof ApiError ? err.message : 'No se pudo eliminar la donación.')
      return false
    } finally {
      setDeleteLoading(false)
    }
  }

  return {
    donacion,
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
