import { beforeEach, describe, expect, it } from 'vitest'

import { clearStoredToken, getStoredToken, setStoredToken } from './authStorage'

describe('authStorage', () => {
  beforeEach(() => {
    localStorage.clear()
  })

  it('guarda y recupera el token', () => {
    setStoredToken('jwt-token')

    expect(getStoredToken()).toBe('jwt-token')
  })

  it('devuelve null cuando no hay token', () => {
    expect(getStoredToken()).toBeNull()
  })

  it('limpia el token almacenado', () => {
    setStoredToken('jwt-token')

    clearStoredToken()

    expect(getStoredToken()).toBeNull()
  })
})
