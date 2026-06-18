import type { ReactNode } from 'react'

type ToolbarProps = {
  children: ReactNode
}

export function Toolbar({ children }: ToolbarProps) {
  return <div className="donaton-toolbar-panel">{children}</div>
}
