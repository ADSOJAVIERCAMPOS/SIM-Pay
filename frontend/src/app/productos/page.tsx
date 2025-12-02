'use client'

export default function ProductosPage() {
  // Productos de demostración
  const productos = [
    {
      id: 1,
      nombre: "Laptop HP Pavilion 15",
      descripcion: "Laptop para uso empresarial con Intel i5",
      precio: 2500000,
      stock: 5,
      categoria: "Electrónicos",
      hash: "a7f5d6e8b3c4..."
    },
    {
      id: 2,
      nombre: "Mouse Inalámbrico Logitech",
      descripcion: "Mouse ergonómico con batería recargable",
      precio: 85000,
      stock: 15,
      categoria: "Electrónicos",
      hash: "b8e9f7a6c5d2..."
    },
    {
      id: 3,
      nombre: "Teclado Mecánico RGB",
      descripcion: "Teclado gaming con switches azules",
      precio: 220000,
      stock: 8,
      categoria: "Gaming",
      hash: "c9f1e8b7d6a5..."
    },
    {
      id: 4,
      nombre: "Monitor 24 pulgadas",
      descripcion: "Monitor Full HD para oficina",
      precio: 650000,
      stock: 3,
      categoria: "Electrónicos",
      hash: "d1a2f9c8e7b6..."
    }
  ];

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0,
    }).format(amount);
  };

  return (
    <div style={{ padding: '30px', fontFamily: 'Arial' }}>
      {/* Header */}
      <div style={{ 
        display: 'flex', 
        justifyContent: 'space-between', 
        alignItems: 'center',
        marginBottom: '30px'
      }}>
        <div>
          <h1 style={{ color: '#2563eb', margin: '0 0 10px 0' }}>
            📦 Gestión de Productos
          </h1>
          <p style={{ color: '#666', margin: 0 }}>
            Sistema de inventario con trazabilidad inmutable
          </p>
        </div>
        
        <div style={{ display: 'flex', gap: '10px' }}>
          <a href="/" style={{ 
            padding: '10px 20px', 
            backgroundColor: '#6b7280', 
            color: 'white', 
            textDecoration: 'none',
            borderRadius: '8px',
            fontWeight: 'bold'
          }}>
            ← Inicio
          </a>
          <a href="/productos/nuevo" style={{ 
            padding: '10px 20px', 
            backgroundColor: '#059669', 
            color: 'white', 
            textDecoration: 'none',
            borderRadius: '8px',
            fontWeight: 'bold'
          }}>
            ➕ Nuevo Producto
          </a>
        </div>
      </div>

      {/* Stats */}
      <div style={{ 
        display: 'grid', 
        gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', 
        gap: '20px',
        marginBottom: '30px'
      }}>
        <div style={{ 
          padding: '20px', 
          backgroundColor: '#dbeafe', 
          borderRadius: '12px',
          textAlign: 'center'
        }}>
          <h3 style={{ margin: '0 0 10px 0', color: '#1e40af' }}>Total Productos</h3>
          <div style={{ fontSize: '32px', fontWeight: 'bold', color: '#1e40af' }}>
            {productos.length}
          </div>
        </div>
        
        <div style={{ 
          padding: '20px', 
          backgroundColor: '#dcfce7', 
          borderRadius: '12px',
          textAlign: 'center'
        }}>
          <h3 style={{ margin: '0 0 10px 0', color: '#15803d' }}>Stock Total</h3>
          <div style={{ fontSize: '32px', fontWeight: 'bold', color: '#15803d' }}>
            {productos.reduce((sum, p) => sum + p.stock, 0)}
          </div>
        </div>
        
        <div style={{ 
          padding: '20px', 
          backgroundColor: '#fef3c7', 
          borderRadius: '12px',
          textAlign: 'center'
        }}>
          <h3 style={{ margin: '0 0 10px 0', color: '#92400e' }}>Valor Total</h3>
          <div style={{ fontSize: '20px', fontWeight: 'bold', color: '#92400e' }}>
            {formatCurrency(productos.reduce((sum, p) => sum + (p.precio * p.stock), 0))}
          </div>
        </div>
      </div>

      {/* Lista de productos */}
      <div style={{ 
        backgroundColor: '#f9fafb', 
        border: '1px solid #e5e7eb',
        borderRadius: '12px',
        overflow: 'hidden'
      }}>
        <div style={{ 
          padding: '20px', 
          backgroundColor: '#374151', 
          color: 'white',
          fontWeight: 'bold'
        }}>
          📋 Lista de Productos
        </div>
        
        <div style={{ padding: '0' }}>
          {productos.map((producto, index) => (
            <div key={producto.id} style={{ 
              padding: '20px', 
              borderBottom: index < productos.length - 1 ? '1px solid #e5e7eb' : 'none',
              backgroundColor: index % 2 === 0 ? '#ffffff' : '#f9fafb'
            }}>
              <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr 1fr 1fr auto', gap: '20px', alignItems: 'center' }}>
                <div>
                  <h4 style={{ margin: '0 0 5px 0', color: '#1f2937' }}>
                    {producto.nombre}
                  </h4>
                  <p style={{ margin: '0 0 10px 0', color: '#6b7280', fontSize: '14px' }}>
                    {producto.descripcion}
                  </p>
                  <div style={{ 
                    display: 'inline-block',
                    padding: '4px 8px',
                    backgroundColor: '#e0e7ff',
                    color: '#3730a3',
                    borderRadius: '4px',
                    fontSize: '12px',
                    fontWeight: 'bold'
                  }}>
                    {producto.categoria}
                  </div>
                </div>
                
                <div style={{ textAlign: 'center' }}>
                  <div style={{ fontSize: '18px', fontWeight: 'bold', color: '#059669' }}>
                    {formatCurrency(producto.precio)}
                  </div>
                  <div style={{ fontSize: '12px', color: '#6b7280' }}>Precio</div>
                </div>
                
                <div style={{ textAlign: 'center' }}>
                  <div style={{ 
                    fontSize: '18px', 
                    fontWeight: 'bold', 
                    color: producto.stock > 5 ? '#059669' : '#dc2626'
                  }}>
                    {producto.stock}
                  </div>
                  <div style={{ fontSize: '12px', color: '#6b7280' }}>Stock</div>
                </div>
                
                <div style={{ textAlign: 'center' }}>
                  <div style={{ 
                    fontSize: '12px', 
                    color: '#6b7280',
                    fontFamily: 'monospace',
                    backgroundColor: '#f3f4f6',
                    padding: '4px',
                    borderRadius: '4px'
                  }}>
                    🔐 {producto.hash}
                  </div>
                  <div style={{ fontSize: '10px', color: '#9ca3af' }}>Hash SHA-256</div>
                </div>
                
                <div style={{ display: 'flex', gap: '8px' }}>
                  <button 
                    onClick={() => alert(`✏️ Editando producto: ${producto.nombre}`)}
                    style={{ 
                      padding: '8px 12px', 
                      backgroundColor: '#2563eb', 
                      color: 'white',
                      border: 'none',
                      borderRadius: '6px',
                      fontSize: '12px',
                      cursor: 'pointer'
                    }}
                  >
                    ✏️ Editar
                  </button>
                  <button 
                    onClick={() => {
                      const link = `https://wa.me/573001234567?text=🛒 *SIM-Pay* - Producto disponible:%0A%0A📦 ${producto.nombre}%0A💰 ${formatCurrency(producto.precio)}%0A📊 Stock: ${producto.stock}%0A%0A¿Te interesa?`;
                      alert(`📱 Link de WhatsApp generado para: ${producto.nombre}`);
                    }}
                    style={{ 
                      padding: '8px 12px', 
                      backgroundColor: '#25D366', 
                      color: 'white',
                      border: 'none',
                      borderRadius: '6px',
                      fontSize: '12px',
                      cursor: 'pointer'
                    }}
                  >
                    📱 Vender
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
      
      <div style={{ marginTop: '30px', textAlign: 'center' }}>
        <div style={{ 
          padding: '20px', 
          backgroundColor: '#ecfdf5', 
          borderRadius: '12px',
          border: '1px solid #86efac'
        }}>
          <h4 style={{ margin: '0 0 10px 0', color: '#15803d' }}>
            🔐 Trazabilidad Garantizada
          </h4>
          <p style={{ margin: 0, color: '#166534' }}>
            Cada producto tiene un hash SHA-256 único que garantiza la integridad 
            y trazabilidad completa en el sistema SIM-Pay.
          </p>
        </div>
      </div>
    </div>
  );
}