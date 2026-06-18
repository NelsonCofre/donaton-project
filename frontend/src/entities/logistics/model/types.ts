export type ShipmentStatus = 'PREPARACION' | 'EN_TRANSITO' | 'ENTREGADO' | 'CANCELADO'

export interface CollectionCenter {
  idCentro: number
  nombre: string
  ubicacion: string
  responsable: string
  telefono: string
  capacidad: number
}

export interface InventoryItem {
  idInventario: number
  idCentro: number
  centroNombre: string
  recurso: string
  cantidad: number
  updatedAt: string
}

export interface Shipment {
  idEnvio: number
  idCentro: number
  centroNombre: string
  destino: string
  detalle: string
  cantidad: number
  fecha: string
  estado: ShipmentStatus
}

export interface CreateCollectionCenterRequest {
  nombre: string
  ubicacion: string
  responsable: string
  telefono: string
  capacidad: number
}

export interface CreateInventoryItemRequest {
  idCentro: number
  recurso: string
  cantidad: number
}

export interface CreateShipmentRequest {
  idCentro: number
  destino: string
  detalle: string
  cantidad: number
  fecha: string
  estado: ShipmentStatus
}
