# 🚀 Guía de Despliegue SIM-Pay en Vercel + Railway

## 📋 Pre-requisitos

1. **Cuenta en Vercel:** https://vercel.com/javier-camposs-projects
2. **Cuenta en Railway:** https://railway.app (para PostgreSQL + Backend)
3. **Repositorio en GitHub:** Código fuente del proyecto

---

## 🗄️ Paso 1: Configurar Base de Datos PostgreSQL

### Opción A: Railway (Recomendado)
1. Ir a https://railway.app
2. Crear nuevo proyecto
3. Agregar PostgreSQL database
4. Obtener las credenciales:
   ```
   DATABASE_URL=postgresql://username:password@host:port/database
   DB_USERNAME=tu_usuario
   DB_PASSWORD=tu_password
   DB_DRIVER=org.postgresql.Driver
   DB_DIALECT=org.hibernate.dialect.PostgreSQLDialect
   ```

### Opción B: Neon.tech (Gratis)
1. Ir a https://neon.tech
2. Crear cuenta y proyecto
3. Obtener connection string

---

## 🔧 Paso 2: Desplegar Backend en Railway

1. **Conectar repositorio:**
   - Fork/push tu código a GitHub
   - En Railway: New Project → Deploy from GitHub repo
   - Seleccionar carpeta `backend`

2. **Variables de entorno en Railway:**
   ```bash
   SPRING_PROFILES_ACTIVE=prod
   PORT=8080
   DATABASE_URL=postgresql://[tu-url-completa]
   DB_USERNAME=[tu-usuario]
   DB_PASSWORD=[tu-password]
   DB_DRIVER=org.postgresql.Driver
   DB_DIALECT=org.hibernate.dialect.PostgreSQLDialect
   DDL_AUTO=update
   SHOW_SQL=false
   H2_CONSOLE=false
   CORS_ORIGINS=https://sim-pay-frontend.vercel.app,https://*.vercel.app
   JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
   JWT_EXPIRATION=86400000
   ```

3. **Configurar build:**
   - Railway detectará automáticamente Maven
   - El `Procfile` ya está configurado
   - Verificar que use Java 17+

---

## 🌐 Paso 3: Desplegar Frontend en Vercel

1. **Conectar a Vercel:**
   - Ir a https://vercel.com/javier-camposs-projects
   - Import Git Repository
   - Seleccionar tu repositorio
   - Root Directory: `frontend`

2. **Variables de entorno en Vercel:**
   ```bash
   NEXT_PUBLIC_API_URL=https://[tu-app-railway].up.railway.app/api
   NEXTAUTH_URL=https://[tu-app-vercel].vercel.app
   NEXTAUTH_SECRET=tu-secreto-super-seguro-para-nextauth-en-produccion-2024
   ```

3. **Configuración automática:**
   - Vercel detectará Next.js
   - Build Command: `npm run build`
   - Output Directory: `.next`
   - Install Command: `npm install`

---

## 🔄 Paso 4: Configurar CORS

Una vez que tengas las URLs finales:

1. **Actualizar variables en Railway:**
   ```bash
   CORS_ORIGINS=https://tu-frontend.vercel.app,https://*.vercel.app
   ```

2. **Actualizar variables en Vercel:**
   ```bash
   NEXT_PUBLIC_API_URL=https://tu-backend.up.railway.app/api
   ```

---

## ✅ URLs Finales

Después del despliegue tendrás:

- **Frontend:** `https://sim-pay-frontend.vercel.app`
- **Backend API:** `https://simpay-backend.up.railway.app/api`
- **Documentación:** `https://simpay-backend.up.railway.app/api/swagger-ui.html`
- **Health Check:** `https://simpay-backend.up.railway.app/api/actuator/health`

---

## 🧪 Paso 5: Pruebas Post-Despliegue

1. **Verificar backend:**
   ```bash
   curl https://tu-backend.up.railway.app/api/actuator/health
   ```

2. **Probar login:**
   - Ir a tu frontend en Vercel
   - Login con: `admin@simpay.com` / `Admin123!`

3. **Verificar base de datos:**
   - Los datos se crearán automáticamente
   - Las tablas se generan con `ddl-auto=update`

---

## 🔧 Comandos Útiles

### Railway CLI:
```bash
npm install -g @railway/cli
railway login
railway logs
```

### Vercel CLI:
```bash
npm install -g vercel
vercel login
vercel logs
```

---

## 🆘 Troubleshooting

### Backend no inicia:
1. Verificar variables de entorno
2. Revisar logs en Railway
3. Confirmar PostgreSQL conectado

### Frontend no conecta:
1. Verificar `NEXT_PUBLIC_API_URL`
2. Revisar CORS en backend
3. Confirmar HTTPS en producción

### Base de datos:
1. Verificar `DATABASE_URL` formato correcto
2. Confirmar credenciales PostgreSQL
3. Revisar `ddl-auto=update` vs `create-drop`

---

## 📊 Monitoreo

- **Railway:** Dashboard con métricas automáticas
- **Vercel:** Analytics y performance insights
- **Logs:** Accesibles desde ambas plataformas

¡Tu sistema SIM-Pay estará disponible 24/7 en la nube! 🎉