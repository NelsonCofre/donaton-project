import type { DonationSortDirection, DonationSortField } from '@/entities/donation'

type DonationSortSelectProps = {
  sortBy: DonationSortField
  sortDirection: DonationSortDirection
  onChange: (sortBy: DonationSortField, sortDirection: DonationSortDirection) => void
}

export function DonationSortSelect({
  sortBy,
  sortDirection,
  onChange,
}: DonationSortSelectProps) {
  return (
    <div className="donaton-sort-controls">
      <div className="donaton-field donaton-field--inline">
        <label htmlFor="donation-sort-by">Ordenar por</label>
        <select
          id="donation-sort-by"
          value={sortBy}
          onChange={(event) =>
            onChange(event.target.value as DonationSortField, sortDirection)
          }
        >
          <option value="fecha">Fecha</option>
          <option value="cantidad">Cantidad</option>
          <option value="id">ID</option>
        </select>
      </div>
      <div className="donaton-field donaton-field--inline">
        <label htmlFor="donation-sort-direction">Dirección</label>
        <select
          id="donation-sort-direction"
          value={sortDirection}
          onChange={(event) =>
            onChange(sortBy, event.target.value as DonationSortDirection)
          }
        >
          <option value="desc">Descendente</option>
          <option value="asc">Ascendente</option>
        </select>
      </div>
    </div>
  )
}
