const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api'

// Configuración base para las peticiones
const apiRequest = async (endpoint: string, options: RequestInit = {}) => {
  const url = `${API_BASE_URL}${endpoint}`
  
  const config: RequestInit = {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  }

  // Agregar token de autorización si está disponible
  if (typeof window !== 'undefined') {
    const token = document.cookie
      .split('; ')
      .find(row => row.startsWith('token='))
      ?.split('=')[1]

    if (token && config.headers) {
      (config.headers as any)['Authorization'] = `Bearer ${token}`
    }
  }

  try {
    const response = await fetch(url, config)
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    
    return await response.json()
  } catch (error) {
    console.error(`Error en ${endpoint}:`, error)
    throw error
  }
}

// Servicios de API
export const authApi = {
  login: async (credentials: { email: string; password: string }) => {
    return apiRequest('/auth/login', {
      method: 'POST',
      body: JSON.stringify(credentials),
    })
  },
  
  logout: async () => {
    return apiRequest('/auth/logout', { method: 'POST' })
  },
}

export const productosApi = {
  getAll: async (params: {
    page?: number
    size?: number
    sortBy?: string
    sortDir?: string
  } = {}) => {
    const query = new URLSearchParams({
      page: (params.page || 0).toString(),
      size: (params.size || 10).toString(),
      sortBy: params.sortBy || 'nombre',
      sortDir: params.sortDir || 'asc',
    })
    
    return apiRequest(`/productos?${query}`)
  },

  getById: async (id: string) => {
    return apiRequest(`/productos/${id}`)
  },

  buscar: async (query: string, page: number = 0, size: number = 10) => {
    const params = new URLSearchParams({
      q: query,
      page: page.toString(),
      size: size.toString(),
    })
    
    return apiRequest(`/productos/buscar?${params}`)
  },

  getCategorias: async () => {
    return apiRequest('/productos/categorias')
  },

  create: async (producto: any) => {
    return apiRequest('/productos', {
      method: 'POST',
      body: JSON.stringify(producto),
    })
  },

  update: async (id: string, producto: any) => {
    return apiRequest(`/productos/${id}`, {
      method: 'PUT',
      body: JSON.stringify(producto),
    })
  },

  delete: async (id: string) => {
    return apiRequest(`/productos/${id}`, { method: 'DELETE' })
  },
}

export const ventasApi = {
  procesar: async (venta: {
    productoId: string
    cantidad: number
    precioUnitario: number
    metodoPago?: string
    numeroCliente?: string
  }) => {
    return apiRequest('/ventas', {
      method: 'POST',
      body: JSON.stringify(venta),
    })
  },

  confirmarPago: async (transaccionId: string, referenciaPago: string) => {
    return apiRequest(`/ventas/${transaccionId}/confirmar-pago`, {
      method: 'POST',
      body: JSON.stringify({ referenciaPago }),
    })
  },

  cancelar: async (transaccionId: string) => {
    return apiRequest(`/ventas/${transaccionId}/cancelar`, {
      method: 'POST',
    })
  },

  generarWhatsApp: async (numero: string, mensaje: string) => {
    const params = new URLSearchParams({ numero, mensaje })
    return apiRequest(`/ventas/generar-whatsapp?${params}`)
  },
}

export const trazabilidadApi = {
  getTransacciones: async (page: number = 0, size: number = 20) => {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
    })
    
    return apiRequest(`/trazabilidad/transacciones?${params}`)
  },

  getTrazabilidadProducto: async (productoId: string) => {
    return apiRequest(`/trazabilidad/producto/${productoId}`)
  },

  getTransaccionesUsuario: async (usuarioId: string, page: number = 0, size: number = 20) => {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
    })
    
    return apiRequest(`/trazabilidad/usuario/${usuarioId}?${params}`)
  },

  verificarIntegridad: async (productoId: string) => {
    return apiRequest(`/trazabilidad/verificar-integridad/${productoId}`)
  },

  getHashChain: async (productoId: string) => {
    return apiRequest(`/trazabilidad/auditoria/${productoId}/hash-chain`)
  },

  getReporteVentas: async (fechaInicio: string, fechaFin: string) => {
    const params = new URLSearchParams({
      fechaInicio,
      fechaFin,
    })
    
    return apiRequest(`/trazabilidad/reporte-ventas?${params}`)
  },
}

export default {
  auth: authApi,
  productos: productosApi,
  ventas: ventasApi,
  trazabilidad: trazabilidadApi,
}