import type { CollectionCenter } from '@/entities/logistics'
import { InfoRow } from '@/shared/ui'

type CollectionCenterDetailCardProps = {
  center: CollectionCenter
}

export function CollectionCenterDetailCard({ center }: CollectionCenterDetailCardProps) {
  return (
    <div className="donaton-detail-card">
      <div className="donaton-detail-card__header">
        <h2>{center.nombre}</h2>
      </div>
      <dl className="donaton-info-list">
        <InfoRow label="Ubicacion" value={center.ubicacion} />
        <InfoRow label="Responsable" value={center.responsable} />
        <InfoRow label="Telefono" value={center.telefono} />
        <InfoRow label="Capacidad" value={center.capacidad} />
      </dl>
    </div>
  )
}
