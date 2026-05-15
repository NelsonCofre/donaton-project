import type { Donacion } from '@/entities/donation'

type DonationListProps = {
  donaciones: Donacion[]
  loading: boolean
  error: string | null
  editingId: number | null
  deletingId: number | null
  onEdit: (donacion: Donacion) => void
  onDelete: (id: number) => void
}

function formatDate(iso: string): string {
  const d = new Date(iso)
  return Number.isNaN(d.getTime()) ? iso : d.toLocaleDateString()
}

export function DonationList({
  donaciones,
  loading,
  error,
  editingId,
  deletingId,
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
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {donaciones.map((d) => {
            const isEditing = editingId === d.idDonacion
            const isDeleting = deletingId === d.idDonacion
            return (
              <tr key={d.idDonacion} className={isEditing ? 'donaton-row--active' : ''}>
                <td>{d.idDonacion}</td>
                <td>{formatDate(d.fecha)}</td>
                <td>{d.cantidad}</td>
                <td>
                  <span className="donaton-badge">{d.estado}</span>
                </td>
                <td>{d.donante?.nombre ?? '—'}</td>
                <td>{d.donante?.contacto ?? '—'}</td>
                <td>
                  {d.recursoTipos?.length ? d.recursoTipos.join(', ') : '—'}
                </td>
                <td>
                  <div className="donaton-table-actions">
                    <button
                      type="button"
                      className="donaton-btn donaton-btn--small donaton-btn--secondary"
                      onClick={() => onEdit(d)}
                      disabled={isDeleting}
                    >
                      {isEditing ? 'Editando…' : 'Editar'}
                    </button>
                    <button
                      type="button"
                      className="donaton-btn donaton-btn--small donaton-btn--danger"
                      onClick={() => onDelete(d.idDonacion)}
                      disabled={isDeleting || isEditing}
                    >
                      {isDeleting ? 'Eliminando…' : 'Eliminar'}
                    </button>
                  </div>
                </td>
              </tr>
            )
          })}
        </tbody>
      </table>
    </div>
  )
}
