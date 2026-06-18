import type { ReactNode } from 'react'

type ActionBarProps = {
  children: ReactNode
  compact?: boolean
}

export function ActionBar({ children, compact = false }: ActionBarProps) {
  return (
    <div className={`donaton-action-bar${compact ? ' donaton-action-bar--compact' : ''}`}>
      {children}
    </div>
  )
}
