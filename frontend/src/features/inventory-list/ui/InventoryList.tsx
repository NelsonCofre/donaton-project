import type { InventoryItem } from '@/entities/logistics'

type InventoryListProps = {
  items: InventoryItem[]
  editingId?: number | null
  deletingId?: number | null
  onEdit?: (item: InventoryItem) => void
  onDelete?: (id: number) => void
}

export function InventoryList({
  items,
  editingId = null,
  deletingId = null,
  onEdit,
  onDelete,
}: InventoryListProps) {
  return (
    <div className="donaton-table-wrap">
      <table className="donaton-table">
        <thead>
          <tr>
            <th>Centro</th>
            <th>Recurso</th>
            <th>Cantidad</th>
            <th>Actualizado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {items.map((item) => (
            <tr key={item.idInventario}>
              <td>{item.centroNombre}</td>
              <td>{item.recurso}</td>
              <td>{item.cantidad}</td>
              <td>{new Date(item.updatedAt).toLocaleDateString()}</td>
              <td>
                <div className="donaton-table-actions">
                  <button
                    type="button"
                    className="donaton-btn donaton-btn--small donaton-btn--secondary"
                    onClick={() => onEdit?.(item)}
                    disabled={deletingId === item.idInventario}
                  >
                    {editingId === item.idInventario ? 'Editando…' : 'Editar'}
                  </button>
                  <button
                    type="button"
                    className="donaton-btn donaton-btn--small donaton-btn--danger"
                    onClick={() => onDelete?.(item.idInventario)}
                    disabled={deletingId === item.idInventario}
                  >
                    {deletingId === item.idInventario ? 'Eliminando…' : 'Eliminar'}
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
