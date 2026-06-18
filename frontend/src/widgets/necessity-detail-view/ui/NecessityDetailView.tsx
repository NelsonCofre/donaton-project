import { useNavigate, useParams } from 'react-router-dom'
import { useNecessityDetail } from '@/entities/necessity'
import { NecessityDetailCard } from '@/features/necessity-detail-card'
import { NecessityForm } from '@/features/necessity-form'
import {
  ActionBar,
  ErrorState,
  InlineMessage,
  LoadingState,
  PageHeader,
  SectionCard,
} from '@/shared/ui'

export function NecessityDetailView() {
  const { id } = useParams()
  const navigate = useNavigate()
  const necessityId = Number(id)
  const {
    necesidad,
    loading,
    error,
    editError,
    editSuccess,
    editLoading,
    deleteLoading,
    load,
    save,
    remove,
  } = useNecessityDetail(necessityId)

  return (
    <>
      <PageHeader
        title="Detalle de necesidad"
        description="Revisa, ajusta o elimina una necesidad reportada."
        actions={
          <button
            type="button"
            className="donaton-btn donaton-btn--secondary"
            onClick={() => navigate('/necesidades')}
          >
            Volver
          </button>
        }
      />

      {loading ? <LoadingState message="Cargando necesidad…" /> : null}
      {!loading && error ? <ErrorState message={error} onRetry={() => void load()} /> : null}

      {!loading && !error && necesidad ? (
        <>
          <SectionCard>
            <NecessityDetailCard necesidad={necesidad} />
            <ActionBar>
              <button
                type="button"
                className="donaton-btn donaton-btn--danger"
                disabled={deleteLoading}
                onClick={() =>
                  void (async () => {
                    const confirmed = window.confirm('¿Eliminar esta necesidad?')
                    if (!confirmed) return
                    const deleted = await remove()
                    if (deleted) {
                      navigate('/necesidades', {
                        replace: true,
                        state: { success: 'Necesidad eliminada correctamente.' },
                      })
                    }
                  })()
                }
              >
                {deleteLoading ? 'Eliminando…' : 'Eliminar necesidad'}
              </button>
            </ActionBar>
          </SectionCard>

          {editSuccess ? <InlineMessage tone="success">{editSuccess}</InlineMessage> : null}

          <SectionCard title="Editar necesidad" variant="highlight">
            <NecessityForm
              title="Actualizar necesidad"
              submitLabel="Guardar cambios"
              initialValues={necesidad}
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
