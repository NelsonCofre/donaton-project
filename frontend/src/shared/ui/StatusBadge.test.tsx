import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'

import { StatusBadge } from './StatusBadge'

describe('StatusBadge', () => {
  it('renderiza el texto recibido', () => {
    render(<StatusBadge label="PENDIENTE" />)

    expect(screen.getByText('PENDIENTE')).toBeInTheDocument()
  })

  it('infiere tono warning para estados pendientes', () => {
    render(<StatusBadge label="En preparación" />)

    expect(screen.getByText('En preparación')).toHaveClass('donaton-badge--warning')
  })

  it('permite forzar el tono desde props', () => {
    render(<StatusBadge label="Manual" tone="success" />)

    expect(screen.getByText('Manual')).toHaveClass('donaton-badge--success')
  })
})
