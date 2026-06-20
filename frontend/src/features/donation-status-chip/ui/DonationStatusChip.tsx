import type { DonacionEstado } from '@/entities/donation'
import { StatusBadge } from '@/shared/ui'

type DonationStatusChipProps = {
  estado: DonacionEstado
}

export function DonationStatusChip({ estado }: DonationStatusChipProps) {
  return <StatusBadge label={estado} />
}
