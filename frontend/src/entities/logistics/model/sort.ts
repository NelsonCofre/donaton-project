import type { InventoryItem, Shipment } from './types'

export const sortInventoryByUpdatedDate = (items: InventoryItem[]) =>
  [...items].sort((a, b) => b.updatedAt.localeCompare(a.updatedAt))

export const sortShipmentsByDate = (items: Shipment[]) =>
  [...items].sort((a, b) => b.fecha.localeCompare(a.fecha))
