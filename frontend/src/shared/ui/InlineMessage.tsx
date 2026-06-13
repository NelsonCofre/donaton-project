type InlineMessageProps = {
  tone?: 'info' | 'success' | 'error'
  children: string
}

export function InlineMessage({ tone = 'info', children }: InlineMessageProps) {
  if (tone === 'error') {
    return (
      <div className="donaton-alert donaton-alert--error" role="alert">
        {children}
      </div>
    )
  }

  return (
    <p className={`donaton-banner donaton-banner--${tone === 'success' ? 'ok' : 'info'}`}>
      {children}
    </p>
  )
}
