type MetricCardProps = {
  label: string
  value: string | number
  hint?: string
  tone?: 'neutral' | 'info' | 'success' | 'warning'
}

export function MetricCard({
  label,
  value,
  hint,
  tone = 'neutral',
}: MetricCardProps) {
  return (
    <article className={`donaton-metric-card donaton-metric-card--${tone}`}>
      <p className="donaton-metric-card__label">{label}</p>
      <strong className="donaton-metric-card__value">{value}</strong>
      {hint ? <p className="donaton-metric-card__hint">{hint}</p> : null}
    </article>
  )
}
