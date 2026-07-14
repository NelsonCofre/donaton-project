import { Link } from 'react-router-dom'
import { EmptyState } from '@/shared/ui'

type DonationEmptyStateProps = {
  hasFilters: boolean
}

export function DonationEmptyState({ hasFilters }: DonationEmptyStateProps) {
  return (
    <EmptyState
      title={hasFilters ? 'No hay donaciones que coincidan' : 'No hay donaciones'}
      description={
        hasFilters
          ? 'Ajusta la búsqueda o los filtros para ver otros resultados.'
          : 'Registra la primera donación para comenzar a trabajar el listado.'
      }
      action={
        hasFilters ? null : (
          <Link className="donaton-btn" to="/donaciones/nueva">
            Nueva donación
          </Link>
        )
      }
    />
  )
}
