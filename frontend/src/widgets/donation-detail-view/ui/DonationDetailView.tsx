import { useCallback, useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import type { CreateDonacionRequest, Donacion } from '@/entities/donation'
import { deleteDonacion, fetchDonacionById, updateDonacion } from '@/entities/donation'
import { BackToDonationsLink } from '@/features/donation-back'
import { DeleteDonationButton } from '@/features/donation-delete'
import { DonationDetailCard } from '@/features/donation-detail-card'
import { EditDonationSection } from '@/features/donation-edit'
import { ApiError } from '@/shared/api/client'
import {
  ActionBar,
  ErrorState,
  InlineMessage,
  LoadingState,
  PageHeader,
  SectionCard,
} from '@/shared/ui'

export function DonationDetailView() {
  const { id } = useParams()
  const navigate = useNavigate()
  const donationId = Number(id)
  const [donacion, setDonacion] = useState<Donacion | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [editError, setEditError] = useState<string | null>(null)
  const [editSuccess, setEditSuccess] = useState<string | null>(null)
  const [editLoading, setEditLoading] = useState(false)
  const [deleteLoading, setDeleteLoading] = useState(false)

  const load = useCallback(async () => {
    if (!Number.isFinite(donationId)) {
      setError('La donación solicitada no es válida.')
      setLoading(false)
      return
    }

    setLoading(true)
    setError(null)
    try {
      const data = await fetchDonacionById(donationId)
      setDonacion(data)
    } catch (err) {
      setDonacion(null)
      setError(
        err instanceof ApiError
          ? err.message
          : 'No se pudo cargar el detalle de la donación.',
      )
    } finally {
      setLoading(false)
    }
  }, [donationId])

  useEffect(() => {
    void Promise.resolve().then(() => load())
  }, [load])

  async function handleUpdate(values: CreateDonacionRequest) {
    if (!donacion) return

    setEditError(null)
    setEditSuccess(null)
    setEditLoading(true)
    try {
      const updated = await updateDonacion(donacion.idDonacion, {
        ...values,
        fecha: new Date(values.fecha).toISOString(),
      })
      setDonacion(updated)
      setEditSuccess('Donación actualizada correctamente.')
    } catch (err) {
      setEditError(
        err instanceof ApiError ? err.message : 'No se pudo actualizar la donación.',
      )
    } finally {
      setEditLoading(false)
    }
  }

  async function handleDelete() {
    if (!donacion) return

    setDeleteLoading(true)
    setError(null)
    try {
      await deleteDonacion(donacion.idDonacion)
      navigate('/donaciones', {
        replace: true,
        state: { success: 'Donación eliminada correctamente.' },
      })
    } catch (err) {
      setError(err instanceof ApiError ? err.message : 'No se pudo eliminar la donación.')
    } finally {
      setDeleteLoading(false)
    }
  }

  return (
    <>
      <PageHeader
        title="Detalle de donación"
        description="Revisa el estado completo, edita datos o elimina el registro."
        actions={<BackToDonationsLink />}
      />

      {loading ? <LoadingState message="Cargando detalle de donación…" /> : null}
      {!loading && error ? <ErrorState message={error} onRetry={() => void load()} /> : null}

      {!loading && !error && donacion ? (
        <>
          <SectionCard>
            <DonationDetailCard donacion={donacion} />
            <ActionBar>
              <DeleteDonationButton loading={deleteLoading} onDelete={() => void handleDelete()} />
            </ActionBar>
          </SectionCard>

          {editSuccess ? <InlineMessage tone="success">{editSuccess}</InlineMessage> : null}

          <SectionCard title="Editar donación" variant="highlight">
            <EditDonationSection
              donacion={donacion}
              error={editError}
              success={null}
              loading={editLoading}
              onSubmit={(values) => void handleUpdate(values)}
            />
          </SectionCard>
        </>
      ) : null}
    </>
  )
}
