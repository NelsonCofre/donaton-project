import { Link, useNavigate } from 'react-router-dom'
import { useNecessitiesList } from '@/entities/necessity'
import { NecessityList } from '@/features/necessity-list'
import {
  EmptyState,
  ErrorState,
  LoadingState,
  MetricCard,
  PageHeader,
  SectionCard,
  Toolbar,
} from '@/shared/ui'

export function NecessitiesOverview() {
  const navigate = useNavigate()
  const { filteredNecesidades, filters, hasFilters, loading, error, load, setFilters } =
    useNecessitiesList()

  return (
    <>
      <PageHeader
        eyebrow="Terreno"
        title="Necesidades"
        description="Visualiza prioridades en terreno y prepara la futura integracion con el servicio de necesidades."
        actions={
          <Link className="donaton-btn" to="/necesidades/nueva">
            Nueva necesidad
          </Link>
        }
      />

      <div className="donaton-metric-grid">
        <MetricCard
          label="Necesidades visibles"
          value={filteredNecesidades.length}
          hint="Corte actual del tablero"
          tone="info"
        />
        <MetricCard
          label="Vista priorizada"
          value={filters.prioridad === 'TODAS' ? 'General' : filters.prioridad}
          hint="Nivel de prioridad seleccionado"
          tone="warning"
        />
      </div>

      <SectionCard eyebrow="Análisis" title="Filtros y orden" variant="subtle">
        <Toolbar>
          <div className="donaton-field donaton-field--inline">
            <label htmlFor="necessities-search">Buscar</label>
            <input
              id="necessities-search"
              type="search"
              placeholder="Titulo, recurso o ubicacion"
              value={filters.query}
              onChange={(event) =>
                setFilters((current) => ({ ...current, query: event.target.value }))
              }
            />
          </div>
          <div className="donaton-field donaton-field--inline">
            <label htmlFor="necessities-status">Estado</label>
            <select
              id="necessities-status"
              value={filters.estado}
              onChange={(event) =>
                setFilters((current) => ({
                  ...current,
                  estado: event.target.value as typeof current.estado,
                }))
              }
            >
              <option value="TODOS">Todos</option>
              <option value="ABIERTA">Abierta</option>
              <option value="EN_PROCESO">En proceso</option>
              <option value="CUBIERTA">Cubierta</option>
              <option value="CANCELADA">Cancelada</option>
            </select>
          </div>
          <div className="donaton-field donaton-field--inline">
            <label htmlFor="necessities-priority">Prioridad</label>
            <select
              id="necessities-priority"
              value={filters.prioridad}
              onChange={(event) =>
                setFilters((current) => ({
                  ...current,
                  prioridad: event.target.value as typeof current.prioridad,
                }))
              }
            >
              <option value="TODAS">Todas</option>
              <option value="BAJA">Baja</option>
              <option value="MEDIA">Media</option>
              <option value="ALTA">Alta</option>
              <option value="CRITICA">Critica</option>
            </select>
          </div>
          <div className="donaton-sort-controls">
            <div className="donaton-field donaton-field--inline">
              <label htmlFor="necessities-sort-field">Ordenar por</label>
              <select
                id="necessities-sort-field"
                value={filters.sortBy}
                onChange={(event) =>
                  setFilters((current) => ({
                    ...current,
                    sortBy: event.target.value as typeof current.sortBy,
                  }))
                }
              >
                <option value="fechaReporte">Fecha</option>
                <option value="cantidad">Cantidad</option>
                <option value="prioridad">Prioridad</option>
              </select>
            </div>
            <div className="donaton-field donaton-field--inline">
              <label htmlFor="necessities-sort-direction">Direccion</label>
              <select
                id="necessities-sort-direction"
                value={filters.sortDirection}
                onChange={(event) =>
                  setFilters((current) => ({
                    ...current,
                    sortDirection: event.target.value as typeof current.sortDirection,
                  }))
                }
              >
                <option value="desc">Descendente</option>
                <option value="asc">Ascendente</option>
              </select>
            </div>
          </div>
        </Toolbar>
      </SectionCard>

      <SectionCard eyebrow="Seguimiento" title="Listado de necesidades">
        {loading ? <LoadingState message="Cargando necesidades…" /> : null}
        {!loading && error ? <ErrorState message={error} onRetry={() => void load()} /> : null}
        {!loading && !error && filteredNecesidades.length === 0 ? (
          <EmptyState
            title={hasFilters ? 'No hay coincidencias' : 'No hay necesidades registradas'}
            description="Puedes crear necesidades para simular o preparar la integracion del modulo."
          />
        ) : null}
        {!loading && !error && filteredNecesidades.length > 0 ? (
          <NecessityList
            items={filteredNecesidades}
            onSelect={(item) => navigate(`/necesidades/${item.idNecesidad}`)}
          />
        ) : null}
      </SectionCard>
    </>
  )
}
