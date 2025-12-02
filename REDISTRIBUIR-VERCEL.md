# 🚀 Guía para Redistribuir SIM-Pay en Vercel

## Estado Actual del Proyecto

### ✅ Correcciones Aplicadas (Listas para Desplegar)

1. **Frontend - Next.js 14**
   - ✅ Metadata y viewport correctamente separados en `layout.tsx`
   - ✅ Directiva `'use client'` agregada a todas las páginas dinámicas
   - ✅ Configuración `next.config.js` con `output: 'standalone'`
   - ✅ Eliminado archivo `vercel.json` conflictivo
   - ✅ Cambios subidos a GitHub (commit: `1aa9bfa`)

2. **Backend - Spring Boot**
   - ✅ Enum `Usuario.Rol` corregido con valores correctos
   - ✅ `DataInitializer` y `ProductionDataInitializer` actualizados
   - ✅ Cambios subidos a GitHub

---

## 📋 Pasos para Redistribuir en Vercel

### Paso 1: Acceder a Vercel
1. Ve a: https://vercel.com/javier-camposs-projects/sim-pay2026
2. Inicia sesión con tu cuenta de GitHub

### Paso 2: Verificar Configuración del Proyecto

#### 2.1 Settings → General
Asegúrate de que esté configurado:
- **Framework Preset**: `Next.js`
- **Root Directory**: `frontend` ⚠️ IMPORTANTE
- **Build Command**: (dejar vacío o `npm run build`)
- **Output Directory**: (dejar vacío o `.next`)
- **Install Command**: (dejar vacío o `npm install`)

#### 2.2 Settings → Environment Variables
Agrega estas variables para **Production, Preview y Development**:

```
NEXT_PUBLIC_API_URL=https://simpay-backend.railway.app/api
NEXTAUTH_URL=https://sim-pay2026.vercel.app
NEXTAUTH_SECRET=tu-secreto-super-seguro-aqui-cambiar-en-produccion
```

### Paso 3: Redistribuir el Proyecto

#### Opción A: Redistribución desde el Último Commit (RECOMENDADO)

1. Ve a la pestaña **Deployments**
2. Busca el despliegue más reciente (debería decir commit `1aa9bfa` o "fix: correct Usuario.Rol enum values in backend")
3. Haz clic en los **tres puntos (...)** a la derecha del despliegue
4. Selecciona **"Redeploy"**
5. **MUY IMPORTANTE**: 
   - ✅ **DESMARCA** la casilla "Use existing build cache"
   - Esto forzará una compilación limpia
6. Haz clic en **"Redeploy"**

#### Opción B: Forzar Nuevo Despliegue desde GitHub

Si la Opción A no funciona:
1. Ve a **Settings → Git**
2. Verifica que esté conectado al repositorio correcto: `ADSOJAVIERCAMPOS/SIM-Pay`
3. Haz clic en **"Disconnect"** y luego **"Connect"** de nuevo
4. Selecciona la rama `main`
5. Haz clic en **"Deploy"**

#### Opción C: Push Vacío para Forzar Despliegue

Si las opciones anteriores no funcionan, ejecuta en tu terminal local:

```bash
cd "c:\Users\USUARIO\OneDrive - SENA\Escritorio\Nueva carpeta"
git commit --allow-empty -m "chore: trigger Vercel deployment"
git push origin main
```

---

## 🔍 Verificar el Despliegue

### Durante el Despliegue
1. En la pestaña **Deployments**, verás el progreso en tiempo real
2. Puedes hacer clic en el despliegue para ver los logs detallados
3. Busca estos indicadores de éxito:
   ```
   ✓ Generating static pages (11/11)
   ✓ Finalizing page optimization
   ✓ Build Completed
   ```

### Después del Despliegue Exitoso
1. Verás un mensaje verde: **"Deployment Successful"**
2. Verás la URL de tu aplicación desplegada
3. Haz clic en **"Visit"** para abrir la aplicación

### Si Hay Errores
- Revisa los logs de construcción haciendo clic en el despliegue fallido
- Busca errores específicos en la sección **"Build Logs"**
- Los errores más comunes ya fueron corregidos:
  - ❌ ~~Conflicto functions vs builds~~ → ✅ Eliminado `vercel.json`
  - ❌ ~~Error de viewport en metadata~~ → ✅ Viewport separado
  - ❌ ~~Páginas sin 'use client'~~ → ✅ Directiva agregada

---

## 🌐 URLs del Proyecto Desplegado

Una vez desplegado correctamente, tu aplicación estará disponible en:

- **Producción**: https://sim-pay2026.vercel.app
- **Git Main**: https://sim-pay2026-git-main-javier-camposs-projects.vercel.app
- **Vercel Auto**: https://sim-pay2026-mmcx4fqyj-javier-camposs-proyectos.vercel.app

---

## 🐛 Solución de Problemas

### Error: "Root directory not found"
**Solución**: Ve a Settings → General y asegúrate de que **Root Directory** sea `frontend`

### Error: "Build failed" con mención a metadata
**Solución**: Esto ya fue corregido en el último commit. Asegúrate de redistribuir sin caché.

### Error: "Module not found" o dependencias
**Solución**: 
1. Verifica que `package.json` tenga todas las dependencias
2. Redistribuye sin caché (desmarca "Use existing build cache")

### El sitio se despliega pero aparece error 404
**Solución**: 
1. Verifica que el **Root Directory** esté en `frontend`
2. Verifica las variables de entorno
3. Revisa los logs del navegador (F12 → Console)

---

## 📞 Siguiente Paso: Backend en Railway

Una vez que el frontend esté desplegado en Vercel:
1. El backend ya está desplegado en Railway: https://simpay-backend.railway.app
2. Verifica que el backend esté funcionando: https://simpay-backend.railway.app/api/actuator/health
3. Si el backend no responde, necesitaremos redistribuirlo también

---

## ✅ Checklist Final

Antes de considerar el despliegue completo:

- [ ] Frontend desplegado exitosamente en Vercel
- [ ] Variables de entorno configuradas en Vercel
- [ ] Aplicación accesible desde la URL de producción
- [ ] Backend funcionando en Railway
- [ ] Conexión frontend-backend verificada (login funciona)
- [ ] No hay errores en la consola del navegador

---

**Fecha de última actualización**: 2 de diciembre de 2025  
**Commit actual**: `1aa9bfa` - "fix: correct Usuario.Rol enum values in backend"
