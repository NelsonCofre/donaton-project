type LoadingStateProps = {
  message?: string
}

export function LoadingState({ message = 'Cargando…' }: LoadingStateProps) {
  return (
    <div className="donaton-loading-state" aria-live="polite">
      <span className="donaton-loading-state__pulse" />
      <p className="donaton-muted">{message}</p>
    </div>
  )
}
