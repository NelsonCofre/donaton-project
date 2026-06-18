import { Link } from 'react-router-dom'

export function CancelDonationCreationLink() {
  return (
    <Link className="donaton-btn donaton-btn--secondary" to="/donaciones">
      Cancelar
    </Link>
  )
}
