import { useCallback, useEffect, useState } from 'react'
import { ApiError } from '@/shared/api/client'
import type { Donacion } from '@/entities/donation'
import { fetchDonaciones } from '@/entities/donation'
import { CreateDonationForm } from '@/features/donation-create'
import { DonationList } from '@/features/donation-list'

export function DonationsDashboard() {
  const [donaciones, setDonaciones] = useState<Donacion[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

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
          : 'No se pudieron obtener las donaciones. Comprueba el BFF y VITE_API_BASE_URL.',
      )
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void Promise.resolve().then(() => load())
  }, [load])

  return (
    <>
      <h1>Donaciones</h1>
      <p className="donaton-muted">
        Listado y alta de donaciones. Las peticiones van al BFF (`GET/POST
        /api/donations`).
      </p>
      <section className="donaton-card">
        <DonationList donaciones={donaciones} loading={loading} error={error} />
      </section>
      <section className="donaton-card">
        <CreateDonationForm onCreated={() => void load()} />
      </section>
    </>
  )
}
