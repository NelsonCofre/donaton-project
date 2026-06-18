import { InlineMessage } from '@/shared/ui'

type DonationSubmitFeedbackProps = {
  message: string | null
  tone?: 'success' | 'error'
}

export function DonationSubmitFeedback({
  message,
  tone = 'success',
}: DonationSubmitFeedbackProps) {
  return message ? <InlineMessage tone={tone}>{message}</InlineMessage> : null
}
