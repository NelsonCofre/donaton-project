import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'
import { DonationStatusChip } from './DonationStatusChip'

describe('DonationStatusChip', () => {
  it('muestra el estado recibido', () => {
    render(<DonationStatusChip estado="RECIBIDA" />)
    expect(screen.getByText('RECIBIDA')).toBeInTheDocument()
  })
})
