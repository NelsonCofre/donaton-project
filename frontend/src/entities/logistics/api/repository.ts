import { env } from '@/shared/config/env'
import { createHttpLogisticsRepository } from './httpRepository'
import type { LogisticsRepository } from './contract'
import type {
  CollectionCenter,
  InventoryItem,
  Shipment,
} from '../model/types'

let centers: CollectionCenter[] = [
  {
    idCentro: 1,
    nombre: 'Centro Viña Norte',
    ubicacion: 'Vina del Mar',
  },
  {
    idCentro: 2,
    nombre: 'Centro Quilpue Solidario',
    ubicacion: 'Quilpue',
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
    fecha: '2026-06-16',
    estado: 'EN_TRANSITO',
  },
  {
    idEnvio: 2,
    idCentro: 2,
    centroNombre: 'Centro Quilpue Solidario',
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

const repository =
  env.logisticsApiBaseUrl && env.logisticsApiBaseUrl.trim()
    ? createHttpLogisticsRepository(env.logisticsApiBaseUrl)
    : mockRepository

export function getLogisticsRepository(): LogisticsRepository {
  return repository
}
