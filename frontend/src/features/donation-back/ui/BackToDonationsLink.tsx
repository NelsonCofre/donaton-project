import { Link } from 'react-router-dom'

export function BackToDonationsLink() {
  return (
    <Link className="donaton-link-action" to="/donaciones">
      Volver a donaciones
    </Link>
  )
}
