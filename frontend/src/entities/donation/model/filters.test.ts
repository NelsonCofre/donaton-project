import { describe, expect, it } from 'vitest'
import { filterAndSortDonations, parseDonationFilters, serializeDonationFilters } from './filters'
import type { Donacion } from './types'

const donations: Donacion[] = [
  { idDonacion: 2, fecha: '2026-01-02', cantidad: 5, estado: 'RECIBIDA', idDonante: 2, donante: { idDonante: 2, nombre: 'Ana', contacto: 'ana@example.com' } },
  { idDonacion: 1, fecha: '2026-01-01', cantidad: 10, estado: 'PENDIENTE', idDonante: 1, recursoTipos: ['Agua'] },
]

describe('donation filters', () => {
  it('round-trips non-default URL filters', () => {
    const filters = parseDonationFilters(new URLSearchParams('q=ana&estado=RECIBIDA&sortBy=cantidad&sortDirection=asc'))
    expect(serializeDonationFilters(filters).toString()).toBe('q=ana&estado=RECIBIDA&sortBy=cantidad&sortDirection=asc')
  })

  it('filters and sorts without mutating the source', () => {
    const result = filterAndSortDonations(donations, { query: '', estado: 'TODOS', sortBy: 'id', sortDirection: 'asc' })
    expect(result.map((item) => item.idDonacion)).toEqual([1, 2])
    expect(donations.map((item) => item.idDonacion)).toEqual([2, 1])
  })
})
