import { useMemo } from 'react'
import {
  useCollectionCentersList,
  useInventoriesList,
  sortInventoryByUpdatedDate,
} from '@/entities/logistics'
import { useInventoryManagement } from '@/features/inventory-manage'
import { InventoryForm } from '@/features/inventory-form'
import { InventoryList } from '@/features/inventory-list'
import {
  EmptyState,
  ErrorState,
  LoadingState,
  MetricCard,
  PageHeader,
  SectionCard,
} from '@/shared/ui'

export function InventoryOverview() {
  const { centers } = useCollectionCentersList()
  const { items, loading, error, load } = useInventoriesList()
  const management = useInventoryManagement(load)

  const sortedItems = useMemo(
    () => sortInventoryByUpdatedDate(items),
    [items],
  )

  return (
    <>
      <PageHeader
        eyebrow="Logística"
        title="Inventario"
        description="Gestiona existencias por centro usando un repositorio desacoplado y reusable."
      />

      <div className="donaton-metric-grid">
        <MetricCard
          label="Items visibles"
          value={sortedItems.length}
          hint="Stock modelado en la UI"
          tone="info"
        />
        <MetricCard
          label="Modo"
          value={management.editing ? 'Edición' : 'Carga'}
          hint="Estado actual del formulario"
          tone={management.editing ? 'warning' : 'neutral'}
        />
      </div>

      <SectionCard eyebrow="Control de stock" title="Listado de inventario">
        {loading ? <LoadingState message="Cargando inventario…" /> : null}
        {!loading && error ? <ErrorState message={error} onRetry={() => void load()} /> : null}
        {!loading && !error && sortedItems.length === 0 ? (
          <EmptyState
            title="No hay inventario registrado"
            description="Agrega recursos a un centro para simular stock disponible."
          />
        ) : null}
        {!loading && !error && sortedItems.length > 0 ? (
          <InventoryList
            items={sortedItems}
            editingId={management.editing?.idInventario ?? null}
            deletingId={management.deletingId}
            onEdit={management.setEditing}
            onDelete={(id) => void management.remove(id)}
          />
        ) : null}
      </SectionCard>

      <SectionCard
        eyebrow="Mantenimiento"
        title={management.editing ? 'Editar item' : 'Nuevo item'}
        variant="subtle"
      >
        <InventoryForm
          centers={centers}
          initialValues={
            management.editing
              ? {
                  idCentro: management.editing.idCentro,
                  recurso: management.editing.recurso,
                  cantidad: management.editing.cantidad,
                }
              : undefined
          }
          error={management.error}
          success={management.success}
          loading={management.saving}
          submitLabel={management.editing ? 'Guardar cambios' : 'Crear item'}
          onSubmit={(values) => void management.save(values)}
          onCancel={management.editing ? () => management.setEditing(null) : undefined}
        />
      </SectionCard>
    </>
  )
}
