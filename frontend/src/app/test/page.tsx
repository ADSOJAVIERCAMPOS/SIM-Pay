export default function TestPage() {
  return (
    <div style={{ padding: '20px', textAlign: 'center' }}>
      <h1>🚀 SIM-Pay está funcionando!</h1>
      <p>Si ves este mensaje, el frontend está corriendo correctamente.</p>
      <div style={{ margin: '20px 0' }}>
        <a href="/login" style={{ color: 'blue', textDecoration: 'underline' }}>
          Ir al Login
        </a>
        {' | '}
        <a href="/dashboard" style={{ color: 'blue', textDecoration: 'underline' }}>
          Ir al Dashboard
        </a>
        {' | '}
        <a href="/payments" style={{ color: 'blue', textDecoration: 'underline' }}>
          Pagos Móviles
        </a>
      </div>
      <p>✅ Frontend: OK</p>
      <p>⏳ Backend: Iniciando...</p>
    </div>
  )
}