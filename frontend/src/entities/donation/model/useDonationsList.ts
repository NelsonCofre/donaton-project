import { useCallback, useEffect, useMemo, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { ApiError } from '@/shared/api/client'
import { getDonationRepository } from '../api/repository'
import type {
  DonationFilters,
  DonationSortDirection,
  DonationSortField,
  Donacion,
  DonacionEstado,
} from './types'

const initialFilters: DonationFilters = {
  query: '',
  estado: 'TODOS',
  sortBy: 'fecha',
  sortDirection: 'desc',
}

function getDonationSearchText(donacion: Donacion): string {
  return [
    donacion.idDonacion,
    donacion.donante?.nombre,
    donacion.donante?.contacto,
    donacion.recursoTipos?.join(' '),
  ]
    .filter(Boolean)
    .join(' ')
    .toLowerCase()
}

function sortDonaciones(donaciones: Donacion[], filters: DonationFilters): Donacion[] {
  return [...donaciones].sort((a, b) => {
    const direction = filters.sortDirection === 'asc' ? 1 : -1

    if (filters.sortBy === 'cantidad') {
      return (a.cantidad - b.cantidad) * direction
    }

    if (filters.sortBy === 'id') {
      return (a.idDonacion - b.idDonacion) * direction
    }

    return (new Date(a.fecha).getTime() - new Date(b.fecha).getTime()) * direction
  })
}

function getFiltersFromParams(searchParams: URLSearchParams): DonationFilters {
  const estado = searchParams.get('estado')
  const sortBy = searchParams.get('sortBy')
  const sortDirection = searchParams.get('sortDirection')

  return {
    query: searchParams.get('q') ?? initialFilters.query,
    estado:
      estado === 'PENDIENTE' ||
      estado === 'RECIBIDA' ||
      estado === 'ASIGNADA' ||
      estado === 'CANCELADA' ||
      estado === 'TODOS'
        ? estado
        : initialFilters.estado,
    sortBy: sortBy === 'cantidad' || sortBy === 'id' ? sortBy : initialFilters.sortBy,
    sortDirection: sortDirection === 'asc' ? sortDirection : initialFilters.sortDirection,
  }
}

export function useDonationsList() {
  const repository = getDonationRepository()
  const [searchParams, setSearchParams] = useSearchParams()
  const [donaciones, setDonaciones] = useState<Donacion[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const filters = useMemo(() => getFiltersFromParams(searchParams), [searchParams])

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

  const filteredDonaciones = useMemo(() => {
    const query = filters.query.trim().toLowerCase()
    const visible = donaciones.filter((donacion) => {
      const matchesQuery = query
        ? getDonationSearchText(donacion).includes(query)
        : true
      const matchesStatus =
        filters.estado === 'TODOS' ? true : donacion.estado === filters.estado

      return matchesQuery && matchesStatus
    })

    return sortDonaciones(visible, filters)
  }, [donaciones, filters])

  function updateFilters(next: Partial<DonationFilters>) {
    const merged = { ...filters, ...next }
    const params = new URLSearchParams()

    if (merged.query.trim()) params.set('q', merged.query.trim())
    if (merged.estado !== 'TODOS') params.set('estado', merged.estado)
    if (merged.sortBy !== 'fecha') params.set('sortBy', merged.sortBy)
    if (merged.sortDirection !== 'desc') params.set('sortDirection', merged.sortDirection)

    setSearchParams(params, { replace: true })
  }

  return {
    filteredDonaciones,
    filters,
    hasFilters: filters.query.trim() !== '' || filters.estado !== 'TODOS',
    loading,
    error,
    load,
    setQuery: (query: string) => updateFilters({ query }),
    setEstado: (estado: DonacionEstado | 'TODOS') => updateFilters({ estado }),
    setSort: (sortBy: DonationSortField, sortDirection: DonationSortDirection) =>
      updateFilters({ sortBy, sortDirection }),
  }
}
