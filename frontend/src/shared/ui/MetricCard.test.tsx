import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'

import { MetricCard } from './MetricCard'

describe('MetricCard', () => {
  it('muestra etiqueta y valor', () => {
    render(<MetricCard label="Donaciones" value={12} />)

    expect(screen.getByText('Donaciones')).toBeInTheDocument()
    expect(screen.getByText('12')).toBeInTheDocument()
  })

  it('muestra hint opcional', () => {
    render(<MetricCard label="Centros" value={3} hint="Activos" />)

    expect(screen.getByText('Activos')).toBeInTheDocument()
  })
})
