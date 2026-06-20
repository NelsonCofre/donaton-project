import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'

import { EmptyState } from './EmptyState'

describe('EmptyState', () => {
  it('muestra título y descripción cuando se entregan', () => {
    render(<EmptyState title="Sin donaciones" description="Crea la primera donación." />)

    expect(screen.getByText('Sin resultados')).toBeInTheDocument()
    expect(screen.getByRole('heading', { name: 'Sin donaciones' })).toBeInTheDocument()
    expect(screen.getByText('Crea la primera donación.')).toBeInTheDocument()
  })

  it('renderiza una acción opcional', () => {
    render(<EmptyState title="Sin centros" action={<button type="button">Crear centro</button>} />)

    expect(screen.getByRole('button', { name: 'Crear centro' })).toBeInTheDocument()
  })
})
