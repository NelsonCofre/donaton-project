import type { DonacionEstado } from '@/entities/donation'

const estados: Array<DonacionEstado | 'TODOS'> = [
  'TODOS',
  'PENDIENTE',
  'RECIBIDA',
  'ASIGNADA',
  'CANCELADA',
]

type DonationFilterPanelProps = {
  value: DonacionEstado | 'TODOS'
  onChange: (value: DonacionEstado | 'TODOS') => void
}

export function DonationFilterPanel({ value, onChange }: DonationFilterPanelProps) {
  return (
    <div className="donaton-field donaton-field--inline">
      <label htmlFor="donation-status">Estado</label>
      <select
        id="donation-status"
        value={value}
        onChange={(event) => onChange(event.target.value as DonacionEstado | 'TODOS')}
      >
        {estados.map((estado) => (
          <option key={estado} value={estado}>
            {estado === 'TODOS' ? 'Todos' : estado}
          </option>
        ))}
      </select>
    </div>
  )
}
