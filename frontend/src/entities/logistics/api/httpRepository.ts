import { request } from '@/shared/api/client'
import type { CollectionCenter, InventoryItem, Shipment } from '../model/types'
import type { LogisticsRepository } from './contract'

export function createHttpLogisticsRepository(baseUrl: string): LogisticsRepository {
  const call = <T,>(path: string, init?: RequestInit) => request<T>(path, init, baseUrl)
  return {
    listCenters: () => call('/api/v1/logistics/collection-centers'),
    getCenterById: (id) => call(`/api/v1/logistics/collection-centers/${id}`),
    createCenter: (payload) => call<CollectionCenter>('/api/v1/logistics/collection-centers', { method: 'POST', body: JSON.stringify(payload) }),
    updateCenter: (id, payload) => call<CollectionCenter>(`/api/v1/logistics/collection-centers/${id}`, { method: 'PUT', body: JSON.stringify(payload) }),
    removeCenter: (id) => call(`/api/v1/logistics/collection-centers/${id}`, { method: 'DELETE' }),
    listInventory: () => call('/api/v1/logistics/inventories'),
    createInventory: (payload) => call<InventoryItem>('/api/v1/logistics/inventories', { method: 'POST', body: JSON.stringify(payload) }),
    updateInventory: (id, payload) => call<InventoryItem>(`/api/v1/logistics/inventories/${id}`, { method: 'PUT', body: JSON.stringify(payload) }),
    removeInventory: (id) => call(`/api/v1/logistics/inventories/${id}`, { method: 'DELETE' }),
    listShipments: () => call('/api/v1/logistics/shipments'),
    createShipment: (payload) => call<Shipment>('/api/v1/logistics/shipments', { method: 'POST', body: JSON.stringify(payload) }),
    updateShipment: (id, payload) => call<Shipment>(`/api/v1/logistics/shipments/${id}`, { method: 'PUT', body: JSON.stringify(payload) }),
    removeShipment: (id) => call(`/api/v1/logistics/shipments/${id}`, { method: 'DELETE' }),
  }
}
