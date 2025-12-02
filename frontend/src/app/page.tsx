export default function HomePage() {
  return (
    <div style={{ padding: '50px', textAlign: 'center', fontFamily: 'Arial' }}>
      <h1 style={{ color: '#2563eb', fontSize: '48px', marginBottom: '20px' }}>
        🚀 SIM-Pay
      </h1>
      <h2 style={{ fontSize: '24px', marginBottom: '30px' }}>
        Sistema de Inventario Modular con Pagos Móviles
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
        
        <a href="/register" style={{ 
          display: 'inline-block', 
          padding: '12px 24px', 
          backgroundColor: '#059669', 
          color: 'white', 
          textDecoration: 'none', 
          borderRadius: '8px',
          fontWeight: 'bold'
        }}>
          📝 Registrarse
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
        <h3 style={{ marginBottom: '20px' }}>🎓 Proyecto de Tesis Doctoral</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '20px' }}>
          <div>
            <h4>🔐 Trazabilidad</h4>
            <p>Hash SHA-256 inmutable</p>
          </div>
          <div>
            <h4>📱 Pagos Móviles</h4>
            <p>Nequi + Daviplata + WhatsApp</p>
          </div>
          <div>
            <h4>🏗️ Arquitectura</h4>
            <p>Spring Boot + Next.js</p>
          </div>
          <div>
            <h4>🌐 Deploy</h4>
            <p>Railway + Vercel</p>
          </div>
        </div>
      </div>
      
      <footer style={{ marginTop: '50px', padding: '20px', borderTop: '1px solid #e5e7eb' }}>
        <p style={{ color: '#666' }}>
          🇨🇴 Hecho con ❤️ en Colombia | SENA - Tesis Doctoral 2025
        </p>
      </footer>
    </div>
  )
}