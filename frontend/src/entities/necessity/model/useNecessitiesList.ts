import { useCallback, useEffect, useMemo, useState } from 'react'
import { getNecessityRepository } from '../api/repository'
import type { NecessityFilters, Necesidad } from './types'

const initialFilters: NecessityFilters = {
  query: '',
  estado: 'TODOS',
  prioridad: 'TODAS',
  sortBy: 'fechaReporte',
  sortDirection: 'desc',
}

function getSearchText(necesidad: Necesidad): string {
  return [
    necesidad.titulo,
    necesidad.descripcion,
    necesidad.recurso,
    necesidad.ubicacion,
  ]
    .join(' ')
    .toLowerCase()
}

export function useNecessitiesList() {
  const repository = getNecessityRepository()
  const [items, setItems] = useState<Necesidad[]>([])
  const [filters, setFilters] = useState<NecessityFilters>(initialFilters)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await repository.list()
      setItems(data)
    } catch (err) {
      setItems([])
      setError(err instanceof Error ? err.message : 'No se pudieron cargar las necesidades.')
    } finally {
      setLoading(false)
    }
  }, [repository])

  useEffect(() => {
    void load()
  }, [load])

  const filteredItems = useMemo(() => {
    const query = filters.query.trim().toLowerCase()
    const visible = items.filter((item) => {
      const matchesQuery = query ? getSearchText(item).includes(query) : true
      const matchesStatus = filters.estado === 'TODOS' || item.estado === filters.estado
      const matchesPriority =
        filters.prioridad === 'TODAS' || item.prioridad === filters.prioridad

      return matchesQuery && matchesStatus && matchesPriority
    })

    return [...visible].sort((a, b) => {
      const direction = filters.sortDirection === 'asc' ? 1 : -1

      if (filters.sortBy === 'cantidad') return (a.cantidad - b.cantidad) * direction
      if (filters.sortBy === 'prioridad') {
        const order = ['BAJA', 'MEDIA', 'ALTA', 'CRITICA']
        return (order.indexOf(a.prioridad) - order.indexOf(b.prioridad)) * direction
      }

      return (
        (new Date(a.fechaReporte).getTime() - new Date(b.fechaReporte).getTime()) *
        direction
      )
    })
  }, [filters, items])

  return {
    filteredNecesidades: filteredItems,
    filters,
    hasFilters:
      filters.query.trim() !== '' ||
      filters.estado !== 'TODOS' ||
      filters.prioridad !== 'TODAS',
    loading,
    error,
    load,
    setFilters,
  }
}
