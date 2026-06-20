import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'

import { DonationStatusChip } from './DonationStatusChip'

describe('DonationStatusChip', () => {
  it('renderiza el estado recibido', () => {
    render(<DonationStatusChip estado="PENDIENTE" />)

    expect(screen.getByText('PENDIENTE')).toBeInTheDocument()
  })

  it('aplica estilo warning para pendiente', () => {
    render(<DonationStatusChip estado="PENDIENTE" />)

    expect(screen.getByText('PENDIENTE')).toHaveClass('donaton-badge--warning')
  })
})
