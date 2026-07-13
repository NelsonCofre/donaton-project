import { request } from '@/shared/api/client'
import type { Necesidad } from '../model/types'
import type { NecessityRepository } from './contract'
export function createHttpNecessityRepository(baseUrl: string): NecessityRepository {
  const call = <T,>(path: string, init?: RequestInit) => request<T>(path, init, baseUrl)
  return {
    list: () => call('/api/v1/necessities'),
    getById: (id) => call(`/api/v1/necessities/${id}`),
    create: (payload) => call<Necesidad>('/api/v1/necessities', { method: 'POST', body: JSON.stringify(payload) }),
    update: (id, payload) => call<Necesidad>(`/api/v1/necessities/${id}`, { method: 'PUT', body: JSON.stringify(payload) }),
    remove: (id) => call(`/api/v1/necessities/${id}`, { method: 'DELETE' }),
  }
}
