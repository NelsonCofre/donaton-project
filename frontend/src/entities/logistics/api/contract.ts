import type { CollectionCenter, CreateCollectionCenterRequest, CreateInventoryItemRequest, CreateShipmentRequest, InventoryItem, Shipment } from '../model/types'
export interface LogisticsRepository {
  listCenters(): Promise<CollectionCenter[]>; getCenterById(id: number): Promise<CollectionCenter>
  createCenter(payload: CreateCollectionCenterRequest): Promise<CollectionCenter>; updateCenter(id: number, payload: CreateCollectionCenterRequest): Promise<CollectionCenter>; removeCenter(id: number): Promise<void>
  listInventory(): Promise<InventoryItem[]>; createInventory(payload: CreateInventoryItemRequest): Promise<InventoryItem>; updateInventory(id: number, payload: CreateInventoryItemRequest): Promise<InventoryItem>; removeInventory(id: number): Promise<void>
  listShipments(): Promise<Shipment[]>; createShipment(payload: CreateShipmentRequest): Promise<Shipment>; updateShipment(id: number, payload: CreateShipmentRequest): Promise<Shipment>; removeShipment(id: number): Promise<void>
}
