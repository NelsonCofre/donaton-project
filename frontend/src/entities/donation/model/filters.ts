import type { DonationFilters, Donacion } from './types'

export const defaultDonationFilters: DonationFilters = {
  query: '',
  estado: 'TODOS',
  sortBy: 'fecha',
  sortDirection: 'desc',
}

export function parseDonationFilters(params: URLSearchParams): DonationFilters {
  const estado = params.get('estado')
  const sortBy = params.get('sortBy')
  const sortDirection = params.get('sortDirection')

  return {
    query: params.get('q') ?? defaultDonationFilters.query,
    estado:
      estado === 'PENDIENTE' || estado === 'RECIBIDA' || estado === 'ASIGNADA' ||
      estado === 'CANCELADA' || estado === 'TODOS'
        ? estado
        : defaultDonationFilters.estado,
    sortBy: sortBy === 'cantidad' || sortBy === 'id' ? sortBy : defaultDonationFilters.sortBy,
    sortDirection: sortDirection === 'asc' ? 'asc' : defaultDonationFilters.sortDirection,
  }
}

export function serializeDonationFilters(filters: DonationFilters): URLSearchParams {
  const params = new URLSearchParams()
  if (filters.query.trim()) params.set('q', filters.query.trim())
  if (filters.estado !== 'TODOS') params.set('estado', filters.estado)
  if (filters.sortBy !== 'fecha') params.set('sortBy', filters.sortBy)
  if (filters.sortDirection !== 'desc') params.set('sortDirection', filters.sortDirection)
  return params
}

export function filterAndSortDonations(
  donations: Donacion[],
  filters: DonationFilters,
): Donacion[] {
  const query = filters.query.trim().toLowerCase()
  const direction = filters.sortDirection === 'asc' ? 1 : -1
  return donations
    .filter((donation) => {
      const text = [
        donation.idDonacion,
        donation.donante?.nombre,
        donation.donante?.contacto,
        donation.recursoTipos?.join(' '),
      ].filter(Boolean).join(' ').toLowerCase()
      return (!query || text.includes(query)) &&
        (filters.estado === 'TODOS' || donation.estado === filters.estado)
    })
    .sort((a, b) => {
      if (filters.sortBy === 'cantidad') return (a.cantidad - b.cantidad) * direction
      if (filters.sortBy === 'id') return (a.idDonacion - b.idDonacion) * direction
      return (new Date(a.fecha).getTime() - new Date(b.fecha).getTime()) * direction
    })
}
