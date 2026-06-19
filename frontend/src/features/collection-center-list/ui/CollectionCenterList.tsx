import type { CollectionCenter } from '@/entities/logistics'

type CollectionCenterListProps = {
  centers: CollectionCenter[]
  onSelect?: (center: CollectionCenter) => void
}

export function CollectionCenterList({ centers, onSelect }: CollectionCenterListProps) {
  return (
    <div className="donaton-table-wrap">
      <table className="donaton-table">
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Ubicacion</th>
          </tr>
        </thead>
        <tbody>
          {centers.map((center) => (
            <tr
              key={center.idCentro}
              onClick={() => onSelect?.(center)}
              tabIndex={onSelect ? 0 : undefined}
              onKeyDown={(event) => {
                if (onSelect && (event.key === 'Enter' || event.key === ' ')) {
                  event.preventDefault()
                  onSelect(center)
                }
              }}
            >
              <td>{center.nombre}</td>
              <td>{center.ubicacion}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
