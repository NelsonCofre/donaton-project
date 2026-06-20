import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import {
  getLogisticsRepository,
  useCollectionCentersList,
  type CreateCollectionCenterRequest,
} from '@/entities/logistics'
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
  const repository = getLogisticsRepository()
  const { centers, loading, error, load } = useCollectionCentersList()
  const [formError, setFormError] = useState<string | null>(null)
  const [formSuccess, setFormSuccess] = useState<string | null>(null)
  const [saving, setSaving] = useState(false)

  async function handleCreate(values: CreateCollectionCenterRequest) {
    setFormError(null)
    setFormSuccess(null)
    setSaving(true)
    try {
      await repository.createCenter(values)
      setFormSuccess('Centro creado correctamente.')
      await load()
    } catch (err) {
      setFormError(err instanceof Error ? err.message : 'No se pudo crear el centro.')
    } finally {
      setSaving(false)
    }
  }

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
          error={formError}
          success={formSuccess}
          loading={saving}
          onSubmit={(values) => void handleCreate(values)}
        />
      </SectionCard>
    </>
  )
}
