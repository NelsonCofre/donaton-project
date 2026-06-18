export type NecessityPriority = 'BAJA' | 'MEDIA' | 'ALTA' | 'CRITICA'
export type NecessityStatus = 'ABIERTA' | 'EN_PROCESO' | 'CUBIERTA' | 'CANCELADA'
export type NecessitySortField = 'fechaReporte' | 'cantidad' | 'prioridad'
export type NecessitySortDirection = 'asc' | 'desc'

export interface NecessityFilters {
  query: string
  estado: NecessityStatus | 'TODOS'
  prioridad: NecessityPriority | 'TODAS'
  sortBy: NecessitySortField
  sortDirection: NecessitySortDirection
}

export interface Necesidad {
  idNecesidad: number
  titulo: string
  descripcion: string
  recurso: string
  cantidad: number
  prioridad: NecessityPriority
  estado: NecessityStatus
  ubicacion: string
  fechaReporte: string
}

export interface CreateNecesidadRequest {
  titulo: string
  descripcion: string
  recurso: string
  cantidad: number
  prioridad: NecessityPriority
  estado: NecessityStatus
  ubicacion: string
  fechaReporte: string
}
