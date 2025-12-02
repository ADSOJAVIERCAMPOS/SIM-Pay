'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { PaymentMobile } from '@/components/PaymentMobile'
import { ArrowLeft, CreditCard, Smartphone, QrCode, MessageCircle, ExternalLink } from 'lucide-react'
import Link from 'next/link'

interface Payment {
  paymentId: string
  transaccionId: string
  paymentMethod: string
  amount: number
  status: string
  confirmationCode: string
  createdAt: string
  expiresAt: string
}

export default function PaymentsPage() {
  const [payments, setPayments] = useState<Payment[]>([])
  const [loading, setLoading] = useState(true)
  const [showNewPayment, setShowNewPayment] = useState(false)

  useEffect(() => {
    fetchPayments()
  }, [])

  const fetchPayments = async () => {
    try {
      const response = await fetch('/api/payments')
      if (response.ok) {
        const data = await response.json()
        setPayments(data)
      }
    } catch (error) {
      console.error('Error fetching payments:', error)
    } finally {
      setLoading(false)
    }
  }

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0,
    }).format(amount)
  }

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'CONFIRMED': return 'bg-green-500'
      case 'PENDING': return 'bg-yellow-500'
      case 'EXPIRED': return 'bg-red-500'
      default: return 'bg-gray-500'
    }
  }

  const getStatusText = (status: string) => {
    switch (status) {
      case 'CONFIRMED': return 'Confirmado'
      case 'PENDING': return 'Pendiente'
      case 'EXPIRED': return 'Expirado'
      default: return 'Desconocido'
    }
  }

  const getMethodIcon = (method: string) => {
    switch (method) {
      case 'NEQUI': return '💜'
      case 'DAVIPLATA': return '🔴'
      default: return '💳'
    }
  }

  if (showNewPayment) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="max-w-2xl mx-auto">
          <div className="mb-6">
            <Button
              variant="outline"
              onClick={() => setShowNewPayment(false)}
              className="mb-4"
            >
              <ArrowLeft className="w-4 h-4 mr-2" />
              Volver a Pagos
            </Button>
          </div>
          
          <PaymentMobile
            transaccionId="demo-transaction"
            amount={150000}
            description="Producto de demostración"
            onPaymentComplete={(paymentId) => {
              console.log('Payment completed:', paymentId)
              setShowNewPayment(false)
              fetchPayments()
            }}
          />
        </div>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8">
      {/* Header */}
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-3xl font-bold">💸 Pagos Móviles</h1>
          <p className="text-gray-600 mt-2">
            Gestiona pagos con Nequi, Daviplata y WhatsApp
          </p>
        </div>
        
        <div className="flex gap-3">
          <Link href="/dashboard">
            <Button variant="outline">
              <ArrowLeft className="w-4 h-4 mr-2" />
              Dashboard
            </Button>
          </Link>
          <Button onClick={() => setShowNewPayment(true)}>
            <Smartphone className="w-4 h-4 mr-2" />
            Nuevo Pago
          </Button>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">
              Total Pagos
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{payments.length}</div>
            <div className="flex items-center gap-1 text-sm text-gray-600 mt-1">
              <CreditCard className="w-3 h-3" />
              Todos los métodos
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">
              Confirmados
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">
              {payments.filter(p => p.status === 'CONFIRMED').length}
            </div>
            <div className="flex items-center gap-1 text-sm text-green-600 mt-1">
              ✅ Exitosos
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">
              Pendientes
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-yellow-600">
              {payments.filter(p => p.status === 'PENDING').length}
            </div>
            <div className="flex items-center gap-1 text-sm text-yellow-600 mt-1">
              ⏳ En proceso
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">
              Total Monto
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {formatCurrency(payments.reduce((sum, p) => sum + p.amount, 0))}
            </div>
            <div className="flex items-center gap-1 text-sm text-gray-600 mt-1">
              💰 COP
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Payments List */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            📱 Historial de Pagos Móviles
          </CardTitle>
          <CardDescription>
            Todos los pagos generados con Nequi, Daviplata y WhatsApp
          </CardDescription>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900 mx-auto"></div>
              <p className="text-gray-600 mt-2">Cargando pagos...</p>
            </div>
          ) : payments.length === 0 ? (
            <div className="text-center py-12">
              <Smartphone className="w-12 h-12 text-gray-400 mx-auto mb-4" />
              <h3 className="text-lg font-medium text-gray-900 mb-2">
                No hay pagos aún
              </h3>
              <p className="text-gray-600 mb-4">
                Crea tu primer pago móvil para comenzar
              </p>
              <Button onClick={() => setShowNewPayment(true)}>
                <Smartphone className="w-4 h-4 mr-2" />
                Crear Primer Pago
              </Button>
            </div>
          ) : (
            <div className="space-y-4">
              {payments.map((payment) => (
                <Card key={payment.paymentId} className="border-l-4 border-l-blue-500">
                  <CardContent className="p-4">
                    <div className="flex justify-between items-start">
                      <div className="flex-1">
                        <div className="flex items-center gap-3 mb-2">
                          <span className="text-2xl">
                            {getMethodIcon(payment.paymentMethod)}
                          </span>
                          <div>
                            <div className="font-medium">
                              {payment.paymentMethod}
                            </div>
                            <div className="text-sm text-gray-600">
                              {payment.confirmationCode}
                            </div>
                          </div>
                        </div>
                        
                        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                          <div>
                            <span className="text-gray-600">Monto:</span>
                            <div className="font-medium">
                              {formatCurrency(payment.amount)}
                            </div>
                          </div>
                          <div>
                            <span className="text-gray-600">Estado:</span>
                            <div>
                              <Badge 
                                className={`${getStatusColor(payment.status)} text-white`}
                              >
                                {getStatusText(payment.status)}
                              </Badge>
                            </div>
                          </div>
                          <div>
                            <span className="text-gray-600">Creado:</span>
                            <div className="font-medium">
                              {new Date(payment.createdAt).toLocaleDateString('es-CO')}
                            </div>
                          </div>
                          <div>
                            <span className="text-gray-600">Expira:</span>
                            <div className="font-medium">
                              {new Date(payment.expiresAt).toLocaleDateString('es-CO')}
                            </div>
                          </div>
                        </div>
                      </div>
                      
                      <div className="flex gap-2 ml-4">
                        <Button variant="outline" size="sm">
                          <QrCode className="w-4 h-4" />
                        </Button>
                        <Button variant="outline" size="sm">
                          <MessageCircle className="w-4 h-4" />
                        </Button>
                        <Button variant="outline" size="sm">
                          <ExternalLink className="w-4 h-4" />
                        </Button>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}