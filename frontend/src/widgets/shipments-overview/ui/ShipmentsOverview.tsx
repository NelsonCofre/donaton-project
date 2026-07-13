import { useMemo } from 'react'
import {
  useCollectionCentersList,
  useShipmentsList,
  sortShipmentsByDate,
} from '@/entities/logistics'
import { useShipmentManagement } from '@/features/shipment-manage'
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
  const { centers } = useCollectionCentersList()
  const { items, loading, error, load } = useShipmentsList()
  const management = useShipmentManagement(load)

  const sortedItems = useMemo(
    () => sortShipmentsByDate(items),
    [items],
  )

  return (
    <>
      <PageHeader
        eyebrow="Logística"
        title="Envios"
        description="Coordina despachos registrados por centro, fecha y estado."
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
          value={management.editing ? 'Edición' : 'Planificación'}
          hint="Formulario operativo"
          tone={management.editing ? 'warning' : 'neutral'}
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
            editingId={management.editing?.idEnvio ?? null}
            deletingId={management.deletingId}
            onEdit={management.setEditing}
            onDelete={(id) => void management.remove(id)}
          />
        ) : null}
      </SectionCard>

      <SectionCard
        eyebrow="Programación"
        title={management.editing ? 'Editar envio' : 'Nuevo envio'}
        variant="subtle"
      >
        <ShipmentForm
          centers={centers}
          initialValues={
            management.editing
              ? {
                  idCentro: management.editing.idCentro,
                  fecha: management.editing.fecha,
                  estado: management.editing.estado,
                }
              : undefined
          }
          error={management.error}
          success={management.success}
          loading={management.saving}
          submitLabel={management.editing ? 'Guardar cambios' : 'Crear envio'}
          onSubmit={(values) => void management.save(values)}
          onCancel={management.editing ? () => management.setEditing(null) : undefined}
        />
      </SectionCard>
    </>
  )
}
