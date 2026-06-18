type InfoRowProps = {
  label: string
  value: string | number
}

export function InfoRow({ label, value }: InfoRowProps) {
  return (
    <div className="donaton-info-row">
      <dt>{label}</dt>
      <dd>{value}</dd>
    </div>
  )
}
