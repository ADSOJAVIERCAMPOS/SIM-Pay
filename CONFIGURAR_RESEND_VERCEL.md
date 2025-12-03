# ✅ Configuración Resend + Vercel - Paso a Paso

## 🚀 Parte 1: Configurar Resend

### Paso 1: Instalar Integración en Vercel

1. Ve a: https://vercel.com/integrations/resend
2. Click en **"Add Integration"**
3. Selecciona tu proyecto: **"sim-pay-app"** (o el nombre de tu frontend)
4. Click **"Add"**
5. Autoriza la integración

✅ Esto creará automáticamente `RESEND_API_KEY` en Vercel

### Paso 2: Verificar Variable de Entorno en Vercel

1. Ve a tu proyecto en Vercel: https://vercel.com/dashboard
2. Selecciona **"sim-pay-app"**
3. Settings → Environment Variables
4. Deberías ver:
   ```
   RESEND_API_KEY = re_xxxxxxxxxxxx
   ```

---

## 🔧 Parte 2: Configurar Backend Local

### Paso 1: Copiar API Key de Vercel

Desde Vercel Dashboard:
1. Settings → Environment Variables
2. Click en el ícono de "ojo" junto a `RESEND_API_KEY`
3. Copia el valor completo (ejemplo: `re_xxxxxxxxxxxx`)

### Paso 2: Configurar backend/.env

Edita `backend/.env` y pega tu API Key:

```bash
RESEND_API_KEY=re_tu_api_key_completa_aqui
RESEND_FROM_EMAIL=ject2583@gmail.com
RESEND_FROM_NAME=SIM-Pay Sistema
```

---

## 🧪 Parte 3: Probar Envío de Emails

### Opción A: Desde Backend (Spring Boot)

1. **Iniciar el backend:**
   ```cmd
   cd backend
   mvnw spring-boot:run
   ```

2. **Espera a ver:** `Started SimplifyApplication`

3. **Iniciar el frontend:**
   ```cmd
   cd frontend
   npm run dev
   ```

4. **Probar registro:**
   - Ve a: http://localhost:3000/login
   - Click "Crear cuenta"
   - Completa el formulario:
     ```
     Nombre: Test Usuario
     Email: test@ejemplo.com
     Contraseña: Test123!
     ```
   - Click "Registrarse"

5. **Verificar en consola del backend:**
   ```
   📧 Enviando notificación de nuevo usuario...
   ✅ Email enviado exitosamente vía Resend
   Email ID: re_xxxxxxxxxxxx
   ✉️ EMAIL ENVIADO A SUPERADMIN: ject2583@gmail.com
   ```

6. **Revisar tu email:**
   - Inbox: `ject2583@gmail.com`
   - Busca: "👤 NUEVO USUARIO REGISTRADO - SIM-Pay"
   - Puede estar en spam/promociones la primera vez

### Opción B: Prueba Rápida con cURL

Desde tu terminal, ejecuta:

```bash
curl -X POST https://api.resend.com/emails \
  -H "Authorization: Bearer re_tu_api_key_aqui" \
  -H "Content-Type: application/json" \
  -d '{
    "from": "SIM-Pay Sistema <ject2583@gmail.com>",
    "to": ["ject2583@gmail.com"],
    "subject": "Prueba de Resend - SIM-Pay",
    "html": "<h1>Funciona!</h1><p>Email de prueba desde Resend.</p>"
  }'
```

Si funciona, verás:
```json
{
  "id": "re_xxxxxxxxxxxx"
}
```

---

## 📊 Parte 4: Monitorear Emails

### Dashboard de Resend

1. Ve a: https://resend.com/emails
2. Verás todos los emails enviados:
   - ✅ Estado (Delivered, Sent, etc.)
   - 📧 Destinatario
   - 📅 Fecha y hora
   - 👁️ Contenido del email

### Ver Logs en Backend

En la consola donde corre el backend verás:

```
📧 Enviando email de verificación a: test@ejemplo.com
✅ Email enviado exitosamente vía Resend
Email ID: re_abc123xyz
✉️ EMAIL ENVIADO A SUPERADMIN: ject2583@gmail.com
```

---

## 🔍 Troubleshooting

### ❌ Error: "Invalid API key"

**Causa:** API Key incorrecta o no configurada

**Solución:**
1. Verifica `backend/.env`
2. Copia de nuevo la API Key desde Resend: https://resend.com/api-keys
3. Asegúrate de que NO haya espacios antes/después

### ❌ Error: "From address not allowed"

**Causa:** En plan gratuito solo puedes enviar desde el email con el que te registraste

**Solución:**
1. Usa `ject2583@gmail.com` como remitente
2. O verifica un dominio personalizado en Resend

### 📧 Emails no llegan

**Solución:**
1. Revisa **spam/promociones** en Gmail
2. Ve a https://resend.com/emails y verifica el status
3. Si dice "Delivered", revisa todas las carpetas de Gmail
4. Si dice "Bounced", el email destino no existe

### 🐛 Backend no compila

**Solución:**
```cmd
cd backend
mvnw clean install -DskipTests
mvnw spring-boot:run
```

---

## ✅ Checklist de Verificación

- [ ] Integración Resend instalada en Vercel
- [ ] Variable `RESEND_API_KEY` visible en Vercel
- [ ] API Key copiada a `backend/.env`
- [ ] Backend compilado exitosamente
- [ ] Backend corriendo (`Started SimplifyApplication`)
- [ ] Frontend corriendo (`npm run dev`)
- [ ] Usuario de prueba registrado
- [ ] Email recibido en `ject2583@gmail.com`
- [ ] Email visible en https://resend.com/emails

---

## 🎯 Resumen

Una vez configurado, el sistema enviará emails automáticamente cuando:

1. ✉️ **Nuevo usuario se registre** → Email a superadmin
2. 🔐 **Login con OAuth (Google/Facebook)** → Código 2FA a superadmin
3. 📝 **Cambios en datos críticos** → Alerta a superadmin
4. 🔢 **Código de verificación** → Email al usuario

Todos los emails se registran en:
- 📊 Resend Dashboard: https://resend.com/emails
- 🗄️ PostgreSQL: tabla `device_logs`
- 📋 Consola del backend

---

**¿Listo para probar?** Sigue los pasos de la **Parte 3** 🚀
