# 📧 Configuración de SendGrid para SIM-Pay

## Guía Rápida de Configuración

### 1️⃣ Crear Cuenta en SendGrid

1. Visita: https://signup.sendgrid.com/
2. Completa el registro (plan gratuito: 100 emails/día)
3. Verifica tu email

### 2️⃣ Generar API Key

1. Accede a: https://app.sendgrid.com/settings/api_keys
2. Click en **"Create API Key"**
3. Nombre sugerido: `SIM-Pay Production`
4. Selecciona: **"Full Access"** (o "Restricted Access" con permisos de Mail Send)
5. Copia la API Key generada (solo se muestra una vez)

### 3️⃣ Verificar Remitente (Sender)

**Opción A - Single Sender (Más Rápida):**
1. Ve a: https://app.sendgrid.com/settings/sender_auth/senders
2. Click en **"Create New Sender"**
3. Completa:
   - From Name: `SIM-Pay Sistema`
   - From Email: `ject2583@gmail.com` (o tu email)
   - Reply To: (mismo email)
4. Verifica el email recibido de SendGrid

**Opción B - Domain Authentication (Producción):**
1. Ve a: https://app.sendgrid.com/settings/sender_auth/domain
2. Sigue el proceso para tu dominio (requiere acceso DNS)

### 4️⃣ Configurar Variables de Entorno

**Desarrollo Local:**

1. Crea archivo `.env` en la carpeta `backend/`:
```bash
SENDGRID_API_KEY=SG.tu_api_key_aqui
SENDGRID_FROM_EMAIL=ject2583@gmail.com
SENDGRID_FROM_NAME=SIM-Pay Sistema
```

2. Carga las variables en Windows:
```cmd
set SENDGRID_API_KEY=SG.tu_api_key_aqui
set SENDGRID_FROM_EMAIL=ject2583@gmail.com
```

**Railway (Producción):**

1. Ve a tu proyecto en Railway
2. Settings → Variables
3. Agrega:
   - `SENDGRID_API_KEY`: tu API key real
   - `SENDGRID_FROM_EMAIL`: email verificado
   - `SENDGRID_FROM_NAME`: SIM-Pay Sistema

### 5️⃣ Probar Configuración

**Sin API Key (Modo Simulación):**
- El sistema mostrará emails en la consola del backend
- Útil para desarrollo sin configurar SendGrid

**Con API Key:**
```bash
cd backend
mvnw spring-boot:run
```

Deberías ver en los logs:
```
✅ Email enviado exitosamente vía SendGrid
Status Code: 202
```

## 📊 Monitoreo

**Dashboard de SendGrid:**
- Activity: https://app.sendgrid.com/email_activity
- Ver emails enviados, entregados, abiertos
- Revisar errores y bounces

## 🔒 Seguridad

- ✅ **NUNCA** commits la API Key en Git
- ✅ Usa `.env.example` como plantilla
- ✅ Rota la API Key si se expone
- ✅ Usa "Restricted Access" con solo permisos necesarios

## 🚨 Límites del Plan Gratuito

- **100 emails/día** (3,000/mes)
- Suficiente para desarrollo y pruebas
- Considera plan de pago para producción ($19.95/mes = 50,000 emails)

## 📧 Emails que Envía SIM-Pay

1. **Nuevo Dispositivo** → `ject2583@gmail.com`
2. **Nuevo Usuario** → `ject2583@gmail.com`
3. **Cambio de Datos** → `ject2583@gmail.com`
4. **Código de Verificación** → Email del usuario

## 🐛 Troubleshooting

**Error: "The from email does not contain a valid address"**
- Solución: Verifica el remitente en SendGrid

**Error: "Forbidden"**
- Solución: Revisa que la API Key tenga permisos "Mail Send"

**Error: "Unauthorized"**
- Solución: La API Key es incorrecta o expiró

**No llegan emails:**
- Revisa spam/promociones
- Verifica en SendGrid Activity Dashboard
- Confirma que el email destino es válido

## 📚 Recursos

- [Documentación SendGrid](https://docs.sendgrid.com/)
- [Java Library Reference](https://github.com/sendgrid/sendgrid-java)
- [API Key Permissions](https://docs.sendgrid.com/ui/account-and-settings/api-keys)

---

**¿Listo para producción?** 🚀

Una vez configurado SendGrid, todos los emails se enviarán automáticamente con diseño HTML profesional y notificaciones al superadmin `ject2583@gmail.com`.
