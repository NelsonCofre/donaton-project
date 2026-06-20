import type { CreateDonacionRequest, Donacion } from '@/entities/donation'
import { donacionToFormValues } from '@/entities/donation/lib/mapDonacion'
import { DonationForm } from '@/features/donation-form'

type EditDonationSectionProps = {
  donacion: Donacion
  error: string | null
  success: string | null
  loading: boolean
  onSubmit: (values: CreateDonacionRequest) => void
}

export function EditDonationSection({
  donacion,
  error,
  success,
  loading,
  onSubmit,
}: EditDonationSectionProps) {
  return (
    <DonationForm
      key={donacion.idDonacion}
      title="Editar donación"
      submitLabel="Guardar cambios"
      initialValues={donacionToFormValues(donacion)}
      error={error}
      success={success}
      loading={loading}
      onSubmit={onSubmit}
    />
  )
}
