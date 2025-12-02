'use client'

import React, { createContext, useContext, useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import Cookies from 'js-cookie'

interface User {
  id: string
  email: string
  nombre: string
  rol: string
}

interface AuthContextType {
  user: User | null
  login: (token: string, userData: User) => void
  logout: () => void
  isAuthenticated: boolean
  isLoading: boolean
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const router = useRouter()

  useEffect(() => {
    // Verificar si hay un token almacenado al cargar la aplicación
    const token = Cookies.get('token')
    const userData = Cookies.get('user')

    if (token && userData) {
      try {
        const parsedUser = JSON.parse(userData)
        setUser(parsedUser)
      } catch (error) {
        console.error('Error parsing user data:', error)
        logout()
      }
    }
    
    setIsLoading(false)
  }, [])

  const login = (token: string, userData: User) => {
    // Guardar token y datos de usuario en cookies
    Cookies.set('token', token, { expires: 1 }) // 1 día
    Cookies.set('user', JSON.stringify(userData), { expires: 1 })
    
    setUser(userData)
    router.push('/dashboard')
  }

  const logout = () => {
    // Limpiar cookies y estado
    Cookies.remove('token')
    Cookies.remove('user')
    
    setUser(null)
    router.push('/login')
  }

  const value = {
    user,
    login,
    logout,
    isAuthenticated: !!user,
    isLoading,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}