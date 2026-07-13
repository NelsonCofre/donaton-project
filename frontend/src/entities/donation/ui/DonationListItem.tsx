import { StatusBadge } from '@/shared/ui'
import type { Donacion } from '../model/types'
type Props = { donacion: Donacion; isEditing?: boolean; isDeleting?: boolean; onSelect?: (value: Donacion) => void; onEdit?: (value: Donacion) => void; onDelete?: (id: number) => void }
export function DonationListItem({ donacion, isEditing = false, isDeleting = false, onSelect, onEdit, onDelete }: Props) {
  return <tr className={isEditing ? 'donaton-row--active' : undefined} onClick={() => onSelect?.(donacion)} tabIndex={onSelect ? 0 : undefined} onKeyDown={(event) => { if (onSelect && (event.key === 'Enter' || event.key === ' ')) { event.preventDefault(); onSelect(donacion) } }}>
    <td>{donacion.idDonacion}</td><td>{new Date(donacion.fecha).toLocaleDateString()}</td><td>{donacion.cantidad}</td><td><StatusBadge label={donacion.estado} /></td><td>{donacion.donante?.nombre ?? '-'}</td><td>{donacion.donante?.contacto ?? '-'}</td><td>{donacion.recursoTipos?.length ? donacion.recursoTipos.join(', ') : '-'}</td>
    {onEdit || onDelete ? <td><div className="donaton-table-actions">
      {onEdit ? <button type="button" className="donaton-btn donaton-btn--small donaton-btn--secondary" onClick={(event) => { event.stopPropagation(); onEdit(donacion) }} disabled={isDeleting}>{isEditing ? 'Editando…' : 'Editar'}</button> : null}
      {onDelete ? <button type="button" className="donaton-btn donaton-btn--small donaton-btn--danger" onClick={(event) => { event.stopPropagation(); onDelete(donacion.idDonacion) }} disabled={isDeleting || isEditing}>{isDeleting ? 'Eliminando…' : 'Eliminar'}</button> : null}
    </div></td> : null}
  </tr>
}
