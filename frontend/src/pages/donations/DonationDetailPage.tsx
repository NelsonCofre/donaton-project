import { Link, useNavigate, useParams } from 'react-router-dom'
import { useDonationDetail } from '@/entities/donation'
import {
  DeleteDonationButton,
  DonationDetailCard,
  EditDonationSection,
} from '@/features/donations'
import {
  ActionBar,
  ErrorState,
  InlineMessage,
  LoadingState,
  PageHeader,
  SectionCard,
} from '@/shared/ui'

export function DonationDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const donationId = Number(id)
  const {
    donacion,
    loading,
    error,
    editError,
    editSuccess,
    editLoading,
    deleteLoading,
    load,
    save,
    remove,
  } = useDonationDetail(donationId)

  return (
    <>
      <PageHeader
        title="Detalle de donación"
        description="Revisa el estado completo, edita datos o elimina el registro."
        actions={
          <Link className="donaton-link-action" to="/donaciones">
            Volver a donaciones
          </Link>
        }
      />

      {loading ? <LoadingState message="Cargando detalle de donación…" /> : null}
      {!loading && error ? <ErrorState message={error} onRetry={() => void load()} /> : null}

      {!loading && !error && donacion ? (
        <>
          <SectionCard>
            <DonationDetailCard donacion={donacion} />
            <ActionBar>
              <DeleteDonationButton
                loading={deleteLoading}
                onDelete={() =>
                  void (async () => {
                    const deleted = await remove()
                    if (deleted) {
                      navigate('/donaciones', {
                        replace: true,
                        state: { success: 'Donación eliminada correctamente.' },
                      })
                    }
                  })()
                }
              />
            </ActionBar>
          </SectionCard>

          {editSuccess ? <InlineMessage tone="success">{editSuccess}</InlineMessage> : null}

          <SectionCard title="Editar donación" variant="highlight">
            <EditDonationSection
              donacion={donacion}
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
