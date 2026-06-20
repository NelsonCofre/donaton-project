import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'

import { InfoRow } from './InfoRow'

describe('InfoRow', () => {
  it('renderiza etiqueta y valor', () => {
    render(<InfoRow label="Ubicación" value="Santiago" />)

    expect(screen.getByText('Ubicación')).toBeInTheDocument()
    expect(screen.getByText('Santiago')).toBeInTheDocument()
  })
})
