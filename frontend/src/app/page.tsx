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

        <h2 style={{ fontSize: '24px', marginBottom: '30px' }}>
          NEGOCIO ONLINE Y CON PAGOS MÓVILES
        </h2>

        <div style={{ display: 'flex', gap: '20px', justifyContent: 'center', flexWrap: 'wrap' }}>
          <button onClick={() => handleNavigate('/login')} style={{
            display: 'inline-flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: '8px',
            padding: '12px 24px',
            backgroundColor: '#16a34a',
            color: 'white',
            textDecoration: 'none',
            borderRadius: '8px',
            fontWeight: 'bold',
            border: 'none',
            cursor: 'pointer'
          }}>
            <span style={{ fontSize: '24px' }}>🌐</span> Conectarse
          </button>

          <button onClick={() => handleNavigate('/productos')} style={{
            display: 'inline-block',
            padding: '12px 24px',
            backgroundColor: '#15803d',
            color: 'white',
            textDecoration: 'none',
            borderRadius: '8px',
            fontWeight: 'bold',
            border: 'none',
            cursor: 'pointer'
          }}>
            📦 Ver Productos
          </button>

          <button onClick={() => handleNavigate('/payments')} style={{
            display: 'inline-block',
            padding: '12px 24px',
            backgroundColor: '#059669',
            color: 'white',
            textDecoration: 'none',
            borderRadius: '8px',
            fontWeight: 'bold',
            border: 'none',
            cursor: 'pointer'
          }}>
            💳 Pagos Móviles
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