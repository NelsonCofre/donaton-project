type DeleteDonationButtonProps = {
  loading: boolean
  onDelete: () => void
}

export function DeleteDonationButton({ loading, onDelete }: DeleteDonationButtonProps) {
  function handleClick() {
    if (window.confirm('¿Eliminar esta donación?')) {
      onDelete()
    }
  }

  return (
    <button
      type="button"
      className="donaton-btn donaton-btn--danger"
      onClick={handleClick}
      disabled={loading}
    >
      {loading ? 'Eliminando…' : 'Eliminar donación'}
    </button>
  )
}
