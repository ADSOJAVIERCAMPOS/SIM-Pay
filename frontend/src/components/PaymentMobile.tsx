'use client'

import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'

interface PaymentProps {
  transaccionId: string
  amount: number
  description: string
  onPaymentComplete?: (paymentId: string) => void
}

interface PaymentResponse {
  paymentId: string
  transaccionId: string
  paymentMethod: string
  amount: number
  status: string
  paymentUrl: string
  confirmationCode: string
  qrCode: string
  whatsappMessage: string
  createdAt: string
  expiresAt: string
}

export function PaymentMobile({ transaccionId, amount, description, onPaymentComplete }: PaymentProps) {
  const [selectedMethod, setSelectedMethod] = useState<'NEQUI' | 'DAVIPLATA' | null>(null)
  const [customerName, setCustomerName] = useState('')
  const [customerPhone, setCustomerPhone] = useState('')
  const [payment, setPayment] = useState<PaymentResponse | null>(null)
  const [confirmationCode, setConfirmationCode] = useState('')
  const [loading, setLoading] = useState(false)
  const [step, setStep] = useState<'select' | 'details' | 'payment' | 'confirm'>('select')

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0,
    }).format(amount)
  }

  const generatePayment = async () => {
    if (!selectedMethod || !customerName || !customerPhone) return

    setLoading(true)
    try {
      const response = await fetch('/api/payments/whatsapp/send', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          transaccionId,
          amount,
          currency: 'COP',
          description,
          customerName,
          customerPhone,
          paymentMethod: selectedMethod,
        }),
      })

      if (response.ok) {
        const paymentData = await response.json()
        setPayment(paymentData)
        setStep('payment')
      }
    } catch (error) {
      console.error('Error generando pago:', error)
    } finally {
      setLoading(false)
    }
  }

  const confirmPayment = async () => {
    if (!payment || !confirmationCode) return

    setLoading(true)
    try {
      const response = await fetch(`/api/payments/${payment.paymentId}/confirm`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ confirmationCode }),
      })

      if (response.ok) {
        setStep('confirm')
        onPaymentComplete?.(payment.paymentId)
      }
    } catch (error) {
      console.error('Error confirmando pago:', error)
    } finally {
      setLoading(false)
    }
  }

  const openWhatsApp = () => {
    if (payment?.paymentUrl) {
      window.open(payment.paymentUrl, '_blank')
    }
  }

  if (step === 'select') {
    return (
      <Card className="w-full max-w-md mx-auto">
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            📱 Pago Móvil
          </CardTitle>
          <CardDescription>
            {description} - {formatCurrency(amount)}
          </CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <Button
              variant={selectedMethod === 'NEQUI' ? 'default' : 'outline'}
              onClick={() => setSelectedMethod('NEQUI')}
              className="h-20 flex flex-col gap-2"
            >
              <div className="text-2xl">💜</div>
              <div className="text-sm font-medium">Nequi</div>
            </Button>
            <Button
              variant={selectedMethod === 'DAVIPLATA' ? 'default' : 'outline'}
              onClick={() => setSelectedMethod('DAVIPLATA')}
              className="h-20 flex flex-col gap-2"
            >
              <div className="text-2xl">🔴</div>
              <div className="text-sm font-medium">Daviplata</div>
            </Button>
          </div>
          
          {selectedMethod && (
            <Button 
              onClick={() => setStep('details')}
              className="w-full"
            >
              Continuar con {selectedMethod}
            </Button>
          )}
        </CardContent>
      </Card>
    )
  }

  if (step === 'details') {
    return (
      <Card className="w-full max-w-md mx-auto">
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            👤 Datos del Cliente
          </CardTitle>
          <CardDescription>
            Pago via {selectedMethod} - {formatCurrency(amount)}
          </CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          <div>
            <label className="text-sm font-medium mb-2 block">Nombre completo</label>
            <Input
              placeholder="Ej: Juan Pérez"
              value={customerName}
              onChange={(e) => setCustomerName(e.target.value)}
            />
          </div>
          
          <div>
            <label className="text-sm font-medium mb-2 block">Teléfono WhatsApp</label>
            <Input
              placeholder="Ej: 3001234567"
              value={customerPhone}
              onChange={(e) => setCustomerPhone(e.target.value)}
            />
          </div>

          <div className="flex gap-2">
            <Button 
              variant="outline" 
              onClick={() => setStep('select')}
              className="flex-1"
            >
              Atrás
            </Button>
            <Button 
              onClick={generatePayment}
              disabled={!customerName || !customerPhone || loading}
              className="flex-1"
            >
              {loading ? 'Generando...' : 'Generar Pago'}
            </Button>
          </div>
        </CardContent>
      </Card>
    )
  }

  if (step === 'payment' && payment) {
    return (
      <Card className="w-full max-w-md mx-auto">
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            💸 Pago Generado
          </CardTitle>
          <CardDescription>
            Código: <Badge variant="secondary">{payment.confirmationCode}</Badge>
          </CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="text-center p-4 bg-gray-50 rounded-lg">
            <div className="text-sm text-gray-600 mb-2">Método seleccionado:</div>
            <div className="font-medium text-lg">
              {payment.paymentMethod === 'NEQUI' ? '💜 Nequi' : '🔴 Daviplata'}
            </div>
            <div className="text-2xl font-bold mt-2">
              {formatCurrency(payment.amount)}
            </div>
          </div>

          <Button
            onClick={openWhatsApp}
            className="w-full bg-green-600 hover:bg-green-700"
          >
            📱 Abrir WhatsApp
          </Button>

          <div className="text-center">
            <div className="text-sm text-gray-600 mb-2">
              ¿Ya realizaste el pago?
            </div>
            <Input
              placeholder="Ingresa el código de confirmación"
              value={confirmationCode}
              onChange={(e) => setConfirmationCode(e.target.value)}
              className="mb-2"
            />
            <Button
              onClick={confirmPayment}
              disabled={!confirmationCode || loading}
              variant="outline"
              className="w-full"
            >
              {loading ? 'Confirmando...' : 'Confirmar Pago'}
            </Button>
          </div>

          <div className="text-xs text-gray-500 text-center">
            Válido hasta: {new Date(payment.expiresAt).toLocaleString('es-CO')}
          </div>
        </CardContent>
      </Card>
    )
  }

  if (step === 'confirm') {
    return (
      <Card className="w-full max-w-md mx-auto">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-green-600">
            ✅ Pago Confirmado
          </CardTitle>
          <CardDescription>
            Tu pago ha sido procesado exitosamente
          </CardDescription>
        </CardHeader>
        <CardContent className="text-center space-y-4">
          <div className="text-6xl">🎉</div>
          <div>
            <div className="font-medium">Método: {payment?.paymentMethod}</div>
            <div className="text-2xl font-bold text-green-600">
              {formatCurrency(amount)}
            </div>
            <div className="text-sm text-gray-600">
              Código: {payment?.confirmationCode}
            </div>
          </div>
          
          <Button
            onClick={() => {
              setStep('select')
              setPayment(null)
              setConfirmationCode('')
              setSelectedMethod(null)
              setCustomerName('')
              setCustomerPhone('')
            }}
            className="w-full"
          >
            Nuevo Pago
          </Button>
        </CardContent>
      </Card>
    )
  }

  return null
}