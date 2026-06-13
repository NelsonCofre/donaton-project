type ErrorStateProps = {
  message: string
  onRetry?: () => void
}

export function ErrorState({ message, onRetry }: ErrorStateProps) {
  return (
    <div className="donaton-state donaton-state--error">
      <div className="donaton-alert donaton-alert--error" role="alert">
        {message}
      </div>
      {onRetry ? (
        <button type="button" className="donaton-btn donaton-btn--secondary" onClick={onRetry}>
          Reintentar
        </button>
      ) : null}
    </div>
  )
}
