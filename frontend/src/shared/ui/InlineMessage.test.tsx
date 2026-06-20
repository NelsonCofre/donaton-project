import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'

import { InlineMessage } from './InlineMessage'

describe('InlineMessage', () => {
  it('usa tono informativo por defecto', () => {
    render(<InlineMessage>Mensaje general</InlineMessage>)

    expect(screen.getByText('Mensaje general')).toHaveClass('donaton-inline-message--info')
  })

  it('usa role alert para errores', () => {
    render(<InlineMessage tone="error">No se pudo guardar</InlineMessage>)

    expect(screen.getByRole('alert')).toHaveTextContent('No se pudo guardar')
  })
})
