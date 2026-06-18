import type { ReactNode } from 'react'

type SectionCardProps = {
  eyebrow?: string
  title?: string
  children: ReactNode
  variant?: 'default' | 'highlight' | 'subtle'
}

export function SectionCard({
  eyebrow,
  title,
  children,
  variant = 'default',
}: SectionCardProps) {
  const variantClass =
    variant === 'highlight'
      ? ' donaton-card--highlight'
      : variant === 'subtle'
        ? ' donaton-card--subtle'
        : ''

  return (
    <section className={`donaton-card${variantClass}`}>
      {eyebrow ? <p className="donaton-section-eyebrow">{eyebrow}</p> : null}
      {title ? <h2 className="donaton-section-title">{title}</h2> : null}
      {children}
    </section>
  )
}
