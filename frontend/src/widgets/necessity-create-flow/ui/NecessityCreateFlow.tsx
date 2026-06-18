import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getNecessityRepository } from '@/entities/necessity'
import { NecessityForm } from '@/features/necessity-form'
import { ActionBar, PageHeader, SectionCard } from '@/shared/ui'

export function NecessityCreateFlow() {
  const navigate = useNavigate()
  const repository = getNecessityRepository()
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  async function handleSubmit(values: Parameters<typeof repository.create>[0]) {
    setError(null)
    setSuccess(null)
    setLoading(true)
    try {
      await repository.create(values)
      setSuccess('Necesidad registrada correctamente.')
      navigate('/necesidades', {
        replace: true,
        state: { success: 'Necesidad registrada correctamente.' },
      })
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo crear la necesidad.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <>
      <PageHeader
        title="Nueva necesidad"
        description="Registra una necesidad para modelar la operacion de terreno desde el frontend."
        actions={
          <button
            type="button"
            className="donaton-btn donaton-btn--secondary"
            onClick={() => navigate('/necesidades')}
          >
            Cancelar
          </button>
        }
      />

      <SectionCard>
        <NecessityForm
          title="Nueva necesidad"
          submitLabel="Registrar necesidad"
          error={error}
          success={success}
          loading={loading}
          onSubmit={(values) => void handleSubmit(values)}
        />
        <ActionBar>
          <button
            type="button"
            className="donaton-btn donaton-btn--secondary"
            onClick={() => navigate('/necesidades')}
          >
            Volver al listado
          </button>
        </ActionBar>
      </SectionCard>
    </>
  )
}
