'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { useAuth } from '@/lib/auth-context'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Eye, EyeOff, Mail, Lock, User, Key, AtSign } from 'lucide-react'
import { toast } from 'sonner'

export default function LoginPage() {
  const [isSignUp, setIsSignUp] = useState(false)
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [nombre, setNombre] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [showConfirmPassword, setShowConfirmPassword] = useState(false)
  const [rememberMe, setRememberMe] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  const [show2FAModal, setShow2FAModal] = useState(false)
  const [verificationCode, setVerificationCode] = useState('')
  const [pendingProvider, setPendingProvider] = useState<'google' | 'facebook' | null>(null)
  const [menuRotation, setMenuRotation] = useState(0)
  const { login } = useAuth()
  const router = useRouter()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!email || !password) {
      toast.error('Por favor complete todos los campos')
      return
    }

    if (isSignUp) {
      if (!nombre) {
        toast.error('Por favor ingrese su nombre')
        return
      }
      if (password !== confirmPassword) {
        toast.error('Las contraseñas no coinciden')
        return
      }
      if (password.length < 6) {
        toast.error('La contraseña debe tener al menos 6 caracteres')
        return
      }
    }

    setIsLoading(true)

    try {
      const endpoint = isSignUp ? '/auth/register' : '/auth/login'
      const body = isSignUp
        ? { email, password, nombre, rol: 'VENDEDOR' }
        : { email, password }

      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}${endpoint}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
      })

      const data = await response.json()

      if (response.ok) {
        if (isSignUp) {
          toast.success('¡Cuenta creada exitosamente! Iniciando sesión...')
          setTimeout(() => {
            login(data.token, {
              id: data.id,
              email: data.email,
              nombre: data.nombre,
              rol: data.rol,
            })
            router.push('/dashboard')
          }, 1000)
        } else {
          login(data.token, {
            id: data.id,
            email: data.email,
            nombre: data.nombre,
            rol: data.rol,
          })
          toast.success('¡Bienvenido a SIM-Pay!')
          router.push('/dashboard')
        }
      } else {
        toast.error(data.message || `Error al ${isSignUp ? 'registrarse' : 'iniciar sesión'}`)
      }
    } catch (error) {
      console.error('Error:', error)
      toast.error('Error de conexión. Intente nuevamente.')
    } finally {
      setIsLoading(false)
    }
  }

  const handleGoogleLogin = async () => {
    try {
      // Detectar información del dispositivo
      const deviceInfo = {
        userAgent: navigator.userAgent,
        platform: navigator.platform,
        language: navigator.language,
        timestamp: new Date().toISOString()
      }

      // Enviar notificación al backend para verificar si es nuevo dispositivo
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/check-device`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ provider: 'google', deviceInfo })
      })

      const data = await response.json()

      if (data.isNewDevice || data.requires2FA) {
        // Mostrar modal de verificación 2FA
        setPendingProvider('google')
        setShow2FAModal(true)
        toast.info('Se ha enviado un código de verificación a tu correo/teléfono')
      } else {
        // Proceder con OAuth normal
        window.location.href = 'https://accounts.google.com/o/oauth2/v2/auth?client_id=YOUR_CLIENT_ID&redirect_uri=' + encodeURIComponent(window.location.origin + '/auth/google/callback') + '&response_type=code&scope=email%20profile'
      }
    } catch (error) {
      console.error('Error en Google login:', error)
      toast.error('Error al iniciar sesión con Google')
    }
  }

  const handleFacebookLogin = async () => {
    try {
      // Detectar información del dispositivo
      const deviceInfo = {
        userAgent: navigator.userAgent,
        platform: navigator.platform,
        language: navigator.language,
        timestamp: new Date().toISOString()
      }

      // Enviar notificación al backend para verificar si es nuevo dispositivo
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/check-device`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ provider: 'facebook', deviceInfo })
      })

      const data = await response.json()

      if (data.isNewDevice || data.requires2FA) {
        // Mostrar modal de verificación 2FA
        setPendingProvider('facebook')
        setShow2FAModal(true)
        toast.info('Se ha enviado un código de verificación a tu correo/teléfono')
      } else {
        // Proceder con OAuth normal
        window.location.href = 'https://www.facebook.com/v12.0/dialog/oauth?client_id=YOUR_APP_ID&redirect_uri=' + encodeURIComponent(window.location.origin + '/auth/facebook/callback') + '&scope=email,public_profile'
      }
    } catch (error) {
      console.error('Error en Facebook login:', error)
      toast.error('Error al iniciar sesión con Facebook')
    }
  }

  const handleForgotPassword = () => {
    toast.info('Recuperación de contraseña estará disponible próximamente')
  }

  const handleVerify2FA = async () => {
    if (!verificationCode || verificationCode.length !== 6) {
      toast.error('Ingrese un código de 6 dígitos válido')
      return
    }

    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/verify-2fa`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ code: verificationCode, provider: pendingProvider })
      })

      const data = await response.json()

      if (response.ok && data.verified) {
        toast.success('Código verificado correctamente')
        setShow2FAModal(false)
        
        // Continuar con OAuth según el proveedor
        if (pendingProvider === 'google') {
          window.location.href = 'https://accounts.google.com/o/oauth2/v2/auth?client_id=YOUR_CLIENT_ID&redirect_uri=' + encodeURIComponent(window.location.origin + '/auth/google/callback') + '&response_type=code&scope=email%20profile'
        } else if (pendingProvider === 'facebook') {
          window.location.href = 'https://www.facebook.com/v12.0/dialog/oauth?client_id=YOUR_APP_ID&redirect_uri=' + encodeURIComponent(window.location.origin + '/auth/facebook/callback') + '&scope=email,public_profile'
        }
      } else {
        toast.error('Código inválido. Intente nuevamente.')
      }
    } catch (error) {
      console.error('Error verificando 2FA:', error)
      toast.error('Error en la verificación')
    }
  }

  return (
    <div className="min-h-screen w-full flex items-center justify-center bg-gradient-to-br from-emerald-50 via-white to-green-50 px-4 py-8">
      <div className="w-full max-w-md mx-auto">
        {/* Login/Signup Card - RECUADRO VISIBLE CON BORDE */}
        <Card className="w-full shadow-2xl border-4 border-green-500 backdrop-blur-sm bg-white/95 p-2">
          <CardHeader className="space-y-1 pb-2 pt-6">
          </CardHeader>
          <CardContent className="space-y-4">
            <form onSubmit={handleSubmit} className="space-y-4">
              {/* Nombre (solo en registro) */}
              {isSignUp && (
                <div className="space-y-2">
                  <label htmlFor="nombre" className="text-sm font-medium text-gray-700 flex items-center gap-2">
                    <User className="h-4 w-4 text-green-600" />
                    Nombre Completo
                  </label>
                  <Input
                    id="nombre"
                    type="text"
                    placeholder="Juan Pérez"
                    value={nombre}
                    onChange={(e) => setNombre(e.target.value)}
                    className="w-full border-gray-300 focus:border-green-500 focus:ring-green-500"
                  />
                </div>
              )}

              {/* Email */}
              <div className="space-y-2">
                <label htmlFor="email" className="text-sm font-medium text-gray-700 flex items-center gap-2">
                  <AtSign className="h-4 w-4 text-green-600" />
                  Correo Electrónico
                </label>
                <Input
                  id="email"
                  type="email"
                  placeholder="tu@email.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full border-gray-300 focus:border-green-500 focus:ring-green-500"
                />
              </div>

              {/* Password */}
              <div className="space-y-2">
                <label htmlFor="password" className="text-sm font-medium text-gray-700 flex items-center gap-2">
                  <Key className="h-4 w-4 text-green-600" />
                  Contraseña
                </label>
                <div className="relative">
                  <Input
                    id="password"
                    type={showPassword ? "text" : "password"}
                    placeholder="••••••••"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="w-full pr-10 border-gray-300 focus:border-green-500 focus:ring-green-500"
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute inset-y-0 right-0 flex items-center pr-3 hover:text-green-600 transition-colors"
                  >
                    {showPassword ? (
                      <EyeOff className="h-4 w-4 text-gray-400" />
                    ) : (
                      <Eye className="h-4 w-4 text-gray-400" />
                    )}
                  </button>
                </div>
              </div>

              {/* Confirm Password (solo en registro) */}
              {isSignUp && (
                <div className="space-y-2">
                  <label htmlFor="confirmPassword" className="text-sm font-medium text-gray-700 flex items-center gap-2">
                    <Key className="h-4 w-4 text-green-600" />
                    Confirmar Contraseña
                  </label>
                  <div className="relative">
                    <Input
                      id="confirmPassword"
                      type={showConfirmPassword ? "text" : "password"}
                      placeholder="••••••••"
                      value={confirmPassword}
                      onChange={(e) => setConfirmPassword(e.target.value)}
                      className="w-full pr-10 border-gray-300 focus:border-green-500 focus:ring-green-500"
                    />
                    <button
                      type="button"
                      onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                      className="absolute inset-y-0 right-0 flex items-center pr-3 hover:text-green-600 transition-colors"
                    >
                      {showConfirmPassword ? (
                        <EyeOff className="h-4 w-4 text-gray-400" />
                      ) : (
                        <Eye className="h-4 w-4 text-gray-400" />
                      )}
                    </button>
                  </div>
                </div>
              )}

              {/* Forgot Password (solo en login) */}
              {!isSignUp && (
                <div className="flex justify-end">
                  <button
                    type="button"
                    onClick={handleForgotPassword}
                    disabled={!email || !email.includes('@')}
                    className="text-sm text-green-600 hover:text-green-700 font-medium disabled:text-gray-400 disabled:cursor-not-allowed"
                  >
                    ¿Olvidaste tu contraseña?
                  </button>
                </div>
              )}

              <Button
                type="submit"
                className="w-full bg-gradient-to-r from-green-600 to-emerald-600 hover:from-green-700 hover:to-emerald-700 text-white font-semibold py-6 rounded-xl shadow-lg hover:shadow-2xl hover:scale-[1.02] transition-all duration-300 mt-2"
                disabled={isLoading || !email || !password}
              >
                {isLoading
                  ? (isSignUp ? 'Creando cuenta...' : 'Conectando...')
                  : (isSignUp ? 'Crear Cuenta' : 'Conectarse')}
              </Button>
            </form>

            {/* Divider */}
            <div className="relative my-6">
              <div className="absolute inset-0 flex items-center">
                <span className="w-full border-t border-gray-200" />
              </div>
              <div className="relative flex justify-center text-xs uppercase">
                <span className="bg-white px-3 text-gray-500 font-medium">O continúa con</span>
              </div>
            </div>

            {/* Social Login Buttons - FACEBOOK PRIMERO */}
            <div className="grid grid-cols-2 gap-4">
              {/* Facebook Login - PRIMERO */}
              <Button
                type="button"
                variant="outline"
                onClick={handleFacebookLogin}
                className="w-full border-2 border-gray-200 hover:border-blue-300 hover:bg-blue-50/50 py-6 rounded-xl transition-all duration-300 hover:shadow-md hover:scale-[1.02] flex items-center justify-center"
              >
                <svg className="h-4 w-4 mr-2" fill="#1877F2" viewBox="0 0 24 24">
                  <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z" />
                </svg>
                <span className="font-semibold text-gray-700 text-sm">Facebook</span>
              </Button>

              {/* Google Login - SEGUNDO */}
              <Button
                type="button"
                variant="outline"
                onClick={handleGoogleLogin}
                className="w-full border-2 border-gray-200 hover:border-green-300 hover:bg-green-50/50 py-6 rounded-xl transition-all duration-300 hover:shadow-md hover:scale-[1.02] flex items-center justify-center"
              >
                <svg className="h-6 w-6 mr-2" viewBox="0 0 24 24">
                  <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" />
                  <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" />
                  <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" />
                  <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" />
                </svg>
                <span className="font-semibold text-gray-700 text-sm">Google</span>
              </Button>
            </div>

            {/* Toggle between login/signup - DOS BOTONES SEPARADOS CON ANIMACIÓN */}
            <div className="pt-6 border-t border-gray-100 mt-6 space-y-3" style={{ transition: 'transform 0.6s', transform: `rotate(${menuRotation}deg)` }}>
              <p className="text-center text-sm text-gray-600">
                {isSignUp ? '¿Ya tienes cuenta?' : '¿No tienes cuenta?'}
              </p>
              <Button
                type="button"
                variant="outline"
                onClick={() => {
                  setIsSignUp(!isSignUp)
                  setMenuRotation(menuRotation + 180)
                  setEmail('')
                  setPassword('')
                  setNombre('')
                  setConfirmPassword('')
                }}
                className="w-full border-2 border-green-600 text-green-600 hover:bg-green-50 font-semibold py-6 rounded-xl transition-all duration-300"
              >
                {isSignUp ? 'Inicia sesión' : 'Regístrate'}
              </Button>
            </div>
          </CardContent>
        </Card>

        {/* Footer - Solo copyright */}
        <div className="text-center mt-6 text-xs text-gray-500">
          <p className="font-semibold">© 2026</p>
          <p>JAVIER - ANGIE</p>
        </div>
      </div>

      {/* Modal de Verificación 2FA */}
      {show2FAModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 px-4">
          <Card className="w-full max-w-md bg-white shadow-2xl">
            <CardHeader>
              <CardTitle className="text-xl text-center text-green-600">Verificación de Seguridad</CardTitle>
              <CardDescription className="text-center">
                Se ha detectado un nuevo dispositivo. Por favor ingresa el código de verificación enviado a tu correo/teléfono.
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <label htmlFor="verificationCode" className="text-sm font-medium text-gray-700">
                  Código de Verificación (6 dígitos)
                </label>
                <Input
                  id="verificationCode"
                  type="text"
                  maxLength={6}
                  placeholder="123456"
                  value={verificationCode}
                  onChange={(e) => setVerificationCode(e.target.value.replace(/\D/g, ''))}
                  className="w-full text-center text-2xl tracking-widest border-gray-300 focus:border-green-500 focus:ring-green-500"
                />
              </div>
              <div className="flex gap-3">
                <Button
                  onClick={() => {
                    setShow2FAModal(false)
                    setVerificationCode('')
                    setPendingProvider(null)
                  }}
                  variant="outline"
                  className="flex-1 border-2 border-gray-300 hover:bg-gray-50"
                >
                  Cancelar
                </Button>
                <Button
                  onClick={handleVerify2FA}
                  disabled={verificationCode.length !== 6}
                  className="flex-1 bg-gradient-to-r from-green-600 to-emerald-600 hover:from-green-700 hover:to-emerald-700 text-white"
                >
                  Verificar
                </Button>
              </div>
              <p className="text-xs text-center text-gray-500">
                ¿No recibiste el código? <button className="text-green-600 hover:underline">Reenviar</button>
              </p>
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  )
}