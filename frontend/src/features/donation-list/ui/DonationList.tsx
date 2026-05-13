import type { Donacion } from '@/entities/donation'

type DonationListProps = {
  donaciones: Donacion[]
  loading: boolean
  error: string | null
}

function formatDate(iso: string): string {
  const d = new Date(iso)
  return Number.isNaN(d.getTime()) ? iso : d.toLocaleDateString()
}

export function DonationList({ donaciones, loading, error }: DonationListProps) {
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
        No hay donaciones para mostrar. Cuando el BFF y el servicio de donaciones
        estén disponibles, aparecerán aquí.
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
            <th>Recursos</th>
          </tr>
        </thead>
        <tbody>
          {donaciones.map((d) => (
            <tr key={d.idDonacion}>
              <td>{d.idDonacion}</td>
              <td>{formatDate(d.fecha)}</td>
              <td>{d.cantidad}</td>
              <td>{d.estado}</td>
              <td>{d.donante?.nombre ?? `ID ${d.idDonante}`}</td>
              <td>
                {d.recursoTipos?.length
                  ? d.recursoTipos.join(', ')
                  : '—'}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
