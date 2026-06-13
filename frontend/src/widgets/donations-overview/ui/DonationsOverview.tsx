import { useCallback, useEffect, useMemo, useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import type { Donacion, DonationFilters } from '@/entities/donation'
import { fetchDonaciones } from '@/entities/donation'
import { DonationEmptyState } from '@/features/donation-empty-state'
import { DonationFilterPanel } from '@/features/donation-filter'
import { DonationList } from '@/features/donation-list'
import { DonationRefreshButton } from '@/features/donation-refresh'
import { DonationSearchForm } from '@/features/donation-search'
import { DonationSortSelect } from '@/features/donation-sort'
import { ApiError } from '@/shared/api/client'
import {
  ActionBar,
  ErrorState,
  InlineMessage,
  LoadingState,
  PageHeader,
  SectionCard,
} from '@/shared/ui'

type NavigationState = {
  success?: string
} | null

const initialFilters: DonationFilters = {
  query: '',
  estado: 'TODOS',
  sortBy: 'fecha',
  sortDirection: 'desc',
}

function getDonationSearchText(donacion: Donacion): string {
  return [
    donacion.idDonacion,
    donacion.donante?.nombre,
    donacion.donante?.contacto,
    donacion.recursoTipos?.join(' '),
  ]
    .filter(Boolean)
    .join(' ')
    .toLowerCase()
}

function sortDonaciones(donaciones: Donacion[], filters: DonationFilters): Donacion[] {
  return [...donaciones].sort((a, b) => {
    const direction = filters.sortDirection === 'asc' ? 1 : -1

    if (filters.sortBy === 'cantidad') {
      return (a.cantidad - b.cantidad) * direction
    }

    if (filters.sortBy === 'id') {
      return (a.idDonacion - b.idDonacion) * direction
    }

    return (
      (new Date(a.fecha).getTime() - new Date(b.fecha).getTime()) * direction
    )
  })
}

export function DonationsOverview() {
  const navigate = useNavigate()
  const location = useLocation()
  const locationState = location.state as NavigationState
  const [donaciones, setDonaciones] = useState<Donacion[]>([])
  const [filters, setFilters] = useState<DonationFilters>(initialFilters)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await fetchDonaciones()
      setDonaciones(data)
    } catch (err) {
      setDonaciones([])
      setError(
        err instanceof ApiError
          ? err.message
          : 'No se pudieron cargar las donaciones. Inténtalo de nuevo en unos instantes.',
      )
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void Promise.resolve().then(() => load())
  }, [load])

  const filteredDonaciones = useMemo(() => {
    const query = filters.query.trim().toLowerCase()
    const visible = donaciones.filter((donacion) => {
      const matchesQuery = query
        ? getDonationSearchText(donacion).includes(query)
        : true
      const matchesStatus =
        filters.estado === 'TODOS' ? true : donacion.estado === filters.estado

      return matchesQuery && matchesStatus
    })

    return sortDonaciones(visible, filters)
  }, [donaciones, filters])

  const hasFilters = filters.query.trim() !== '' || filters.estado !== 'TODOS'

  return (
    <>
      <PageHeader
        title="Donaciones"
        description="Consulta, filtra y revisa el detalle de las donaciones registradas."
        actions={
          <Link className="donaton-btn" to="/donaciones/nueva">
            Nueva donación
          </Link>
        }
      />

      {locationState?.success ? (
        <InlineMessage tone="success">{locationState.success}</InlineMessage>
      ) : null}

      <SectionCard title="Filtros">
        <div className="donaton-toolbar">
          <DonationSearchForm
            value={filters.query}
            onChange={(query) => setFilters((current) => ({ ...current, query }))}
          />
          <DonationFilterPanel
            value={filters.estado}
            onChange={(estado) => setFilters((current) => ({ ...current, estado }))}
          />
          <DonationSortSelect
            sortBy={filters.sortBy}
            sortDirection={filters.sortDirection}
            onChange={(sortBy, sortDirection) =>
              setFilters((current) => ({ ...current, sortBy, sortDirection }))
            }
          />
          <ActionBar>
            <DonationRefreshButton loading={loading} onRefresh={() => void load()} />
          </ActionBar>
        </div>
      </SectionCard>

      <SectionCard title="Listado">
        {loading ? <LoadingState message="Cargando donaciones…" /> : null}
        {!loading && error ? <ErrorState message={error} onRetry={() => void load()} /> : null}
        {!loading && !error && filteredDonaciones.length === 0 ? (
          <DonationEmptyState hasFilters={hasFilters} />
        ) : null}
        {!loading && !error && filteredDonaciones.length > 0 ? (
          <DonationList
            donaciones={filteredDonaciones}
            onSelect={(donacion) => navigate(`/donaciones/${donacion.idDonacion}`)}
          />
        ) : null}
      </SectionCard>
    </>
  )
}
