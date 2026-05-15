import type { CreateDonacionRequest, Donacion } from '../model/types'

export function donacionToFormValues(donacion: Donacion): CreateDonacionRequest {
  const fecha =
    donacion.fecha.length >= 10 ? donacion.fecha.slice(0, 10) : donacion.fecha
  return {
    nombreDonante: donacion.donante?.nombre ?? '',
    contactoDonante: donacion.donante?.contacto ?? '',
    tipoRecurso: donacion.recursoTipos?.[0] ?? '',
    cantidad: donacion.cantidad,
    fecha,
  }
}
