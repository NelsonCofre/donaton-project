import {
  createDonacion,
  deleteDonacion,
  fetchDonacionById,
  fetchDonaciones,
  updateDonacion,
} from './donationApi'
import type { CreateDonacionRequest, Donacion } from '../model/types'

export interface DonationRepository {
  list(): Promise<Donacion[]>
  getById(id: number): Promise<Donacion>
  create(payload: CreateDonacionRequest): Promise<Donacion>
  update(id: number, payload: CreateDonacionRequest): Promise<Donacion>
  remove(id: number): Promise<void>
}

const repository: DonationRepository = {
  list: fetchDonaciones,
  getById: fetchDonacionById,
  create: createDonacion,
  update: updateDonacion,
  remove: deleteDonacion,
}

export function getDonationRepository(): DonationRepository {
  return repository
}
