import './globals.css'
import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import { Providers } from './providers'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'SIM-Pay - Sistema de Inventario Modular',
  description: 'Sistema de inventario empresarial con trazabilidad inmutable y pasarela de pagos móviles',
  keywords: 'inventario, trazabilidad, pagos móviles, WhatsApp, Nequi, Daviplata',
  authors: [{ name: 'SIM-Pay Team' }],
  manifest: '/manifest.json',
  themeColor: '#000000',
  viewport: 'width=device-width, initial-scale=1, maximum-scale=1',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="es" suppressHydrationWarning>
      <head />
      <body className={inter.className}>
        <Providers>
          <div className="min-h-screen bg-background font-sans antialiased">
            {children}
          </div>
        </Providers>
      </body>
    </html>
  )
}