'use client'

import { useState } from 'react'

export default function HomePage() {
  const [flipStates, setFlipStates] = useState({ login: false, productos: false, pagos: false })

  const handleFlip = (button: 'login' | 'productos' | 'pagos') => {
    setFlipStates(prev => ({ ...prev, [button]: true }))
    setTimeout(() => {
      if (button === 'login') window.location.href = '/login'
      else if (button === 'productos') window.location.href = '/productos'
      else window.location.href = '/payments'
    }, 300)
  }

  return (
    <div style={{ padding: '50px', textAlign: 'center', fontFamily: 'Arial' }}>
      {/* Logo */}
      <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '20px' }}>
        <img
          src="/logoApp.jpeg"
          alt="Optimus Logo"
          style={{ width: '150px', height: '150px', objectFit: 'contain' }}
        />
      </div>


      <h2 style={{ fontSize: '24px', marginBottom: '30px' }}>
        NEGOCIO ONLINE Y CON PAGOS MÓVILES
      </h2>

      <div style={{ display: 'flex', gap: '20px', justifyContent: 'center', flexWrap: 'wrap' }}>
        <button onClick={() => handleFlip('login')} style={{
          display: 'inline-block',
          padding: '12px 24px',
          backgroundColor: '#16a34a',
          color: 'white',
          textDecoration: 'none',
          borderRadius: '8px',
          fontWeight: 'bold',
          border: 'none',
          cursor: 'pointer',
          transform: flipStates.login ? 'rotateY(180deg)' : 'rotateY(0deg)',
          transition: 'transform 0.6s',
          transformStyle: 'preserve-3d'
        }}>
          🔐 Conectarse
        </button>

        <button onClick={() => handleFlip('productos')} style={{
          display: 'inline-block',
          padding: '12px 24px',
          backgroundColor: '#15803d',
          color: 'white',
          textDecoration: 'none',
          borderRadius: '8px',
          fontWeight: 'bold',
          border: 'none',
          cursor: 'pointer',
          transform: flipStates.productos ? 'rotateY(180deg)' : 'rotateY(0deg)',
          transition: 'transform 0.6s',
          transformStyle: 'preserve-3d'
        }}>
          📦 Ver Productos
        </button>

        <button onClick={() => handleFlip('pagos')} style={{
          display: 'inline-block',
          padding: '12px 24px',
          backgroundColor: '#059669',
          color: 'white',
          textDecoration: 'none',
          borderRadius: '8px',
          fontWeight: 'bold',
          border: 'none',
          cursor: 'pointer',
          transform: flipStates.pagos ? 'rotateY(180deg)' : 'rotateY(0deg)',
          transition: 'transform 0.6s',
          transformStyle: 'preserve-3d'
        }}>
          💳 Pagos Móviles
        </button>
      </div>

      <div style={{ marginTop: '50px', padding: '20px', backgroundColor: '#f3f4f6', borderRadius: '12px' }}>
        <h3 style={{ marginBottom: '20px' }}>Producido por Javier y Angie</h3>
      </div>

      <footer style={{ marginTop: '50px', padding: '20px', borderTop: '1px solid #e5e7eb' }}>
        <p style={{ color: '#666' }}>
          Colombia 2026
        </p>
      </footer>
    </div>
  )
}