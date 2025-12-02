'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { useAuth } from '@/lib/auth-context'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Shield, Eye, EyeOff, Mail, Lock, User, Chrome } from 'lucide-react'
import { toast } from 'sonner'

export default function LoginPage() {
  const [isSignUp, setIsSignUp] = useState(false)
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [nombre, setNombre] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
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
          // Auto-login después del registro
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

  const handleGoogleLogin = () => {
    toast.info('Google OAuth estará disponible próximamente')
    // TODO: Implementar OAuth con Google
    // window.location.href = `${process.env.NEXT_PUBLIC_API_URL}/oauth2/authorization/google`
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-600 via-emerald-600 to-teal-500 px-4 relative overflow-hidden">
      {/* Animated background elements */}
      <div className="absolute inset-0 overflow-hidden">
        <div className="absolute -top-40 -right-40 w-80 h-80 bg-white opacity-10 rounded-full blur-3xl animate-pulse"></div>
        <div className="absolute -bottom-40 -left-40 w-80 h-80 bg-white opacity-10 rounded-full blur-3xl animate-pulse delay-1000"></div>
      </div>

      <div className="w-full max-w-md space-y-8 relative z-10">
        {/* Header */}
        <div className="text-center">
          <div className="flex justify-center mb-4">
            <div className="bg-white p-4 rounded-2xl shadow-2xl">
              <Shield className="h-12 w-12 text-green-600" />
            </div>
          </div>
          <h1 className="text-4xl font-bold text-white mb-2">SIM-Pay</h1>
          <p className="text-green-100">Sistema de Inventario Modular</p>
        </div>

        {/* Login/Signup Card */}
        <Card className="w-full shadow-2xl border-0 backdrop-blur-sm bg-white/95">
          <CardHeader className="space-y-1 pb-4">
            <CardTitle className="text-2xl text-center font-bold bg-gradient-to-r from-green-600 to-emerald-600 bg-clip-text text-transparent">
              {isSignUp ? 'Crear Cuenta' : 'Iniciar Sesión'}
            </CardTitle>
            <CardDescription className="text-center text-gray-600">
              {isSignUp
                ? 'Completa el formulario para crear tu cuenta'
                : 'Ingresa tus credenciales para continuar'}
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <form onSubmit={handleSubmit} className="space-y-4">
              {/* Nombre (solo en registro) */}
              {isSignUp && (
                <div className="space-y-2">
                  <label htmlFor="nombre" className="text-sm font-medium text-gray-700 flex items-center gap-2">
                    <User className="h-4 w-4" />
                    Nombre Completo
                  </label>
                  <Input
                    id="nombre"
                    type="text"
                    placeholder="Juan Pérez"
                    value={nombre}
                    onChange={(e) => setNombre(e.target.value)}
                    className="w-full transition-all focus:ring-2 focus:ring-green-500"
                  />
                </div>
              )}

              {/* Email */}
              <div className="space-y-2">
                <label htmlFor="email" className="text-sm font-medium text-gray-700 flex items-center gap-2">
                  <Mail className="h-4 w-4" />
                  Correo Electrónico
                </label>
                <Input
                  id="email"
                  type="email"
                  placeholder="tu@email.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full transition-all focus:ring-2 focus:ring-green-500"
                />
              </div>

              {/* Password */}
              <div className="space-y-2">
                <label htmlFor="password" className="text-sm font-medium text-gray-700 flex items-center gap-2">
                  <Lock className="h-4 w-4" />
                  Contraseña
                </label>
                <div className="relative">
                  <Input
                    id="password"
                    type={showPassword ? "text" : "password"}
                    placeholder="••••••••"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="w-full pr-10 transition-all focus:ring-2 focus:ring-purple-500"
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
                    <Lock className="h-4 w-4" />
                    Confirmar Contraseña
                  </label>
                  <Input
                    id="confirmPassword"
                    type="password"
                    placeholder="••••••••"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    className="w-full transition-all focus:ring-2 focus:ring-green-500"
                  />
                </div>
              )}

              <Button
                type="submit"
                className="w-full bg-gradient-to-r from-green-600 to-emerald-600 hover:from-green-700 hover:to-emerald-700 text-white font-semibold py-6 rounded-lg shadow-lg hover:shadow-xl transition-all duration-300 transform hover:scale-[1.02]"
                disabled={isLoading}
              >
                {isLoading
                  ? (isSignUp ? 'Creando cuenta...' : 'Iniciando sesión...')
                  : (isSignUp ? 'Crear Cuenta' : 'Iniciar Sesión')}
              </Button>
            </form>

            {/* Divider */}
            <div className="relative">
              <div className="absolute inset-0 flex items-center">
                <span className="w-full border-t border-gray-300" />
              </div>
              <div className="relative flex justify-center text-xs uppercase">
                <span className="bg-white px-2 text-gray-500">O continúa con</span>
              </div>
            </div>

            {/* Google Login */}
            <Button
              type="button"
              variant="outline"
              onClick={handleGoogleLogin}
              className="w-full border-2 border-gray-300 hover:border-gray-400 hover:bg-gray-50 py-6 rounded-lg transition-all duration-300 transform hover:scale-[1.02]"
            >
              <Chrome className="h-5 w-5 mr-2 text-green-600" />
              <span className="font-semibold">Google</span>
            </Button>

            {/* Toggle between login/signup */}
            <div className="text-center pt-4">
              <button
                type="button"
                onClick={() => {
                  setIsSignUp(!isSignUp)
                  setEmail('')
                  setPassword('')
                  setNombre('')
                  setConfirmPassword('')
                }}
                className="text-sm text-gray-600 hover:text-green-600 transition-colors font-medium"
              >
                {isSignUp
                  ? '¿Ya tienes cuenta? Inicia sesión'
                  : '¿No tienes cuenta? Regístrate'}
              </button>
            </div>


          </CardContent>
        </Card>

        {/* Footer */}
        <div className="text-center text-sm text-white/90">
          <p className="font-medium">© 2026 SIM-Pay</p>
          <p className="mt-1">Producido por Javier Campos y Angie Pinzón</p>
        </div>
      </div>

      <style jsx global>{`
        @keyframes pulse {
          0%, 100% {
            opacity: 0.1;
          }
          50% {
            opacity: 0.2;
          }
        }
        .animate-pulse {
          animation: pulse 4s cubic-bezier(0.4, 0, 0.6, 1) infinite;
        }
        .delay-1000 {
          animation-delay: 1s;
        }
      `}</style>
    </div>
  )
}