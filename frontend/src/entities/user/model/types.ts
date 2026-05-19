/** Rol de usuario según modelo del informe (README raíz). */
export type UserRole = 'ADMIN' | 'USER'

/** Entidad Usuario: id_usuario, email, password (no expuesto en cliente), rol. */
export interface User {
  idUsuario: number
  email: string
  rol: UserRole
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  email: string
  password: string
  rol: UserRole
}

export interface AuthResponse {
  token: string
  user: User
}
