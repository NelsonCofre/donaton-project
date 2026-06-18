type ErrorStateProps = {
  message: string
  onRetry?: () => void
}

export function ErrorState({ message, onRetry }: ErrorStateProps) {
  return (
    <div className="donaton-state donaton-state--error">
      <div className="donaton-inline-message donaton-inline-message--error" role="alert">
        <strong>No pudimos cargar esta sección.</strong>
        <span>{message}</span>
      </div>
      {onRetry ? (
        <button type="button" className="donaton-btn donaton-btn--secondary" onClick={onRetry}>
          Reintentar
        </button>
      ) : null}
    </div>
  )
}
