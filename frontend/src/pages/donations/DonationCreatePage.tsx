import { Link, useNavigate } from 'react-router-dom'
import { CreateDonationForm } from '@/features/donations'
import { ActionBar, PageHeader, SectionCard } from '@/shared/ui'

export function DonationCreatePage() {
  const navigate = useNavigate()

  return (
    <>
      <PageHeader
        title="Nueva donación"
        description="Registra una donación para sumarla al listado general."
        actions={
          <Link className="donaton-btn donaton-btn--secondary" to="/donaciones">
            Cancelar
          </Link>
        }
      />

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
          <Link className="donaton-btn donaton-btn--secondary" to="/donaciones">
            Cancelar
          </Link>
        </ActionBar>
      </SectionCard>
    </>
  )
}
