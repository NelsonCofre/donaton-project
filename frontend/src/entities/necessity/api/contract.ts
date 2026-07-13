import type { CreateNecesidadRequest, Necesidad } from '../model/types'
export interface NecessityRepository {
  list(): Promise<Necesidad[]>; getById(id: number): Promise<Necesidad>
  create(payload: CreateNecesidadRequest): Promise<Necesidad>
  update(id: number, payload: CreateNecesidadRequest): Promise<Necesidad>
  remove(id: number): Promise<void>
}
