import { useState } from 'react'
import { ApiError } from '@/shared/api/client'
import { createDonacion, type CreateDonacionRequest } from '@/entities/donation'
import { DonationForm } from '@/features/donation-form'

type CreateDonationFormProps = {
  onCreated: () => void
}

export function CreateDonationForm({ onCreated }: CreateDonationFormProps) {
  const [formKey, setFormKey] = useState(0)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  async function handleSubmit(values: CreateDonacionRequest) {
    setError(null)
    setSuccess(null)
    setLoading(true)
    try {
      await createDonacion({
        ...values,
        fecha: new Date(values.fecha).toISOString(),
      })
      setSuccess('Donación registrada correctamente.')
      setFormKey((k) => k + 1)
      onCreated()
    } catch (err) {
      setError(err instanceof ApiError ? err.message : 'No se pudo crear la donación.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <DonationForm
      key={formKey}
      title="Nueva donación"
      submitLabel="Registrar donación"
      error={error}
      success={success}
      loading={loading}
      onSubmit={handleSubmit}
    />
  )
}
