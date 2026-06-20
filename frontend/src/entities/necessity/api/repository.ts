import { env } from '@/shared/config/env'
import { getStoredToken } from '@/shared/lib/authStorage'
import type { CreateNecesidadRequest, Necesidad } from '../model/types'

export interface NecessityRepository {
  list(): Promise<Necesidad[]>
  getById(id: number): Promise<Necesidad>
  create(payload: CreateNecesidadRequest): Promise<Necesidad>
  update(id: number, payload: CreateNecesidadRequest): Promise<Necesidad>
  remove(id: number): Promise<void>
}

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

function createApiRepository(baseUrl: string): NecessityRepository {
  function errorMessage(status: number, body: unknown) {
    if (body && typeof body === 'object' && 'message' in body) {
      const message = (body as { message: unknown }).message
      if (typeof message === 'string' && message.trim()) return message
    }
    return `Error ${status}`
  }

  async function request<T>(path: string, init?: RequestInit): Promise<T> {
    const token = getStoredToken()
    const response = await fetch(`${baseUrl}${path}`, {
      ...init,
      headers: {
        Accept: 'application/json',
        ...(init?.body ? { 'Content-Type': 'application/json' } : {}),
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        ...(init?.headers ?? {}),
      },
    })

    const text = await response.text()
    let body: unknown = null
    if (text) {
      try {
        body = JSON.parse(text) as unknown
      } catch {
        body = text
      }
    }

    if (!response.ok) {
      throw new Error(errorMessage(response.status, body))
    }

    if (response.status === 204) {
      return undefined as T
    }

    return body as T
  }

  return {
    list: () => request<Necesidad[]>('/api/v1/necessities'),
    getById: (id) => request<Necesidad>(`/api/v1/necessities/${id}`),
    create: (payload) =>
      request<Necesidad>('/api/v1/necessities', {
        method: 'POST',
        body: JSON.stringify(payload),
      }),
    update: (id, payload) =>
      request<Necesidad>(`/api/v1/necessities/${id}`, {
        method: 'PUT',
        body: JSON.stringify(payload),
      }),
    remove: (id) =>
      request<void>(`/api/v1/necessities/${id}`, {
        method: 'DELETE',
      }),
  }
}

const repository =
  env.necessityApiBaseUrl && env.necessityApiBaseUrl.trim()
    ? createApiRepository(env.necessityApiBaseUrl)
    : mockRepository

export function getNecessityRepository(): NecessityRepository {
  return repository
}
