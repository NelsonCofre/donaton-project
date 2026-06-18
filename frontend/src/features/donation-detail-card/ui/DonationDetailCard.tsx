import type { Donacion } from '@/entities/donation'
import { DonationStatusChip } from '@/features/donation-status-chip'
import { InfoRow } from '@/shared/ui'

type DonationDetailCardProps = {
  donacion: Donacion
}

function formatDate(iso: string): string {
  const date = new Date(iso)
  return Number.isNaN(date.getTime()) ? iso : date.toLocaleDateString()
}

export function DonationDetailCard({ donacion }: DonationDetailCardProps) {
  return (
    <div className="donaton-detail-card">
      <div className="donaton-detail-card__header">
        <h2>Donación #{donacion.idDonacion}</h2>
        <DonationStatusChip estado={donacion.estado} />
      </div>
      <dl className="donaton-info-list">
        <InfoRow label="Fecha" value={formatDate(donacion.fecha)} />
        <InfoRow label="Cantidad" value={donacion.cantidad} />
        <InfoRow label="Donante" value={donacion.donante?.nombre ?? '-'} />
        <InfoRow label="Contacto" value={donacion.donante?.contacto ?? '-'} />
        <InfoRow
          label="Recursos"
          value={donacion.recursoTipos?.length ? donacion.recursoTipos.join(', ') : '-'}
        />
      </dl>
    </div>
  )
}
