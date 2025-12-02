import { NextResponse } from 'next/server'

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

// Simulación de datos de pagos (en producción vendría de la base de datos)
const mockPayments: Payment[] = [
  {
    paymentId: 'pay_001',
    transaccionId: 'txn_001',
    paymentMethod: 'NEQUI',
    amount: 250000,
    status: 'CONFIRMED',
    confirmationCode: 'NEQ123456',
    createdAt: '2024-01-15T10:30:00Z',
    expiresAt: '2024-01-15T22:30:00Z'
  },
  {
    paymentId: 'pay_002',
    transaccionId: 'txn_002',
    paymentMethod: 'DAVIPLATA',
    amount: 180000,
    status: 'PENDING',
    confirmationCode: 'DAV789012',
    createdAt: '2024-01-14T14:15:00Z',
    expiresAt: '2024-01-14T26:15:00Z'
  },
  {
    paymentId: 'pay_003',
    transaccionId: 'txn_003',
    paymentMethod: 'NEQUI',
    amount: 95000,
    status: 'EXPIRED',
    confirmationCode: 'NEQ345678',
    createdAt: '2024-01-13T16:45:00Z',
    expiresAt: '2024-01-14T04:45:00Z'
  }
]

export async function GET() {
  try {
    // En producción, esto consultaría la base de datos
    // const payments = await paymentService.getAllPayments()
    
    return NextResponse.json(mockPayments, { status: 200 })
  } catch (error) {
    console.error('Error fetching payments:', error)
    return NextResponse.json(
      { error: 'Error interno del servidor' },
      { status: 500 }
    )
  }
}

export async function POST(request: Request) {
  try {
    const paymentData = await request.json()
    
    // Simular creación de pago
    const newPayment: Payment = {
      paymentId: `pay_${Date.now()}`,
      transaccionId: paymentData.transaccionId || `txn_${Date.now()}`,
      paymentMethod: paymentData.paymentMethod || 'NEQUI',
      amount: paymentData.amount || 0,
      status: 'PENDING',
      confirmationCode: `${paymentData.paymentMethod?.substring(0, 3).toUpperCase()}${Math.floor(Math.random() * 1000000)}`,
      createdAt: new Date().toISOString(),
      expiresAt: new Date(Date.now() + 12 * 60 * 60 * 1000).toISOString() // 12 horas
    }
    
    // En producción, esto guardaría en la base de datos
    mockPayments.push(newPayment)
    
    return NextResponse.json(newPayment, { status: 201 })
  } catch (error) {
    console.error('Error creating payment:', error)
    return NextResponse.json(
      { error: 'Error al crear el pago' },
      { status: 500 }
    )
  }
}