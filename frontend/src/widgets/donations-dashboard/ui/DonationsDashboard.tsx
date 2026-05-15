import { useCallback, useEffect, useState } from 'react'
import { ApiError } from '@/shared/api/client'
import type { Donacion } from '@/entities/donation'
import {
  deleteDonacion,
  fetchDonaciones,
  updateDonacion,
} from '@/entities/donation'
import { donacionToFormValues } from '@/entities/donation/lib/mapDonacion'
import type { CreateDonacionRequest } from '@/entities/donation'
import { CreateDonationForm } from '@/features/donation-create'
import { DonationForm } from '@/features/donation-form'
import { DonationList } from '@/features/donation-list'

export function DonationsDashboard() {
  const [donaciones, setDonaciones] = useState<Donacion[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [editing, setEditing] = useState<Donacion | null>(null)
  const [editError, setEditError] = useState<string | null>(null)
  const [editSuccess, setEditSuccess] = useState<string | null>(null)
  const [editLoading, setEditLoading] = useState(false)
  const [deletingId, setDeletingId] = useState<number | null>(null)

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await fetchDonaciones()
      setDonaciones(data)
    } catch (err) {
      setDonaciones([])
      setError(
        err instanceof ApiError
          ? err.message
          : 'No se pudieron cargar las donaciones. Inténtalo de nuevo en unos instantes.',
      )
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void Promise.resolve().then(() => load())
  }, [load])

  function handleEdit(donacion: Donacion) {
    setEditing(donacion)
    setEditError(null)
    setEditSuccess(null)
  }

  function cancelEdit() {
    setEditing(null)
    setEditError(null)
    setEditSuccess(null)
  }

  async function handleUpdate(values: CreateDonacionRequest) {
    if (!editing) return
    setEditError(null)
    setEditSuccess(null)
    setEditLoading(true)
    try {
      await updateDonacion(editing.idDonacion, {
        ...values,
        fecha: new Date(values.fecha).toISOString(),
      })
      setEditSuccess('Donación actualizada.')
      setEditing(null)
      await load()
    } catch (err) {
      setEditError(
        err instanceof ApiError ? err.message : 'No se pudo actualizar la donación.',
      )
    } finally {
      setEditLoading(false)
    }
  }

  async function handleDelete(id: number) {
    const confirmed = window.confirm('¿Eliminar esta donación?')
    if (!confirmed) return
    setDeletingId(id)
    setError(null)
    try {
      await deleteDonacion(id)
      if (editing?.idDonacion === id) {
        cancelEdit()
      }
      await load()
    } catch (err) {
      setError(
        err instanceof ApiError ? err.message : 'No se pudo eliminar la donación.',
      )
    } finally {
      setDeletingId(null)
    }
  }

  return (
    <>
      <h1>Donaciones</h1>

      <section className="donaton-card">
        <h2 className="donaton-section-title">Listado</h2>
        <DonationList
          donaciones={donaciones}
          loading={loading}
          error={error}
          editingId={editing?.idDonacion ?? null}
          deletingId={deletingId}
          onEdit={handleEdit}
          onDelete={handleDelete}
        />
      </section>

      {editing ? (
        <section className="donaton-card donaton-card--highlight">
          <DonationForm
            key={editing.idDonacion}
            title={`Editar donación #${editing.idDonacion}`}
            submitLabel="Guardar cambios"
            initialValues={donacionToFormValues(editing)}
            error={editError}
            success={editSuccess}
            loading={editLoading}
            onSubmit={handleUpdate}
            onCancel={cancelEdit}
          />
        </section>
      ) : null}

      <section className="donaton-card">
        <CreateDonationForm
          onCreated={() => {
            void load()
          }}
        />
      </section>
    </>
  )
}
