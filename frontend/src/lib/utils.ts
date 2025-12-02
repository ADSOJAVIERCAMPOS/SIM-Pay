import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatCurrency(amount: number): string {
  return new Intl.NumberFormat('es-CO', {
    style: 'currency',
    currency: 'COP',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(amount)
}

export function formatDate(date: string | Date): string {
  const dateObj = typeof date === 'string' ? new Date(date) : date
  return new Intl.DateTimeFormat('es-CO', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }).format(dateObj)
}

export function generateWhatsAppLink(phone: string, message: string): string {
  const encodedMessage = encodeURIComponent(message)
  return `https://wa.me/57${phone}?text=${encodedMessage}`
}

export function generateNequiLink(amount: number): string {
  // Simulación del link de Nequi (en producción sería la URL real de Nequi)
  return `https://nequi.com.co/pagar?amount=${amount}&merchant=SIM-Pay`
}

export function generateDaviplataLink(amount: number): string {
  // Simulación del link de Daviplata (en producción sería la URL real de Daviplata)
  return `https://daviplata.com/pagar?amount=${amount}&merchant=SIM-Pay`
}