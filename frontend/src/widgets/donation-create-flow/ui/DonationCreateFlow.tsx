import { useNavigate } from 'react-router-dom'
import { CancelDonationCreationLink } from '@/features/donation-cancel'
import { CreateDonationForm } from '@/features/donation-create'
import { DonationSubmitFeedback } from '@/features/donation-submit-feedback'
import { ActionBar, PageHeader, SectionCard } from '@/shared/ui'

export function DonationCreateFlow() {
  const navigate = useNavigate()

  return (
    <>
      <PageHeader
        title="Nueva donación"
        description="Registra una donación para sumarla al listado general."
        actions={<CancelDonationCreationLink />}
      />

      <DonationSubmitFeedback message={null} />

      <SectionCard>
        <CreateDonationForm
          onCreated={() =>
            navigate('/donaciones', {
              replace: true,
              state: { success: 'Donación registrada correctamente.' },
            })
          }
        />
        <ActionBar>
          <CancelDonationCreationLink />
        </ActionBar>
      </SectionCard>
    </>
  )
}
