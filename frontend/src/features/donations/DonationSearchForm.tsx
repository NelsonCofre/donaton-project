type DonationSearchFormProps = {
  value: string
  onChange: (value: string) => void
}

export function DonationSearchForm({ value, onChange }: DonationSearchFormProps) {
  return (
    <div className="donaton-field donaton-field--inline">
      <label htmlFor="donation-search">Buscar</label>
      <input
        id="donation-search"
        type="search"
        placeholder="ID, donante, contacto o recurso"
        value={value}
        onChange={(event) => onChange(event.target.value)}
      />
    </div>
  )
}
