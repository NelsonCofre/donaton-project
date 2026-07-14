import type { Donacion } from '@/entities/donation'
import { StatusBadge } from '@/shared/ui'

type DonationListItemProps = {
  donacion: Donacion
  isEditing?: boolean
  isDeleting?: boolean
  onSelect?: (donacion: Donacion) => void
  onEdit?: (donacion: Donacion) => void
  onDelete?: (id: number) => void
}

function formatDate(iso: string): string {
  const d = new Date(iso)
  return Number.isNaN(d.getTime()) ? iso : d.toLocaleDateString()
}

export function DonationListItem({
  donacion,
  isEditing = false,
  isDeleting = false,
  onSelect,
  onEdit,
  onDelete,
}: DonationListItemProps) {
  function handleRowClick() {
    onSelect?.(donacion)
  }

  return (
    <tr
      className={isEditing ? 'donaton-row--active' : undefined}
      onClick={handleRowClick}
      tabIndex={onSelect ? 0 : undefined}
      onKeyDown={(event) => {
        if (onSelect && (event.key === 'Enter' || event.key === ' ')) {
          event.preventDefault()
          onSelect(donacion)
        }
      }}
    >
      <td>{donacion.idDonacion}</td>
      <td>{formatDate(donacion.fecha)}</td>
      <td>{donacion.cantidad}</td>
      <td>
        <StatusBadge label={donacion.estado} />
      </td>
      <td>{donacion.donante?.nombre ?? '-'}</td>
      <td>{donacion.donante?.contacto ?? '-'}</td>
      <td>{donacion.recursoTipos?.length ? donacion.recursoTipos.join(', ') : '-'}</td>
      {onEdit || onDelete ? (
        <td>
          <div className="donaton-table-actions">
            {onEdit ? (
              <button
                type="button"
                className="donaton-btn donaton-btn--small donaton-btn--secondary"
                onClick={(event) => {
                  event.stopPropagation()
                  onEdit(donacion)
                }}
                disabled={isDeleting}
              >
                {isEditing ? 'Editando…' : 'Editar'}
              </button>
            ) : null}
            {onDelete ? (
              <button
                type="button"
                className="donaton-btn donaton-btn--small donaton-btn--danger"
                onClick={(event) => {
                  event.stopPropagation()
                  onDelete(donacion.idDonacion)
                }}
                disabled={isDeleting || isEditing}
              >
                {isDeleting ? 'Eliminando…' : 'Eliminar'}
              </button>
            ) : null}
          </div>
        </td>
      ) : null}
    </tr>
  )
}
