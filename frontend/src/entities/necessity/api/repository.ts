import { env } from '@/shared/config/env'
import { createHttpNecessityRepository } from './httpRepository'
import type { NecessityRepository } from './contract'
import type { Necesidad } from '../model/types'

let mockData: Necesidad[] = [
  {
    idNecesidad: 1,
    titulo: 'Agua potable para Quilpue',
    descripcion: 'Se requieren bidones y botellas para centros temporales.',
    recurso: 'Agua potable',
    cantidad: 240,
    prioridad: 'CRITICA',
    estado: 'ABIERTA',
    ubicacion: 'Quilpue, Region de Valparaiso',
    fechaReporte: '2026-06-10',
  },
  {
    idNecesidad: 2,
    titulo: 'Kits de higiene familiar',
    descripcion: 'Apoyo para familias desplazadas por incendio.',
    recurso: 'Kit de higiene',
    cantidad: 80,
    prioridad: 'ALTA',
    estado: 'EN_PROCESO',
    ubicacion: 'Villa Alemana, Region de Valparaiso',
    fechaReporte: '2026-06-12',
  },
  {
    idNecesidad: 3,
    titulo: 'Abrigo nocturno',
    descripcion: 'Frazadas y ropa de invierno para albergues.',
    recurso: 'Frazadas',
    cantidad: 120,
    prioridad: 'MEDIA',
    estado: 'ABIERTA',
    ubicacion: 'Valparaiso, Region de Valparaiso',
    fechaReporte: '2026-06-14',
  },
]

function clone<T>(value: T): T {
  return JSON.parse(JSON.stringify(value)) as T
}

function getNextId() {
  return mockData.reduce((max, item) => Math.max(max, item.idNecesidad), 0) + 1
}

const mockRepository: NecessityRepository = {
  async list() {
    return clone(mockData)
  },
  async getById(id) {
    const item = mockData.find((entry) => entry.idNecesidad === id)
    if (!item) throw new Error('Necesidad no encontrada.')
    return clone(item)
  },
  async create(payload) {
    const created: Necesidad = {
      idNecesidad: getNextId(),
      ...payload,
    }
    mockData = [created, ...mockData]
    return clone(created)
  },
  async update(id, payload) {
    const index = mockData.findIndex((entry) => entry.idNecesidad === id)
    if (index < 0) throw new Error('Necesidad no encontrada.')
    const updated: Necesidad = { idNecesidad: id, ...payload }
    mockData = mockData.map((entry) => (entry.idNecesidad === id ? updated : entry))
    return clone(updated)
  },
  async remove(id) {
    mockData = mockData.filter((entry) => entry.idNecesidad !== id)
  },
}

const repository =
  env.necessityApiBaseUrl && env.necessityApiBaseUrl.trim()
    ? createHttpNecessityRepository(env.necessityApiBaseUrl)
    : mockRepository

export function getNecessityRepository(): NecessityRepository {
  return repository
}
