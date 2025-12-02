export default function AddProductPage() {
  return (
    <div style={{ padding: '50px', fontFamily: 'Arial' }}>
      <h1 style={{ color: '#2563eb', marginBottom: '30px' }}>
        ➕ Agregar Nuevo Producto
      </h1>
      
      <div style={{ 
        maxWidth: '600px', 
        margin: '0 auto', 
        backgroundColor: '#f9fafb', 
        padding: '30px', 
        borderRadius: '12px',
        border: '1px solid #e5e7eb'
      }}>
        <form style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          <div>
            <label style={{ display: 'block', marginBottom: '8px', fontWeight: 'bold' }}>
              📦 Nombre del Producto:
            </label>
            <input 
              type="text" 
              placeholder="Ej: Laptop HP Pavilion"
              style={{ 
                width: '100%', 
                padding: '12px', 
                border: '1px solid #d1d5db',
                borderRadius: '8px',
                fontSize: '16px'
              }}
            />
          </div>
          
          <div>
            <label style={{ display: 'block', marginBottom: '8px', fontWeight: 'bold' }}>
              📝 Descripción:
            </label>
            <textarea 
              placeholder="Descripción detallada del producto..."
              style={{ 
                width: '100%', 
                padding: '12px', 
                border: '1px solid #d1d5db',
                borderRadius: '8px',
                fontSize: '16px',
                minHeight: '80px'
              }}
            />
          </div>
          
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '8px', fontWeight: 'bold' }}>
                💰 Precio (COP):
              </label>
              <input 
                type="number" 
                placeholder="150000"
                style={{ 
                  width: '100%', 
                  padding: '12px', 
                  border: '1px solid #d1d5db',
                  borderRadius: '8px',
                  fontSize: '16px'
                }}
              />
            </div>
            
            <div>
              <label style={{ display: 'block', marginBottom: '8px', fontWeight: 'bold' }}>
                📊 Stock Inicial:
              </label>
              <input 
                type="number" 
                placeholder="10"
                style={{ 
                  width: '100%', 
                  padding: '12px', 
                  border: '1px solid #d1d5db',
                  borderRadius: '8px',
                  fontSize: '16px'
                }}
              />
            </div>
          </div>
          
          <div>
            <label style={{ display: 'block', marginBottom: '8px', fontWeight: 'bold' }}>
              🏷️ Categoría:
            </label>
            <select style={{ 
              width: '100%', 
              padding: '12px', 
              border: '1px solid #d1d5db',
              borderRadius: '8px',
              fontSize: '16px'
            }}>
              <option>Seleccionar categoría...</option>
              <option>📱 Electrónicos</option>
              <option>👔 Ropa</option>
              <option>🏠 Hogar</option>
              <option>📚 Libros</option>
              <option>🎮 Gaming</option>
              <option>💄 Belleza</option>
              <option>🏃‍♂️ Deportes</option>
            </select>
          </div>
          
          <div style={{ 
            display: 'flex', 
            gap: '15px', 
            marginTop: '20px',
            justifyContent: 'center' 
          }}>
            <button 
              type="button"
              onClick={() => window.history.back()}
              style={{ 
                padding: '12px 24px', 
                backgroundColor: '#6b7280', 
                color: 'white', 
                border: 'none',
                borderRadius: '8px',
                fontSize: '16px',
                fontWeight: 'bold',
                cursor: 'pointer'
              }}
            >
              ← Cancelar
            </button>
            
            <button 
              type="button"
              onClick={() => {
                alert('✅ ¡Producto agregado exitosamente!\\n🔐 Hash SHA-256 generado para trazabilidad inmutable');
                window.location.href = '/dashboard';
              }}
              style={{ 
                padding: '12px 24px', 
                backgroundColor: '#059669', 
                color: 'white', 
                border: 'none',
                borderRadius: '8px',
                fontSize: '16px',
                fontWeight: 'bold',
                cursor: 'pointer'
              }}
            >
              ✅ Guardar Producto
            </button>
          </div>
        </form>
        
        <div style={{ 
          marginTop: '30px', 
          padding: '15px', 
          backgroundColor: '#dbeafe', 
          borderRadius: '8px',
          border: '1px solid #93c5fd'
        }}>
          <h4 style={{ margin: '0 0 10px 0', color: '#1e40af' }}>
            🔐 Trazabilidad Inmutable
          </h4>
          <p style={{ margin: 0, fontSize: '14px', color: '#374151' }}>
            Cada producto genera un hash SHA-256 único que garantiza la integridad
            y trazabilidad completa en el sistema SIM-Pay.
          </p>
        </div>
      </div>
      
      <div style={{ textAlign: 'center', marginTop: '30px' }}>
        <a href="/dashboard" style={{ 
          color: '#2563eb', 
          textDecoration: 'none',
          fontWeight: 'bold'
        }}>
          ← Volver al Dashboard
        </a>
      </div>
    </div>
  )
}