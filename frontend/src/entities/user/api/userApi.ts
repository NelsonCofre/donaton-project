import { request } from '@/shared/api/client'
import type { AuthResponse, LoginRequest, RegisterRequest, User } from '../model/types'

/** Contrato orientado al BFF; ajustar rutas cuando el backend esté listo. */
export async function login(payload: LoginRequest): Promise<AuthResponse> {
  return request<AuthResponse>('/api/auth/login', {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}

export async function register(payload: RegisterRequest): Promise<User> {
  return request<User>('/api/auth/register', {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}
