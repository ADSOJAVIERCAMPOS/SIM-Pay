export default function HomePage() {
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
      <p style={{ fontSize: '18px', marginBottom: '40px', color: '#666' }}>
        ✅ Trazabilidad Inmutable | 📱 Pagos Nequi/Daviplata | 🔐 Seguridad JWT
      </p>

      <div style={{ display: 'flex', gap: '20px', justifyContent: 'center', flexWrap: 'wrap' }}>
        <a href="/login" style={{
          display: 'inline-block',
          padding: '12px 24px',
          backgroundColor: '#2563eb',
          color: 'white',
          textDecoration: 'none',
          borderRadius: '8px',
          fontWeight: 'bold'
        }}>
          🔐 Iniciar Sesión
        </a>

        <a href="/productos" style={{
          display: 'inline-block',
          padding: '12px 24px',
          backgroundColor: '#ea580c',
          color: 'white',
          textDecoration: 'none',
          borderRadius: '8px',
          fontWeight: 'bold'
        }}>
          📦 Ver Productos
        </a>

        <a href="/payments" style={{
          display: 'inline-block',
          padding: '12px 24px',
          backgroundColor: '#7c3aed',
          color: 'white',
          textDecoration: 'none',
          borderRadius: '8px',
          fontWeight: 'bold'
        }}>
          💳 Pagos Móviles
        </a>
      </div>

      <div style={{ marginTop: '50px', padding: '20px', backgroundColor: '#f3f4f6', borderRadius: '12px' }}>
        <h3 style={{ marginBottom: '20px' }}>Producido por Javier y Angie</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '20px' }}>
          <div>
            <h4>🔐 Trazabilidad</h4>
            <p>Hash SHA-256 inmutable</p>
          </div>
          <div>
            <h4>📱 Pagos Móviles</h4>
            <p>Nequi + Daviplata + WhatsApp</p>
          </div>
        </div>
      </div>

      <footer style={{ marginTop: '50px', padding: '20px', borderTop: '1px solid #e5e7eb' }}>
        <p style={{ color: '#666' }}>
          Colombia 2026
        </p>
      </footer>
    </div>
  )
}