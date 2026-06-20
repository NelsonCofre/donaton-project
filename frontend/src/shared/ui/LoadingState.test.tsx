import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'

import { LoadingState } from './LoadingState'

describe('LoadingState', () => {
  it('muestra el mensaje por defecto', () => {
    render(<LoadingState />)

    expect(screen.getByText('Cargando…')).toBeInTheDocument()
  })

  it('permite personalizar el mensaje', () => {
    render(<LoadingState message="Cargando donaciones" />)

    expect(screen.getByText('Cargando donaciones')).toBeInTheDocument()
  })
})
