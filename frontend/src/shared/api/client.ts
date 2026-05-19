import { env } from '@/shared/config/env'
import { AUTH_LOGOUT_EVENT } from '@/shared/lib/authEvents'
import { clearStoredToken, getStoredToken } from '@/shared/lib/authStorage'

export class ApiError extends Error {
  status: number
  body: unknown

  constructor(message: string, status: number, body?: unknown) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.body = body
  }
}

function getErrorMessage(status: number, body: unknown): string {
  if (body && typeof body === 'object' && 'message' in body) {
    const msg = (body as { message: unknown }).message
    if (typeof msg === 'string' && msg.trim()) return msg
  }
  if (status === 0) return 'No hay conexión con el servidor.'
  return `Error ${status}`
}

export async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const url = `${env.apiBaseUrl}${path}`
  const token = getStoredToken()
  const headers: HeadersInit = {
    Accept: 'application/json',
    ...(init?.body ? { 'Content-Type': 'application/json' } : {}),
    ...(init?.headers ?? {}),
  }
  if (token) {
    ;(headers as Record<string, string>)['Authorization'] = `Bearer ${token}`
  }

  let res: Response
  try {
    res = await fetch(url, { ...init, headers })
  } catch {
    throw new ApiError('No hay conexión con el servidor.', 0)
  }

  const text = await res.text()
  let body: unknown = null
  if (text) {
    try {
      body = JSON.parse(text) as unknown
    } catch {
      body = text
    }
  }

  if (res.status === 401) {
    clearStoredToken()
    window.dispatchEvent(new Event(AUTH_LOGOUT_EVENT))
  }

  if (!res.ok) {
    throw new ApiError(getErrorMessage(res.status, body), res.status, body)
  }

  return body as T
}
