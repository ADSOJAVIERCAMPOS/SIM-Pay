'use client'

import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import { useAuth } from '@/lib/auth-context'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import {
  Package,
  ShoppingCart,
  DollarSign,
  TrendingUp,
  Search,
  Plus,
  LogOut,
  Shield,
  Eye,
  Smartphone,
  Hash,
  ExternalLink
} from 'lucide-react'
import { formatCurrency, formatDate } from '@/lib/utils'
import { productosApi, ventasApi, trazabilidadApi } from '@/services/api'
import { toast } from 'sonner'

interface Producto {
  id: string
  nombre: string
  descripcion: string
  precio: number
  stock: number
  categoria: string
  hashStockLastCommit: string
  createdAt: string
}

interface Transaccion {
  id: string
  tipo: string
  cantidad: number
  total: number
  estado: string
  hashTransaccion: string
  createdAt: string
}

export default function DashboardPage() {
  const { user, logout } = useAuth()
  const router = useRouter()
  const [activeTab, setActiveTab] = useState('productos')

  // Estados
  const [productos, setProductos] = useState<Producto[]>([])
  const [transacciones, setTransacciones] = useState<Transaccion[]>([])
  const [searchTerm, setSearchTerm] = useState('')
  const [isLoading, setIsLoading] = useState(false)

  // Estados para venta
  const [ventaProducto, setVentaProducto] = useState<Producto | null>(null)
  const [ventaCantidad, setVentaCantidad] = useState(1)
  const [ventaTelefono, setVentaTelefono] = useState('')
  const [ventaMetodo, setVentaMetodo] = useState('NEQUI')

  useEffect(() => {
    if (!user) {
      router.push('/login')
      return
    }
    loadData()
  }, [user, router])

  const loadData = async () => {
    setIsLoading(true)
    try {
      const [productosResponse, transaccionesResponse] = await Promise.all([
        productosApi.getAll({ size: 20 }),
        trazabilidadApi.getTransacciones(0, 10)
      ])

      setProductos(productosResponse.content || productosResponse)
      setTransacciones(transaccionesResponse.content || transaccionesResponse)
    } catch (error) {
      console.error('Error cargando datos:', error)
      toast.error('Error cargando datos del sistema')
    } finally {
      setIsLoading(false)
    }
  }

  const handleVenta = async (producto: Producto) => {
    setVentaProducto(producto)
  }

  const procesarVenta = async () => {
    if (!ventaProducto) return

    if (ventaCantidad > ventaProducto.stock) {
      toast.error('Stock insuficiente')
      return
    }

    setIsLoading(true)
    try {
      const response = await ventasApi.procesar({
        productoId: ventaProducto.id,
        cantidad: ventaCantidad,
        precioUnitario: ventaProducto.precio,
        metodoPago: ventaMetodo,
        numeroCliente: ventaTelefono
      })

      toast.success('¡Venta procesada exitosamente!')

      // Mostrar información de pago
      const mensajePago = `
🛒 *SIM-Pay - Compra Procesada*

💰 Total: ${formatCurrency(response.transaccion.total)}
🔢 Ref: ${response.referencia}

💳 Opciones de pago:
${ventaMetodo === 'NEQUI' ? '🟢 Nequi: ' + response.nequiLink : '🔵 Daviplata: ' + response.daviplataLink}

${response.whatsAppLink ? '📱 Enviado por WhatsApp' : ''}
      `

      // Abrir WhatsApp si hay número
      if (response.whatsAppLink) {
        window.open(response.whatsAppLink, '_blank')
      }

      // Limpiar formulario
      setVentaProducto(null)
      setVentaCantidad(1)
      setVentaTelefono('')

      // Recargar datos
      loadData()

    } catch (error) {
      console.error('Error procesando venta:', error)
      toast.error('Error procesando la venta')
    } finally {
      setIsLoading(false)
    }
  }

  const filteredProductos = productos.filter(producto =>
    producto.nombre.toLowerCase().includes(searchTerm.toLowerCase()) ||
    producto.categoria.toLowerCase().includes(searchTerm.toLowerCase())
  )

  const getStockBadgeVariant = (stock: number) => {
    if (stock <= 5) return 'danger'
    if (stock <= 15) return 'warning'
    return 'success'
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center space-x-4">
              <Shield className="h-8 w-8 text-primary" />
              <div>
                <h1 className="text-2xl font-bold text-gray-900">SIM-Pay Dashboard</h1>
                <p className="text-sm text-gray-500">Sistema de Inventario Modular</p>
              </div>
            </div>

            <div className="flex items-center space-x-4">
              <div className="text-right">
                <p className="text-sm font-medium text-gray-900">{user?.nombre}</p>
                <p className="text-xs text-gray-500">{user?.rol}</p>
              </div>
              <Button
                variant="outline"
                onClick={() => router.push('/payments')}
                className="bg-gradient-to-r from-purple-500 to-blue-500 text-white border-none hover:from-purple-600 hover:to-blue-600"
              >
                <Smartphone className="h-4 w-4 mr-2" />
                Pagos Móviles
              </Button>
              <Button variant="outline" onClick={logout}>
                <LogOut className="h-4 w-4 mr-2" />
                Salir
              </Button>
            </div>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        {/* Estadísticas */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Total Productos</CardTitle>
              <Package className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{productos.length}</div>
              <p className="text-xs text-muted-foreground">productos activos</p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Stock Total</CardTitle>
              <TrendingUp className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">
                {productos.reduce((sum, p) => sum + p.stock, 0)}
              </div>
              <p className="text-xs text-muted-foreground">unidades en inventario</p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Transacciones</CardTitle>
              <ShoppingCart className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{transacciones.length}</div>
              <p className="text-xs text-muted-foreground">operaciones registradas</p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Trazabilidad</CardTitle>
              <Hash className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-green-600">100%</div>
              <p className="text-xs text-muted-foreground">integridad verificada</p>
            </CardContent>
          </Card>
        </div>

        {/* Navegación */}
        <div className="border-b border-gray-200 mb-6">
          <nav className="flex space-x-8">
            {['productos', 'ventas', 'pagos', 'trazabilidad'].map((tab) => (
              <button
                key={tab}
                onClick={() => tab === 'pagos' ? router.push('/payments') : setActiveTab(tab)}
                className={`py-2 px-1 border-b-2 font-medium text-sm capitalize ${activeTab === tab
                    ? 'border-primary text-primary'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  } flex items-center gap-1`}
              >
                {tab === 'pagos' && <Smartphone className="h-3 w-3" />}
                {tab}
              </button>
            ))}
          </nav>
        </div>

        {/* Contenido por pestaña */}
        {activeTab === 'productos' && (
          <div className="space-y-6">
            {/* Búsqueda */}
            <div className="flex flex-col sm:flex-row gap-4 items-center justify-between">
              <div className="relative flex-1 max-w-md">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
                <Input
                  placeholder="Buscar productos..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>

            {/* Grid de productos */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {filteredProductos.map((producto) => (
                <Card key={producto.id} className="product-card">
                  <CardContent className="p-4">
                    <div className="flex justify-between items-start mb-2">
                      <h3 className="font-semibold text-lg truncate">{producto.nombre}</h3>
                      <Badge variant={getStockBadgeVariant(producto.stock)}>
                        {producto.stock}
                      </Badge>
                    </div>

                    <p className="text-sm text-gray-600 mb-2 line-clamp-2">
                      {producto.descripcion}
                    </p>

                    <div className="space-y-2">
                      <div className="flex justify-between items-center">
                        <span className="text-sm text-gray-500">Precio:</span>
                        <span className="font-bold text-primary">
                          {formatCurrency(producto.precio)}
                        </span>
                      </div>

                      <div className="flex justify-between items-center">
                        <span className="text-sm text-gray-500">Categoría:</span>
                        <Badge variant="outline">{producto.categoria}</Badge>
                      </div>

                      <div className="pt-2">
                        <Button
                          onClick={() => handleVenta(producto)}
                          className="w-full"
                          disabled={producto.stock === 0}
                        >
                          <ShoppingCart className="h-4 w-4 mr-2" />
                          Vender
                        </Button>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          </div>
        )}

        {activeTab === 'trazabilidad' && (
          <div className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center">
                  <Hash className="h-5 w-5 mr-2" />
                  Registro de Transacciones
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {transacciones.map((transaccion) => (
                    <div key={transaccion.id} className="transaction-item">
                      <div className="flex-1">
                        <div className="flex items-center space-x-2 mb-1">
                          <Badge variant={transaccion.tipo === 'VENTA' ? 'success' : 'secondary'}>
                            {transaccion.tipo}
                          </Badge>
                          <span className="font-medium">{formatCurrency(transaccion.total)}</span>
                          <Badge variant="outline">{transaccion.estado}</Badge>
                        </div>
                        <div className="hash-display">
                          Hash: {transaccion.hashTransaccion.substring(0, 16)}...
                        </div>
                        <p className="text-xs text-gray-500 mt-1">
                          {formatDate(transaccion.createdAt)}
                        </p>
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </div>
        )}
      </div>

      {/* Modal de Venta */}
      {ventaProducto && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <Card className="w-full max-w-md">
            <CardHeader>
              <CardTitle className="flex items-center">
                <Smartphone className="h-5 w-5 mr-2" />
                Procesar Venta
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div>
                <h3 className="font-semibold">{ventaProducto.nombre}</h3>
                <p className="text-sm text-gray-600">Stock disponible: {ventaProducto.stock}</p>
                <p className="text-lg font-bold text-primary">
                  {formatCurrency(ventaProducto.precio)}
                </p>
              </div>

              <div className="space-y-2">
                <label className="text-sm font-medium">Cantidad</label>
                <Input
                  type="number"
                  min="1"
                  max={ventaProducto.stock}
                  value={ventaCantidad}
                  onChange={(e) => setVentaCantidad(parseInt(e.target.value) || 1)}
                />
              </div>

              <div className="space-y-2">
                <label className="text-sm font-medium">Teléfono Cliente (WhatsApp)</label>
                <Input
                  placeholder="3001234567"
                  value={ventaTelefono}
                  onChange={(e) => setVentaTelefono(e.target.value)}
                />
              </div>

              <div className="space-y-2">
                <label className="text-sm font-medium">Método de Pago</label>
                <div className="flex space-x-2">
                  <Button
                    variant={ventaMetodo === 'NEQUI' ? 'default' : 'outline'}
                    onClick={() => setVentaMetodo('NEQUI')}
                    className="flex-1"
                  >
                    Nequi
                  </Button>
                  <Button
                    variant={ventaMetodo === 'DAVIPLATA' ? 'default' : 'outline'}
                    onClick={() => setVentaMetodo('DAVIPLATA')}
                    className="flex-1"
                  >
                    Daviplata
                  </Button>
                </div>
              </div>

              <div className="bg-blue-50 p-3 rounded-lg">
                <p className="text-sm font-medium text-blue-800">Total a cobrar:</p>
                <p className="text-xl font-bold text-blue-900">
                  {formatCurrency(ventaProducto.precio * ventaCantidad)}
                </p>
              </div>

              <div className="flex space-x-2">
                <Button
                  variant="outline"
                  onClick={() => setVentaProducto(null)}
                  className="flex-1"
                >
                  Cancelar
                </Button>
                <Button
                  onClick={procesarVenta}
                  disabled={isLoading}
                  className="flex-1"
                >
                  {isLoading ? 'Procesando...' : 'Procesar Venta'}
                </Button>
              </div>
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  )
}