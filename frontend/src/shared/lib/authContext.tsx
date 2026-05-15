import {
  useCallback,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from 'react'
import { AuthContext } from '@/shared/lib/auth-context'
import { AUTH_LOGOUT_EVENT } from '@/shared/lib/authEvents'
import {
  clearStoredToken,
  getStoredToken,
  setStoredToken,
} from '@/shared/lib/authStorage'

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setTokenState] = useState<string | null>(() => getStoredToken())

  const setToken = useCallback((value: string) => {
    setStoredToken(value)
    setTokenState(value)
  }, [])

  const clearToken = useCallback(() => {
    clearStoredToken()
    setTokenState(null)
  }, [])

  useEffect(() => {
    const onLogout = () => setTokenState(null)
    window.addEventListener(AUTH_LOGOUT_EVENT, onLogout)
    return () => window.removeEventListener(AUTH_LOGOUT_EVENT, onLogout)
  }, [])

  const value = useMemo(
    () => ({
      token,
      isAuthenticated: Boolean(token),
      setToken,
      clearToken,
    }),
    [token, setToken, clearToken],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
