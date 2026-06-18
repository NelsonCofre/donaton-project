type InlineMessageProps = {
  tone?: 'info' | 'success' | 'error'
  children: string
}

export function InlineMessage({ tone = 'info', children }: InlineMessageProps) {
  if (tone === 'error') {
    return (
      <div className="donaton-inline-message donaton-inline-message--error" role="alert">
        {children}
      </div>
    )
  }

  return (
    <p
      className={`donaton-inline-message donaton-inline-message--${
        tone === 'success' ? 'success' : 'info'
      }`}
    >
      {children}
    </p>
  )
}
