import { Link, useLocation, useNavigate } from 'react-router-dom'
import { useDonationsList } from '@/entities/donation'
import { DonationEmptyState } from '@/features/donation-empty-state'
import { DonationFilterPanel } from '@/features/donation-filter'
import { DonationList } from '@/features/donation-list'
import { DonationRefreshButton } from '@/features/donation-refresh'
import { DonationSearchForm } from '@/features/donation-search'
import { DonationSortSelect } from '@/features/donation-sort'
import {
  ActionBar,
  ErrorState,
  InlineMessage,
  LoadingState,
  MetricCard,
  PageHeader,
  SectionCard,
  Toolbar,
} from '@/shared/ui'

type NavigationState = {
  success?: string
} | null

export function DonationsOverview() {
  const navigate = useNavigate()
  const location = useLocation()
  const locationState = location.state as NavigationState
  const {
    filteredDonaciones,
    filters,
    hasFilters,
    loading,
    error,
    load,
    setQuery,
    setEstado,
    setSort,
  } = useDonationsList()

  return (
    <>
      <PageHeader
        eyebrow="Operación"
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

      <div className="donaton-metric-grid">
        <MetricCard
          label="Registros visibles"
          value={filteredDonaciones.length}
          hint="Resultados después de filtros"
          tone="info"
        />
        <MetricCard
          label="Filtros activos"
          value={hasFilters ? 'Sí' : 'No'}
          hint="Búsqueda o estado aplicado"
          tone={hasFilters ? 'warning' : 'neutral'}
        />
      </div>

      <SectionCard eyebrow="Refinamiento" title="Filtros y acciones" variant="subtle">
        <Toolbar>
          <DonationSearchForm value={filters.query} onChange={setQuery} />
          <DonationFilterPanel value={filters.estado} onChange={setEstado} />
          <DonationSortSelect
            sortBy={filters.sortBy}
            sortDirection={filters.sortDirection}
            onChange={setSort}
          />
          <ActionBar>
            <DonationRefreshButton loading={loading} onRefresh={() => void load()} />
          </ActionBar>
        </Toolbar>
      </SectionCard>

      <SectionCard eyebrow="Supervisión" title="Listado de donaciones">
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
