import { useMemo, useState } from 'react'
import {
  getLogisticsRepository,
  useCollectionCentersList,
  useShipmentsList,
  type CreateShipmentRequest,
  type Shipment,
} from '@/entities/logistics'
import { ShipmentForm } from '@/features/shipment-form'
import { ShipmentList } from '@/features/shipment-list'
import {
  EmptyState,
  ErrorState,
  LoadingState,
  MetricCard,
  PageHeader,
  SectionCard,
} from '@/shared/ui'

export function ShipmentsOverview() {
  const repository = getLogisticsRepository()
  const { centers } = useCollectionCentersList()
  const { items, loading, error, load } = useShipmentsList()
  const [editing, setEditing] = useState<Shipment | null>(null)
  const [deletingId, setDeletingId] = useState<number | null>(null)
  const [formError, setFormError] = useState<string | null>(null)
  const [formSuccess, setFormSuccess] = useState<string | null>(null)
  const [saving, setSaving] = useState(false)

  const sortedItems = useMemo(
    () => [...items].sort((a, b) => b.fecha.localeCompare(a.fecha)),
    [items],
  )

  async function handleSubmit(values: CreateShipmentRequest) {
    setFormError(null)
    setFormSuccess(null)
    setSaving(true)
    try {
      if (editing) {
        await repository.updateShipment(editing.idEnvio, values)
        setFormSuccess('Envio actualizado correctamente.')
        setEditing(null)
      } else {
        await repository.createShipment(values)
        setFormSuccess('Envio creado correctamente.')
      }
      await load()
    } catch (err) {
      setFormError(err instanceof Error ? err.message : 'No se pudo guardar el envio.')
    } finally {
      setSaving(false)
    }
  }

  async function handleDelete(id: number) {
    const confirmed = window.confirm('¿Eliminar este envio?')
    if (!confirmed) return
    setDeletingId(id)
    try {
      await repository.removeShipment(id)
      if (editing?.idEnvio === id) setEditing(null)
      await load()
    } finally {
      setDeletingId(null)
    }
  }

  return (
    <>
      <PageHeader
        eyebrow="Logística"
        title="Envios"
        description="Coordina despachos entre centros y destinos con una UI lista para futura integracion real."
      />

      <div className="donaton-metric-grid">
        <MetricCard
          label="Despachos visibles"
          value={sortedItems.length}
          hint="Registros ordenados por fecha"
          tone="info"
        />
        <MetricCard
          label="Modo"
          value={editing ? 'Edición' : 'Planificación'}
          hint="Formulario operativo"
          tone={editing ? 'warning' : 'neutral'}
        />
      </div>

      <SectionCard eyebrow="Distribución" title="Listado de envíos">
        {loading ? <LoadingState message="Cargando envios…" /> : null}
        {!loading && error ? <ErrorState message={error} onRetry={() => void load()} /> : null}
        {!loading && !error && sortedItems.length === 0 ? (
          <EmptyState
            title="No hay envios registrados"
            description="Crea el primer envio para simular el flujo logistico."
          />
        ) : null}
        {!loading && !error && sortedItems.length > 0 ? (
          <ShipmentList
            items={sortedItems}
            editingId={editing?.idEnvio ?? null}
            deletingId={deletingId}
            onEdit={setEditing}
            onDelete={(id) => void handleDelete(id)}
          />
        ) : null}
      </SectionCard>

      <SectionCard
        eyebrow="Programación"
        title={editing ? 'Editar envio' : 'Nuevo envio'}
        variant="subtle"
      >
        <ShipmentForm
          centers={centers}
          initialValues={
            editing
              ? {
                  idCentro: editing.idCentro,
                  destino: editing.destino,
                  detalle: editing.detalle,
                  cantidad: editing.cantidad,
                  fecha: editing.fecha,
                  estado: editing.estado,
                }
              : undefined
          }
          error={formError}
          success={formSuccess}
          loading={saving}
          submitLabel={editing ? 'Guardar cambios' : 'Crear envio'}
          onSubmit={(values) => void handleSubmit(values)}
          onCancel={editing ? () => setEditing(null) : undefined}
        />
      </SectionCard>
    </>
  )
}
