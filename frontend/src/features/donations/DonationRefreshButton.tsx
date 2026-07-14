type DonationRefreshButtonProps = {
  loading: boolean
  onRefresh: () => void
}

export function DonationRefreshButton({ loading, onRefresh }: DonationRefreshButtonProps) {
  return (
    <button
      type="button"
      className="donaton-btn donaton-btn--secondary"
      onClick={onRefresh}
      disabled={loading}
    >
      {loading ? 'Actualizando…' : 'Actualizar'}
    </button>
  )
}
