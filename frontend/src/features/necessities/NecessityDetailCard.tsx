import type { Necesidad } from '@/entities/necessity'
import { InfoRow, StatusBadge } from '@/shared/ui'

type NecessityDetailCardProps = {
  necesidad: Necesidad
}

export function NecessityDetailCard({ necesidad }: NecessityDetailCardProps) {
  return (
    <div className="donaton-detail-card">
      <div className="donaton-detail-card__header">
        <div>
          <h2>{necesidad.titulo}</h2>
          <p className="donaton-page-header__text">{necesidad.descripcion}</p>
        </div>
        <StatusBadge label={necesidad.estado} />
      </div>
      <dl className="donaton-info-list">
        <InfoRow label="Recurso" value={necesidad.recurso} />
        <InfoRow label="Cantidad" value={necesidad.cantidad} />
        <InfoRow label="Prioridad" value={necesidad.prioridad} />
        <InfoRow label="Ubicacion" value={necesidad.ubicacion} />
        <InfoRow
          label="Fecha de reporte"
          value={new Date(necesidad.fechaReporte).toLocaleDateString()}
        />
      </dl>
    </div>
  )
}
