import type { ReactNode } from 'react'

type EmptyStateProps = {
  title: string
  description?: string
  action?: ReactNode
}

export function EmptyState({ title, description, action }: EmptyStateProps) {
  return (
    <div className="donaton-empty-state">
      <h2>{title}</h2>
      {description ? <p className="donaton-muted">{description}</p> : null}
      {action ? <div>{action}</div> : null}
    </div>
  )
}
