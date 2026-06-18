import type { Donacion } from '@/entities/donation'
import { DonationListItem } from '@/features/donation-list-item'

type DonationListProps = {
  donaciones: Donacion[]
  loading?: boolean
  error?: string | null
  editingId?: number | null
  deletingId?: number | null
  onSelect?: (donacion: Donacion) => void
  onEdit?: (donacion: Donacion) => void
  onDelete?: (id: number) => void
}

export function DonationList({
  donaciones,
  loading = false,
  error = null,
  editingId = null,
  deletingId = null,
  onSelect,
  onEdit,
  onDelete,
}: DonationListProps) {
  if (loading) {
    return <p className="donaton-muted">Cargando donaciones…</p>
  }
  if (error) {
    return (
      <div className="donaton-alert donaton-alert--error" role="alert">
        {error}
      </div>
    )
  }
  if (donaciones.length === 0) {
    return (
      <p className="donaton-muted">
        No hay donaciones registradas. Usa el formulario de abajo para agregar la
        primera.
      </p>
    )
  }

  return (
    <div className="donaton-table-wrap">
      <table className="donaton-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Fecha</th>
            <th>Cantidad</th>
            <th>Estado</th>
            <th>Donante</th>
            <th>Contacto</th>
            <th>Recurso</th>
            {onEdit || onDelete ? <th>Acciones</th> : null}
          </tr>
        </thead>
        <tbody>
          {donaciones.map((donacion) => (
            <DonationListItem
              key={donacion.idDonacion}
              donacion={donacion}
              isEditing={editingId === donacion.idDonacion}
              isDeleting={deletingId === donacion.idDonacion}
              onSelect={onSelect}
              onEdit={onEdit}
              onDelete={onDelete}
            />
          ))}
        </tbody>
      </table>
    </div>
  )
}
