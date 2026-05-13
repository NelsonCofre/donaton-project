import { request } from '@/shared/api/client'
import type { CreateDonacionRequest, Donacion } from '../model/types'

export async function fetchDonaciones(): Promise<Donacion[]> {
  return request<Donacion[]>('/api/donations', { method: 'GET' })
}

export async function createDonacion(
  payload: CreateDonacionRequest,
): Promise<Donacion> {
  return request<Donacion>('/api/donations', {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}
