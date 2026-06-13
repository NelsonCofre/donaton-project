import type { ReactNode } from 'react'

type SectionCardProps = {
  title?: string
  children: ReactNode
  variant?: 'default' | 'highlight'
}

export function SectionCard({ title, children, variant = 'default' }: SectionCardProps) {
  return (
    <section
      className={`donaton-card${variant === 'highlight' ? ' donaton-card--highlight' : ''}`}
    >
      {title ? <h2 className="donaton-section-title">{title}</h2> : null}
      {children}
    </section>
  )
}
