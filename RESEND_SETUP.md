# 📧 Configuración de Resend - Email GRATIS Permanente

## ✅ Por Qué Resend en Lugar de SendGrid

| Característica | Resend | SendGrid |
|----------------|--------|----------|
| **Plan Gratuito** | ✅ **PERMANENTE** | ❌ Solo 2 meses |
| **Emails/día** | ✅ 100 | ✅ 100 |
| **Configuración** | ✅ 2 minutos | ❌ 15+ minutos |
| **Verificación** | ✅ Instantánea | ❌ DNS complicado |
| **Soporte** | ✅ Excelente | ⚠️ Limitado gratis |

---

## 🚀 Configuración Rápida (2 minutos)

### Paso 1: Crear Cuenta en Resend

1. Ve a: https://resend.com/signup
2. Regístrate con tu email `ject2583@gmail.com`
3. Verifica tu email (revisa inbox/spam)
4. Inicia sesión

### Paso 2: Obtener API Key

1. Una vez dentro, ve a: https://resend.com/api-keys
2. Click en **"Create API Key"**
3. Name: `SIM-Pay Backend`
4. Permission: **"Full access"** o **"Sending access"**
5. Click **"Add"**
6. **COPIA LA API KEY** (ejemplo: `re_xxxxxxxxxxxxxxxxxxxxxx`)

### Paso 3: Configurar en el Backend

Edita `backend/.env` y pega tu API Key:

```bash
RESEND_API_KEY=re_tu_api_key_completa_aqui
RESEND_FROM_EMAIL=ject2583@gmail.com
RESEND_FROM_NAME=SIM-Pay Sistema
```

**¡Listo!** No necesitas verificar dominios ni configurar DNS.

---

## 🧪 Probar el Sistema

### 1. Iniciar el Backend

```cmd
cd backend
mvnw spring-boot:run
```

Espera a ver: `Started SimplifyApplication`

### 2. Iniciar el Frontend

```cmd
cd frontend
npm run dev
```

### 3. Probar Envío de Emails

1. Ve a: http://localhost:3000/login
2. Haz click en **"Crear cuenta"**
3. Completa el formulario y registra un usuario
4. **Revisa tu email** `ject2583@gmail.com`
5. Deberías recibir: "👤 NUEVO USUARIO REGISTRADO"

### 4. Verificar en Consola del Backend

Deberías ver:

```
📧 Enviando notificación de nuevo usuario...
✅ Email enviado exitosamente vía Resend
Email ID: re_xxxxxxxxxxxx
✉️ EMAIL ENVIADO A SUPERADMIN: ject2583@gmail.com
```

---

## 📊 Monitoreo de Emails

### Dashboard de Resend

Ve a: https://resend.com/emails

Aquí verás:
- ✅ Emails enviados
- ✅ Status de entrega (Delivered, Bounced, etc.)
- ✅ Timestamp de cada email
- ✅ Contenido completo

Es mucho más simple que SendGrid!

---

## 🎯 Ventajas de Resend

### 1. Sin Verificación de Dominio
- ❌ No necesitas registros DNS
- ❌ No necesitas dominio propio
- ✅ Usa tu email de Gmail directamente

### 2. API Simple
- ✅ Una sola línea para enviar email
- ✅ Documentación clara
- ✅ Errores fáciles de entender

### 3. Dashboard Moderno
- ✅ Ver todos los emails enviados
- ✅ Filtrar por fecha, estado, destinatario
- ✅ Ver contenido HTML renderizado

### 4. Gratis Para Siempre
- ✅ 100 emails/día permanente
- ✅ Sin tarjeta de crédito
- ✅ Sin expiración

---

## 🚨 Troubleshooting

### Error: "Invalid API key"
**Solución**: Verifica que copiaste la API Key completa en `backend/.env`

### Emails no llegan
**Solución**: 
1. Revisa spam/promociones en Gmail
2. Ve a https://resend.com/emails y verifica el status
3. Si dice "Bounced", el email destino no existe

### Error: "From address not allowed"
**Solución**: 
- En plan gratuito solo puedes enviar desde el email con el que te registraste
- Usa `ject2583@gmail.com` como remitente

---

## 💰 Planes de Resend

### Plan Gratuito (Actual)
- ✅ 100 emails/día
- ✅ 3,000 emails/mes
- ✅ **Permanente (sin expiración)**
- ✅ Perfecto para desarrollo y producción inicial

### Plan Pro ($20/mes)
- ✅ 50,000 emails/mes
- ✅ Dominio personalizado
- ✅ Webhooks avanzados
- Solo si creces mucho

---

## 📚 Recursos

- **Dashboard**: https://resend.com/emails
- **API Keys**: https://resend.com/api-keys
- **Documentación**: https://resend.com/docs
- **Java SDK**: https://github.com/resend/resend-java

---

## ✅ Checklist Final

- [ ] Cuenta en Resend creada
- [ ] Email `ject2583@gmail.com` verificado
- [ ] API Key generada
- [ ] API Key en `backend/.env`
- [ ] Backend reiniciado
- [ ] Email de prueba enviado
- [ ] Email recibido en inbox

**Una vez completado, todos los emails funcionarán automáticamente.** 🎉
