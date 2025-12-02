# 🚀 Guía de Despliegue Frontend en Vercel

Esta guía asume que ya has desplegado el backend en Render (ver `DEPLOY-RENDER.md`).

## 📋 Pre-requisitos
1.  Cuenta en [Vercel.com](https://vercel.com)
2.  Backend desplegado y funcionando (URL disponible)

## 🛠️ Pasos para Desplegar

### 1. Importar Proyecto en Vercel
1.  Ve al Dashboard de Vercel -> **Add New...** -> **Project**.
2.  Importa tu repositorio de GitHub.
3.  **Framework Preset:** Next.js (se detectará automáticamente).
4.  **Root Directory:** Click en "Edit" y selecciona la carpeta `frontend`.

### 2. Configurar Variables de Entorno
Despliega la sección "Environment Variables" y agrega:

| Clave | Valor |
|-------|-------|
| `NEXT_PUBLIC_API_URL` | `https://tu-backend.onrender.com/api` (Reemplaza esto con TU URL real de Render) |

### 3. Desplegar
Click en **Deploy**. Vercel construirá tu aplicación Next.js.

### 4. Configuración Final (CORS)
Una vez que tengas la URL de tu frontend (ej: `https://simpay-frontend.vercel.app`):

1.  Vuelve a tu dashboard de **Render**.
2.  Ve a las variables de entorno de tu backend.
3.  Edita `CORS_ORIGINS` y pon la URL de tu frontend (sin barra al final).
    *   Ejemplo: `https://simpay-frontend.vercel.app`
4.  Render reiniciará el servicio automáticamente.

---

## ✅ Verificación Final
1.  Abre tu frontend en Vercel.
2.  Intenta iniciar sesión.
3.  Si todo funciona, ¡felicidades! Tienes tu sistema distribuido funcionando.
