# SIM-Pay - Despliegue en Vercel

## 🚀 Configuración de Deploy

### Root Directory
```
frontend
```

### Framework
```
Next.js
```

### Build Command
```
npm run build
```

### Output Directory  
```
.next
```

### Install Command
```
npm install
```

### Development Command
```
npm run dev
```

## 🔐 Variables de Entorno

Agregar en Vercel → Settings → Environment Variables:

### Variable 1
- **Name:** `NEXT_PUBLIC_API_URL`
- **Value:** `http://localhost:8080` (temporal)
- Cambiar después a: `https://sim-pay-backend.railway.app`

### Variable 2
- **Name:** `NEXT_PUBLIC_APP_NAME`
- **Value:** `SIM-Pay`

## 📝 Pasos de Deploy

1. **Importar desde GitHub**
   - Seleccionar repositorio `SIM-Pay`
   
2. **Configurar Root Directory**
   - Click en "Edit" junto a "Root Directory"
   - Escribir: `frontend`
   
3. **Agregar Variables de Entorno**
   - Expandir "Environment Variables"
   - Agregar cada variable con su Key y Value
   - Click en "Add" después de cada una
   
4. **Deploy**
   - Click en "Deploy"
   - Esperar 2-3 minutos
   
5. **Actualizar API URL**
   - Una vez tengas Railway funcionando
   - Ir a Settings → Environment Variables
   - Editar `NEXT_PUBLIC_API_URL`
   - Hacer Redeploy

## ✅ URLs Finales

- **Frontend:** https://sim-pay.vercel.app
- **Backend:** https://sim-pay-backend.railway.app (pendiente)

## 🎓 Tesis Doctoral

Sistema completo funcionando en la nube con:
- ✅ Trazabilidad inmutable SHA-256
- ✅ Pagos móviles (Nequi/Daviplata/WhatsApp)
- ✅ Arquitectura escalable
- ✅ Deploy automático CI/CD