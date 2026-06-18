import { useNavigate, useParams } from 'react-router-dom'
import { useCollectionCenterDetail } from '@/entities/logistics'
import { CollectionCenterDetailCard } from '@/features/collection-center-detail-card'
import { CollectionCenterForm } from '@/features/collection-center-form'
import {
  ActionBar,
  ErrorState,
  InlineMessage,
  LoadingState,
  PageHeader,
  SectionCard,
} from '@/shared/ui'

export function CollectionCenterDetailView() {
  const { id } = useParams()
  const navigate = useNavigate()
  const centerId = Number(id)
  const {
    center,
    loading,
    error,
    editError,
    editSuccess,
    editLoading,
    deleteLoading,
    load,
    save,
    remove,
  } = useCollectionCenterDetail(centerId)

  return (
    <>
      <PageHeader
        title="Detalle de centro"
        description="Revisa los datos clave del centro y ajustalos cuando cambie la operacion."
        actions={
          <button
            type="button"
            className="donaton-btn donaton-btn--secondary"
            onClick={() => navigate('/logistica/centros')}
          >
            Volver
          </button>
        }
      />

      {loading ? <LoadingState message="Cargando centro…" /> : null}
      {!loading && error ? <ErrorState message={error} onRetry={() => void load()} /> : null}

      {!loading && !error && center ? (
        <>
          <SectionCard>
            <CollectionCenterDetailCard center={center} />
            <ActionBar>
              <button
                type="button"
                className="donaton-btn donaton-btn--danger"
                disabled={deleteLoading}
                onClick={() =>
                  void (async () => {
                    const confirmed = window.confirm('¿Eliminar este centro?')
                    if (!confirmed) return
                    const deleted = await remove()
                    if (deleted) {
                      navigate('/logistica/centros', {
                        replace: true,
                        state: { success: 'Centro eliminado correctamente.' },
                      })
                    }
                  })()
                }
              >
                {deleteLoading ? 'Eliminando…' : 'Eliminar centro'}
              </button>
            </ActionBar>
          </SectionCard>

          {editSuccess ? <InlineMessage tone="success">{editSuccess}</InlineMessage> : null}

          <SectionCard title="Editar centro" variant="highlight">
            <CollectionCenterForm
              title="Actualizar centro"
              submitLabel="Guardar cambios"
              initialValues={center}
              error={editError}
              success={null}
              loading={editLoading}
              onSubmit={(values) => void save(values)}
            />
          </SectionCard>
        </>
      ) : null}
    </>
  )
}
