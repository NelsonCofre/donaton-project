import { Link, useNavigate } from 'react-router-dom'
import {
  useCollectionCentersList,
} from '@/entities/logistics'
import { useCreateCollectionCenter } from '@/features/collection-center-create'
import { CollectionCenterForm } from '@/features/collection-center-form'
import { CollectionCenterList } from '@/features/collection-center-list'
import {
  EmptyState,
  ErrorState,
  LoadingState,
  MetricCard,
  PageHeader,
  SectionCard,
} from '@/shared/ui'

export function CollectionCentersOverview() {
  const navigate = useNavigate()
  const { centers, loading, error, load } = useCollectionCentersList()
  const creation = useCreateCollectionCenter(load)

  return (
    <>
      <PageHeader
        eyebrow="Logística"
        title="Centros de acopio"
        description="Administra la red operativa de centros desde una vista desacoplada del backend."
        actions={
          <Link className="donaton-btn donaton-btn--secondary" to="/logistica/inventario">
            Ver inventario
          </Link>
        }
      />

      <div className="donaton-metric-grid">
        <MetricCard
          label="Centros activos"
          value={centers.length}
          hint="Red operativa registrada"
          tone="info"
        />
      </div>

      <SectionCard eyebrow="Cobertura" title="Listado de centros">
        {loading ? <LoadingState message="Cargando centros…" /> : null}
        {!loading && error ? <ErrorState message={error} onRetry={() => void load()} /> : null}
        {!loading && !error && centers.length === 0 ? (
          <EmptyState
            title="No hay centros registrados"
            description="Crea el primer centro para comenzar a modelar la logistica."
          />
        ) : null}
        {!loading && !error && centers.length > 0 ? (
          <CollectionCenterList
            centers={centers}
            onSelect={(center) => navigate(`/logistica/centros/${center.idCentro}`)}
          />
        ) : null}
      </SectionCard>

      <SectionCard eyebrow="Configuración" title="Nuevo centro" variant="subtle">
        <CollectionCenterForm
          title="Registrar centro"
          submitLabel="Guardar centro"
          error={creation.error}
          success={creation.success}
          loading={creation.loading}
          onSubmit={(values) => void creation.create(values)}
        />
      </SectionCard>
    </>
  )
}
