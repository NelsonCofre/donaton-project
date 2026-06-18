import type { ReactNode } from 'react'

type PageHeaderProps = {
  eyebrow?: string
  title: string
  description?: string
  actions?: ReactNode
  aside?: ReactNode
}

export function PageHeader({
  eyebrow,
  title,
  description,
  actions,
  aside,
}: PageHeaderProps) {
  return (
    <header className="donaton-page-header">
      <div className="donaton-page-header__content">
        {eyebrow ? <p className="donaton-page-header__eyebrow">{eyebrow}</p> : null}
        <h1>{title}</h1>
        {description ? <p className="donaton-page-header__text">{description}</p> : null}
        {aside ? <div className="donaton-page-header__aside">{aside}</div> : null}
      </div>
      {actions ? <div className="donaton-page-header__actions">{actions}</div> : null}
    </header>
  )
}
