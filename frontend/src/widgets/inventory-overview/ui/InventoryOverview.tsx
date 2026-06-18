import { useMemo, useState } from 'react'
import {
  getLogisticsRepository,
  useCollectionCentersList,
  useInventoriesList,
  type CreateInventoryItemRequest,
  type InventoryItem,
} from '@/entities/logistics'
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
  const repository = getLogisticsRepository()
  const { centers } = useCollectionCentersList()
  const { items, loading, error, load } = useInventoriesList()
  const [editing, setEditing] = useState<InventoryItem | null>(null)
  const [deletingId, setDeletingId] = useState<number | null>(null)
  const [formError, setFormError] = useState<string | null>(null)
  const [formSuccess, setFormSuccess] = useState<string | null>(null)
  const [saving, setSaving] = useState(false)

  const sortedItems = useMemo(
    () => [...items].sort((a, b) => b.updatedAt.localeCompare(a.updatedAt)),
    [items],
  )

  async function handleSubmit(values: CreateInventoryItemRequest) {
    setFormError(null)
    setFormSuccess(null)
    setSaving(true)
    try {
      if (editing) {
        await repository.updateInventory(editing.idInventario, values)
        setFormSuccess('Inventario actualizado correctamente.')
        setEditing(null)
      } else {
        await repository.createInventory(values)
        setFormSuccess('Inventario creado correctamente.')
      }
      await load()
    } catch (err) {
      setFormError(err instanceof Error ? err.message : 'No se pudo guardar el inventario.')
    } finally {
      setSaving(false)
    }
  }

  async function handleDelete(id: number) {
    const confirmed = window.confirm('¿Eliminar este item de inventario?')
    if (!confirmed) return
    setDeletingId(id)
    try {
      await repository.removeInventory(id)
      if (editing?.idInventario === id) setEditing(null)
      await load()
    } finally {
      setDeletingId(null)
    }
  }

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
          value={editing ? 'Edición' : 'Carga'}
          hint="Estado actual del formulario"
          tone={editing ? 'warning' : 'neutral'}
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
            editingId={editing?.idInventario ?? null}
            deletingId={deletingId}
            onEdit={setEditing}
            onDelete={(id) => void handleDelete(id)}
          />
        ) : null}
      </SectionCard>

      <SectionCard
        eyebrow="Mantenimiento"
        title={editing ? 'Editar item' : 'Nuevo item'}
        variant="subtle"
      >
        <InventoryForm
          centers={centers}
          initialValues={
            editing
              ? {
                  idCentro: editing.idCentro,
                  recurso: editing.recurso,
                  cantidad: editing.cantidad,
                }
              : undefined
          }
          error={formError}
          success={formSuccess}
          loading={saving}
          submitLabel={editing ? 'Guardar cambios' : 'Crear item'}
          onSubmit={(values) => void handleSubmit(values)}
          onCancel={editing ? () => setEditing(null) : undefined}
        />
      </SectionCard>
    </>
  )
}
