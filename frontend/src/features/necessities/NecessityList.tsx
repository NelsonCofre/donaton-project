import type { Necesidad } from '@/entities/necessity'
import { StatusBadge } from '@/shared/ui'

type NecessityListProps = {
  items: Necesidad[]
  onSelect?: (item: Necesidad) => void
}

export function NecessityList({ items, onSelect }: NecessityListProps) {
  return (
    <div className="donaton-table-wrap">
      <table className="donaton-table">
        <thead>
          <tr>
            <th>Titulo</th>
            <th>Recurso</th>
            <th>Cantidad</th>
            <th>Prioridad</th>
            <th>Estado</th>
            <th>Ubicacion</th>
            <th>Fecha</th>
          </tr>
        </thead>
        <tbody>
          {items.map((item) => (
            <tr
              key={item.idNecesidad}
              onClick={() => onSelect?.(item)}
              tabIndex={onSelect ? 0 : undefined}
              onKeyDown={(event) => {
                if (onSelect && (event.key === 'Enter' || event.key === ' ')) {
                  event.preventDefault()
                  onSelect(item)
                }
              }}
            >
              <td>{item.titulo}</td>
              <td>{item.recurso}</td>
              <td>{item.cantidad}</td>
              <td>{item.prioridad}</td>
              <td>
                <StatusBadge label={item.estado} />
              </td>
              <td>{item.ubicacion}</td>
              <td>{new Date(item.fechaReporte).toLocaleDateString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
