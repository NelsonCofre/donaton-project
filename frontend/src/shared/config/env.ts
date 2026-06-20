export const env = {
  apiBaseUrl: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080',
  necessityApiBaseUrl: import.meta.env.VITE_NECESSITY_API_BASE_URL ?? '',
  logisticsApiBaseUrl: import.meta.env.VITE_LOGISTICS_API_BASE_URL ?? '',
} as const
