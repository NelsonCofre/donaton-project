import type { ReactNode } from 'react'

type FieldGroupProps = {
  children: ReactNode
}

export function FieldGroup({ children }: FieldGroupProps) {
  return <div className="donaton-field-group">{children}</div>
}
