type LoadingStateProps = {
  message?: string
}

export function LoadingState({ message = 'Cargando…' }: LoadingStateProps) {
  return <p className="donaton-muted">{message}</p>
}
