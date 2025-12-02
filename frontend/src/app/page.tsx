import Link from 'next/link'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { ArrowRight, Shield, BarChart3, Smartphone, Zap } from 'lucide-react'

export default function HomePage() {
  return (
    <div className="flex flex-col min-h-screen">
      {/* Header */}
      <header className="px-4 lg:px-6 h-14 flex items-center border-b">
        <Link className="flex items-center justify-center" href="/">
          <Shield className="h-6 w-6 mr-2 text-primary" />
          <span className="font-bold text-xl">SIM-Pay</span>
        </Link>
        <nav className="ml-auto flex gap-4 sm:gap-6">
          <Link className="text-sm font-medium hover:underline underline-offset-4" href="/login">
            Iniciar Sesión
          </Link>
        </nav>
      </header>

      {/* Hero Section */}
      <main className="flex-1">
        <section className="w-full py-12 md:py-24 lg:py-32 xl:py-48">
          <div className="container px-4 md:px-6">
            <div className="flex flex-col items-center space-y-4 text-center">
              <div className="space-y-2">
                <h1 className="text-3xl font-bold tracking-tighter sm:text-4xl md:text-5xl lg:text-6xl/none">
                  Sistema de Inventario Modular
                  <span className="block text-primary">con Trazabilidad Inmutable</span>
                </h1>
                <p className="mx-auto max-w-[700px] text-gray-500 md:text-xl dark:text-gray-400">
                  Control total de tu inventario con tecnología blockchain. Integración directa con 
                  WhatsApp, Nequi y Daviplata para pagos móviles seguros.
                </p>
              </div>
              <div className="space-x-4">
                <Button asChild size="lg">
                  <Link href="/login">
                    Comenzar Ahora
                    <ArrowRight className="ml-2 h-4 w-4" />
                  </Link>
                </Button>
                <Button variant="outline" size="lg">
                  Ver Demo
                </Button>
              </div>
            </div>
          </div>
        </section>

        {/* Features Section */}
        <section className="w-full py-12 md:py-24 lg:py-32 bg-gray-50 dark:bg-gray-900">
          <div className="container px-4 md:px-6">
            <div className="grid gap-6 lg:grid-cols-3 lg:gap-12">
              <Card>
                <CardHeader>
                  <Shield className="h-10 w-10 text-primary mb-2" />
                  <CardTitle>Trazabilidad Inmutable</CardTitle>
                  <CardDescription>
                    Cada transacción genera un hash SHA-256 único, creando una cadena inmutable 
                    de custody perfecta para auditorías.
                  </CardDescription>
                </CardHeader>
              </Card>
              
              <Card>
                <CardHeader>
                  <Smartphone className="h-10 w-10 text-primary mb-2" />
                  <CardTitle>Pagos Móviles Integrados</CardTitle>
                  <CardDescription>
                    Genera links de cobro automáticos para Nequi y Daviplata. Envía por WhatsApp 
                    con un solo clic.
                  </CardDescription>
                </CardHeader>
              </Card>
              
              <Card>
                <CardHeader>
                  <BarChart3 className="h-10 w-10 text-primary mb-2" />
                  <CardTitle>Analytics en Tiempo Real</CardTitle>
                  <CardDescription>
                    Dashboard completo con métricas de inventario, ventas y análisis de tendencias 
                    para toma de decisiones.
                  </CardDescription>
                </CardHeader>
              </Card>
            </div>
          </div>
        </section>

        {/* Technology Section */}
        <section className="w-full py-12 md:py-24 lg:py-32">
          <div className="container px-4 md:px-6">
            <div className="flex flex-col items-center space-y-4 text-center">
              <div className="space-y-2">
                <h2 className="text-3xl font-bold tracking-tighter sm:text-5xl">
                  Tecnología Empresarial
                </h2>
                <p className="mx-auto max-w-[900px] text-gray-500 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed dark:text-gray-400">
                  Construido con Java Spring Boot y Next.js para máxima robustez y escalabilidad. 
                  Cumple con normativas colombianas de protección de datos.
                </p>
              </div>
              
              <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4 mt-8">
                <div className="flex flex-col items-center space-y-2 p-4">
                  <Zap className="h-8 w-8 text-primary" />
                  <h3 className="font-semibold">Spring Boot</h3>
                  <p className="text-sm text-gray-500">Backend robusto</p>
                </div>
                <div className="flex flex-col items-center space-y-2 p-4">
                  <Zap className="h-8 w-8 text-primary" />
                  <h3 className="font-semibold">Next.js</h3>
                  <p className="text-sm text-gray-500">Frontend moderno</p>
                </div>
                <div className="flex flex-col items-center space-y-2 p-4">
                  <Zap className="h-8 w-8 text-primary" />
                  <h3 className="font-semibold">PostgreSQL</h3>
                  <p className="text-sm text-gray-500">Base de datos</p>
                </div>
                <div className="flex flex-col items-center space-y-2 p-4">
                  <Zap className="h-8 w-8 text-primary" />
                  <h3 className="font-semibold">Docker</h3>
                  <p className="text-sm text-gray-500">Containerización</p>
                </div>
              </div>
            </div>
          </div>
        </section>
      </main>

      {/* Footer */}
      <footer className="flex flex-col gap-2 sm:flex-row py-6 w-full shrink-0 items-center px-4 md:px-6 border-t">
        <p className="text-xs text-gray-500 dark:text-gray-400">
          © 2024 SIM-Pay. Proyecto de Tesis Doctoral - Todos los derechos reservados.
        </p>
        <nav className="sm:ml-auto flex gap-4 sm:gap-6">
          <Link className="text-xs hover:underline underline-offset-4" href="#">
            Términos de Servicio
          </Link>
          <Link className="text-xs hover:underline underline-offset-4" href="#">
            Privacidad
          </Link>
        </nav>
      </footer>
    </div>
  )
}