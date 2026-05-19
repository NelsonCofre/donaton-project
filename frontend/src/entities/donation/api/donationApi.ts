import { request } from '@/shared/api/client'
import type { CreateDonacionRequest, Donacion } from '../model/types'

export async function fetchDonaciones(): Promise<Donacion[]> {
  return request<Donacion[]>('/api/donations', { method: 'GET' })
}

export async function fetchDonacionById(id: number): Promise<Donacion> {
  return request<Donacion>(`/api/donations/${id}`, { method: 'GET' })
}

export async function createDonacion(
  payload: CreateDonacionRequest,
): Promise<Donacion> {
  return request<Donacion>('/api/donations', {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}

export async function updateDonacion(
  id: number,
  payload: CreateDonacionRequest,
): Promise<Donacion> {
  return request<Donacion>(`/api/donations/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  })
}

export async function deleteDonacion(id: number): Promise<void> {
  await request<void>(`/api/donations/${id}`, { method: 'DELETE' })
}
