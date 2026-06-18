type StatusBadgeProps = {
  label: string
  tone?: 'neutral' | 'info' | 'success' | 'warning' | 'danger'
}

function inferTone(label: string): NonNullable<StatusBadgeProps['tone']> {
  const normalized = label.toLowerCase()

  if (
    normalized.includes('cancel') ||
    normalized.includes('critica') ||
    normalized.includes('crítica')
  ) {
    return 'danger'
  }

  if (
    normalized.includes('pendiente') ||
    normalized.includes('proceso') ||
    normalized.includes('preparacion') ||
    normalized.includes('preparación')
  ) {
    return 'warning'
  }

  if (
    normalized.includes('entregado') ||
    normalized.includes('cubierta') ||
    normalized.includes('recibida')
  ) {
    return 'success'
  }

  if (normalized.includes('transito') || normalized.includes('tránsito')) {
    return 'info'
  }

  return 'neutral'
}

export function StatusBadge({ label, tone }: StatusBadgeProps) {
  const resolvedTone = tone ?? inferTone(label)
  return <span className={`donaton-badge donaton-badge--${resolvedTone}`}>{label}</span>
}
