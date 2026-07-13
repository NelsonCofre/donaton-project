import { StatusBadge } from '@/shared/ui'
import type { DonacionEstado } from '../model/types'
export function DonationStatusChip({ estado }: { estado: DonacionEstado }) {
  return <StatusBadge label={estado} />
}
