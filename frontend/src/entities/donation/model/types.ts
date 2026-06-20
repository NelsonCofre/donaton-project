/**
 * Modelo de dominio de donaciones según README (donante, recurso, donación, vínculo).
 * Valores de estado son orientativos hasta que el API los fije.
 */
export type DonacionEstado = 'PENDIENTE' | 'RECIBIDA' | 'ASIGNADA' | 'CANCELADA'
export type DonationSortField = 'id' | 'fecha' | 'cantidad'
export type DonationSortDirection = 'asc' | 'desc'

export interface DonationFilters {
  query: string
  estado: DonacionEstado | 'TODOS'
  sortBy: DonationSortField
  sortDirection: DonationSortDirection
}

export interface Donante {
  idDonante: number
  nombre: string
  contacto: string
}

export interface Recurso {
  idRecurso: number
  tipo: string
}

export interface Donacion {
  idDonacion: number
  fecha: string
  cantidad: number
  estado: DonacionEstado
  idDonante: number
  donante?: Donante
  recursoTipos?: string[]
}

/** Payload para alta desde UI (el BFF puede mapearlo a donante/recurso/donación). */
export interface CreateDonacionRequest {
  nombreDonante: string
  contactoDonante: string
  tipoRecurso: string
  cantidad: number
  fecha: string
}
