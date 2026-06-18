import { env } from '@/shared/config/env'
import type {
  CollectionCenter,
  CreateCollectionCenterRequest,
  CreateInventoryItemRequest,
  CreateShipmentRequest,
  InventoryItem,
  Shipment,
} from '../model/types'

export interface LogisticsRepository {
  listCenters(): Promise<CollectionCenter[]>
  getCenterById(id: number): Promise<CollectionCenter>
  createCenter(payload: CreateCollectionCenterRequest): Promise<CollectionCenter>
  updateCenter(id: number, payload: CreateCollectionCenterRequest): Promise<CollectionCenter>
  removeCenter(id: number): Promise<void>
  listInventory(): Promise<InventoryItem[]>
  createInventory(payload: CreateInventoryItemRequest): Promise<InventoryItem>
  updateInventory(id: number, payload: CreateInventoryItemRequest): Promise<InventoryItem>
  removeInventory(id: number): Promise<void>
  listShipments(): Promise<Shipment[]>
  createShipment(payload: CreateShipmentRequest): Promise<Shipment>
  updateShipment(id: number, payload: CreateShipmentRequest): Promise<Shipment>
  removeShipment(id: number): Promise<void>
}

let centers: CollectionCenter[] = [
  {
    idCentro: 1,
    nombre: 'Centro Viña Norte',
    ubicacion: 'Vina del Mar',
    responsable: 'Carla Muñoz',
    telefono: '+56 9 5555 1111',
    capacidad: 500,
  },
  {
    idCentro: 2,
    nombre: 'Centro Quilpue Solidario',
    ubicacion: 'Quilpue',
    responsable: 'Jose Reyes',
    telefono: '+56 9 5555 2222',
    capacidad: 320,
  },
]

let inventory: InventoryItem[] = [
  {
    idInventario: 1,
    idCentro: 1,
    centroNombre: 'Centro Viña Norte',
    recurso: 'Agua potable',
    cantidad: 180,
    updatedAt: '2026-06-15',
  },
  {
    idInventario: 2,
    idCentro: 2,
    centroNombre: 'Centro Quilpue Solidario',
    recurso: 'Frazadas',
    cantidad: 90,
    updatedAt: '2026-06-14',
  },
]

let shipments: Shipment[] = [
  {
    idEnvio: 1,
    idCentro: 1,
    centroNombre: 'Centro Viña Norte',
    destino: 'Albergue Miraflores',
    detalle: 'Despacho mixto de agua y alimentos',
    cantidad: 65,
    fecha: '2026-06-16',
    estado: 'EN_TRANSITO',
  },
  {
    idEnvio: 2,
    idCentro: 2,
    centroNombre: 'Centro Quilpue Solidario',
    destino: 'Escuela Canal Chacao',
    detalle: 'Frazadas y kits de higiene',
    cantidad: 40,
    fecha: '2026-06-15',
    estado: 'PREPARACION',
  },
]

function clone<T>(value: T): T {
  return JSON.parse(JSON.stringify(value)) as T
}

function nextId<T extends Record<K, number>, K extends keyof T>(values: T[], key: K) {
  return values.reduce((max, item) => Math.max(max, item[key]), 0) + 1
}

function getCenterName(idCentro: number) {
  return centers.find((center) => center.idCentro === idCentro)?.nombre ?? 'Centro sin nombre'
}

const mockRepository: LogisticsRepository = {
  async listCenters() {
    return clone(centers)
  },
  async getCenterById(id) {
    const center = centers.find((item) => item.idCentro === id)
    if (!center) throw new Error('Centro de acopio no encontrado.')
    return clone(center)
  },
  async createCenter(payload) {
    const created: CollectionCenter = {
      idCentro: nextId(centers, 'idCentro'),
      ...payload,
    }
    centers = [created, ...centers]
    return clone(created)
  },
  async updateCenter(id, payload) {
    const updated: CollectionCenter = { idCentro: id, ...payload }
    centers = centers.map((item) => (item.idCentro === id ? updated : item))
    inventory = inventory.map((item) =>
      item.idCentro === id ? { ...item, centroNombre: updated.nombre } : item,
    )
    shipments = shipments.map((item) =>
      item.idCentro === id ? { ...item, centroNombre: updated.nombre } : item,
    )
    return clone(updated)
  },
  async removeCenter(id) {
    centers = centers.filter((item) => item.idCentro !== id)
    inventory = inventory.filter((item) => item.idCentro !== id)
    shipments = shipments.filter((item) => item.idCentro !== id)
  },
  async listInventory() {
    return clone(inventory)
  },
  async createInventory(payload) {
    const created: InventoryItem = {
      idInventario: nextId(inventory, 'idInventario'),
      idCentro: payload.idCentro,
      centroNombre: getCenterName(payload.idCentro),
      recurso: payload.recurso,
      cantidad: payload.cantidad,
      updatedAt: new Date().toISOString().slice(0, 10),
    }
    inventory = [created, ...inventory]
    return clone(created)
  },
  async updateInventory(id, payload) {
    const updated: InventoryItem = {
      idInventario: id,
      idCentro: payload.idCentro,
      centroNombre: getCenterName(payload.idCentro),
      recurso: payload.recurso,
      cantidad: payload.cantidad,
      updatedAt: new Date().toISOString().slice(0, 10),
    }
    inventory = inventory.map((item) => (item.idInventario === id ? updated : item))
    return clone(updated)
  },
  async removeInventory(id) {
    inventory = inventory.filter((item) => item.idInventario !== id)
  },
  async listShipments() {
    return clone(shipments)
  },
  async createShipment(payload) {
    const created: Shipment = {
      idEnvio: nextId(shipments, 'idEnvio'),
      idCentro: payload.idCentro,
      centroNombre: getCenterName(payload.idCentro),
      destino: payload.destino,
      detalle: payload.detalle,
      cantidad: payload.cantidad,
      fecha: payload.fecha,
      estado: payload.estado,
    }
    shipments = [created, ...shipments]
    return clone(created)
  },
  async updateShipment(id, payload) {
    const updated: Shipment = {
      idEnvio: id,
      idCentro: payload.idCentro,
      centroNombre: getCenterName(payload.idCentro),
      destino: payload.destino,
      detalle: payload.detalle,
      cantidad: payload.cantidad,
      fecha: payload.fecha,
      estado: payload.estado,
    }
    shipments = shipments.map((item) => (item.idEnvio === id ? updated : item))
    return clone(updated)
  },
  async removeShipment(id) {
    shipments = shipments.filter((item) => item.idEnvio !== id)
  },
}

function createApiRepository(baseUrl: string): LogisticsRepository {
  async function request<T>(path: string, init?: RequestInit): Promise<T> {
    const response = await fetch(`${baseUrl}${path}`, {
      ...init,
      headers: {
        Accept: 'application/json',
        ...(init?.body ? { 'Content-Type': 'application/json' } : {}),
        ...(init?.headers ?? {}),
      },
    })

    if (!response.ok) {
      throw new Error(`Error ${response.status}`)
    }

    if (response.status === 204) {
      return undefined as T
    }

    return (await response.json()) as T
  }

  return {
    listCenters: () => request<CollectionCenter[]>('/api/v1/logistics/collection-centers'),
    getCenterById: (id) =>
      request<CollectionCenter>(`/api/v1/logistics/collection-centers/${id}`),
    createCenter: (payload) =>
      request<CollectionCenter>('/api/v1/logistics/collection-centers', {
        method: 'POST',
        body: JSON.stringify(payload),
      }),
    updateCenter: (id, payload) =>
      request<CollectionCenter>(`/api/v1/logistics/collection-centers/${id}`, {
        method: 'PUT',
        body: JSON.stringify(payload),
      }),
    removeCenter: (id) =>
      request<void>(`/api/v1/logistics/collection-centers/${id}`, { method: 'DELETE' }),
    listInventory: () => request<InventoryItem[]>('/api/v1/logistics/inventories'),
    createInventory: (payload) =>
      request<InventoryItem>('/api/v1/logistics/inventories', {
        method: 'POST',
        body: JSON.stringify(payload),
      }),
    updateInventory: (id, payload) =>
      request<InventoryItem>(`/api/v1/logistics/inventories/${id}`, {
        method: 'PUT',
        body: JSON.stringify(payload),
      }),
    removeInventory: (id) =>
      request<void>(`/api/v1/logistics/inventories/${id}`, { method: 'DELETE' }),
    listShipments: () => request<Shipment[]>('/api/v1/logistics/shipments'),
    createShipment: (payload) =>
      request<Shipment>('/api/v1/logistics/shipments', {
        method: 'POST',
        body: JSON.stringify(payload),
      }),
    updateShipment: (id, payload) =>
      request<Shipment>(`/api/v1/logistics/shipments/${id}`, {
        method: 'PUT',
        body: JSON.stringify(payload),
      }),
    removeShipment: (id) =>
      request<void>(`/api/v1/logistics/shipments/${id}`, { method: 'DELETE' }),
  }
}

const repository =
  env.logisticsApiBaseUrl && env.logisticsApiBaseUrl.trim()
    ? createApiRepository(env.logisticsApiBaseUrl)
    : mockRepository

export function getLogisticsRepository(): LogisticsRepository {
  return repository
}
