import { describe, expect, it } from 'vitest'

import type { Donacion } from '../model/types'
import { donacionToFormValues } from './mapDonacion'

describe('donacionToFormValues', () => {
  it('adapta una donación completa a valores de formulario', () => {
    const donacion: Donacion = {
      idDonacion: 10,
      fecha: '2026-06-19T10:30:00',
      cantidad: 25,
      estado: 'PENDIENTE',
      idDonante: 5,
      donante: { idDonante: 5, nombre: 'Ana Pérez', contacto: 'ana@mail.com' },
      recursoTipos: ['Agua'],
    }

    expect(donacionToFormValues(donacion)).toEqual({
      nombreDonante: 'Ana Pérez',
      contactoDonante: 'ana@mail.com',
      tipoRecurso: 'Agua',
      cantidad: 25,
      fecha: '2026-06-19',
    })
  })

  it('usa valores vacíos cuando faltan datos opcionales', () => {
    const donacion: Donacion = {
      idDonacion: 11,
      fecha: '2026-06-19',
      cantidad: 8,
      estado: 'RECIBIDA',
      idDonante: 6,
    }

    expect(donacionToFormValues(donacion)).toMatchObject({
      nombreDonante: '',
      contactoDonante: '',
      tipoRecurso: '',
      cantidad: 8,
      fecha: '2026-06-19',
    })
  })
})
