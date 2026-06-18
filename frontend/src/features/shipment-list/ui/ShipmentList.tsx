import type { Shipment } from '@/entities/logistics'
import { StatusBadge } from '@/shared/ui'

type ShipmentListProps = {
  items: Shipment[]
  editingId?: number | null
  deletingId?: number | null
  onEdit?: (item: Shipment) => void
  onDelete?: (id: number) => void
}

export function ShipmentList({
  items,
  editingId = null,
  deletingId = null,
  onEdit,
  onDelete,
}: ShipmentListProps) {
  return (
    <div className="donaton-table-wrap">
      <table className="donaton-table">
        <thead>
          <tr>
            <th>Centro</th>
            <th>Destino</th>
            <th>Detalle</th>
            <th>Cantidad</th>
            <th>Fecha</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {items.map((item) => (
            <tr key={item.idEnvio}>
              <td>{item.centroNombre}</td>
              <td>{item.destino}</td>
              <td>{item.detalle}</td>
              <td>{item.cantidad}</td>
              <td>{new Date(item.fecha).toLocaleDateString()}</td>
              <td>
                <StatusBadge label={item.estado} />
              </td>
              <td>
                <div className="donaton-table-actions">
                  <button
                    type="button"
                    className="donaton-btn donaton-btn--small donaton-btn--secondary"
                    onClick={() => onEdit?.(item)}
                    disabled={deletingId === item.idEnvio}
                  >
                    {editingId === item.idEnvio ? 'Editando…' : 'Editar'}
                  </button>
                  <button
                    type="button"
                    className="donaton-btn donaton-btn--small donaton-btn--danger"
                    onClick={() => onDelete?.(item.idEnvio)}
                    disabled={deletingId === item.idEnvio}
                  >
                    {deletingId === item.idEnvio ? 'Eliminando…' : 'Eliminar'}
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
