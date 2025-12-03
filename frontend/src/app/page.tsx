'use client'

import { useState } from 'react'

export default function HomePage() {
  const [isFlipping, setIsFlipping] = useState(false)

  const handleNavigate = (path: string) => {
    setIsFlipping(true)
    setTimeout(() => {
      window.location.href = path
    }, 400)
  }

  return (
    <div style={{ padding: '50px', textAlign: 'center', fontFamily: 'Arial', perspective: '1000px' }}>
      {/* Módulo completo con animación 3D */}
      <div style={{
        transform: isFlipping ? 'rotateY(360deg)' : 'rotateY(0deg)',
        transition: 'transform 0.6s ease-in-out',
        transformStyle: 'preserve-3d'
      }}>
        {/* Logo */}
        <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '20px' }}>
          <img
            src="/logoApp.jpeg"
            alt="Optimus Logo"
            style={{ width: '250px', height: '250px', objectFit: 'contain' }}
          />
        </div>

        <h2 style={{ fontSize: '28px', marginBottom: '40px', fontWeight: 'bold', color: '#1f2937' }}>
          NEGOCIOS EN LÍNEA CON PAGOS DIGITALES
        </h2>

        <div style={{ display: 'flex', gap: '20px', justifyContent: 'center', flexWrap: 'wrap' }}>
          {/* Botón Conectarse con planeta */}
          <button onClick={() => handleNavigate('/login')} style={{
            display: 'inline-flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: '10px',
            padding: '18px 36px',
            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            color: 'white',
            textDecoration: 'none',
            borderRadius: '12px',
            fontWeight: 'bold',
            fontSize: '18px',
            border: 'none',
            cursor: 'pointer',
            boxShadow: '0 4px 15px rgba(102, 126, 234, 0.4)',
            transition: 'all 0.3s ease',
            transform: 'scale(1)'
          }}
          onMouseEnter={(e) => e.currentTarget.style.transform = 'scale(1.05)'}
          onMouseLeave={(e) => e.currentTarget.style.transform = 'scale(1)'}
          >
            <span style={{ fontSize: '32px' }}>🌐</span>
            Conectarse
          </button>

          {/* Botón Ver Productos con carrito */}
          <button onClick={() => handleNavigate('/productos')} style={{
            display: 'inline-flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: '10px',
            padding: '18px 36px',
            background: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
            color: 'white',
            textDecoration: 'none',
            borderRadius: '12px',
            fontWeight: 'bold',
            fontSize: '18px',
            border: 'none',
            cursor: 'pointer',
            boxShadow: '0 4px 15px rgba(240, 147, 251, 0.4)',
            transition: 'all 0.3s ease',
            transform: 'scale(1)'
          }}
          onMouseEnter={(e) => e.currentTarget.style.transform = 'scale(1.05)'}
          onMouseLeave={(e) => e.currentTarget.style.transform = 'scale(1)'}
          >
            <span style={{ fontSize: '32px' }}>🛒</span>
            Ver Productos
          </button>

          {/* Botón Pagos Móviles */}
          <button onClick={() => handleNavigate('/payments')} style={{
            display: 'inline-flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: '10px',
            padding: '18px 36px',
            background: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
            color: 'white',
            textDecoration: 'none',
            borderRadius: '12px',
            fontWeight: 'bold',
            fontSize: '18px',
            border: 'none',
            cursor: 'pointer',
            boxShadow: '0 4px 15px rgba(79, 172, 254, 0.4)',
            transition: 'all 0.3s ease',
            transform: 'scale(1)'
          }}
          onMouseEnter={(e) => e.currentTarget.style.transform = 'scale(1.05)'}
          onMouseLeave={(e) => e.currentTarget.style.transform = 'scale(1)'}
          >
            <span style={{ fontSize: '32px' }}>💳</span>
            Pagos Móviles
          </button>
        </div>

        <div style={{ marginTop: '50px', padding: '20px', backgroundColor: '#f3f4f6', borderRadius: '12px' }}>
          <h3 style={{ marginBottom: '20px' }}>JAVIER - ANGIE</h3>
        </div>

        <footer style={{ marginTop: '50px', padding: '20px', borderTop: '1px solid #e5e7eb' }}>
          <p style={{ color: '#666' }}>
            Colombia 2026
          </p>
        </footer>
      </div>
    </div>
  )
}