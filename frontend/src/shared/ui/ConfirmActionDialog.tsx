type ConfirmActionDialogProps = {
  message: string
  onConfirm: () => void
  children: string
  className?: string
  disabled?: boolean
}

export function ConfirmActionDialog({
  message,
  onConfirm,
  children,
  className = 'donaton-btn',
  disabled = false,
}: ConfirmActionDialogProps) {
  function handleConfirm() {
    if (window.confirm(message)) {
      onConfirm()
    }
  }

  return (
    <button type="button" className={className} onClick={handleConfirm} disabled={disabled}>
      {children}
    </button>
  )
}
