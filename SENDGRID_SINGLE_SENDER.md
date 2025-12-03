# 📧 Configuración Rápida de SendGrid - Sin Dominio

## ⚠️ Problema Detectado
Estás intentando autenticar `sim-pay-app.vercel.app` pero no tienes acceso al DNS de Vercel.

## ✅ Solución: Single Sender Verification (Recomendado)

### Paso 1: Cancelar la Autenticación de Dominio
1. Ve a SendGrid → Settings → Sender Authentication
2. Si hay un dominio en proceso, cancélalo o ignóralo

### Paso 2: Verificar Email Individual (Más Fácil)
1. Ve a: https://app.sendgrid.com/settings/sender_auth/senders
2. Click **"Create New Sender"**
3. Completa el formulario:

```
From Name: SIM-Pay Sistema
From Email Address: ject2583@gmail.com
Reply To: ject2583@gmail.com
Company Address: Tu dirección
City: Tu ciudad
Country: Colombia
Nickname: SIM-Pay Sender (solo para identificación interna)
```

4. Click **"Create"**
5. **Revisa tu email** `ject2583@gmail.com`
6. Click en el link de verificación que te llega

### Paso 3: Obtener API Key
1. Ve a: https://app.sendgrid.com/settings/api_keys
2. Click **"Create API Key"**
3. Name: `SIM-Pay Backend`
4. Permissions: **Full Access** (o Restricted Access con "Mail Send")
5. Click **"Create & View"**
6. **COPIA LA API KEY** (se muestra solo una vez)

Ejemplo de API Key:
```
SG.xxxxxxxxxxxxxxxxxxxxxxx.yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
```

### Paso 4: Configurar en Backend

Edita `backend/.env`:
```bash
# Pega tu API Key real aquí (sin espacios ni comillas)
SENDGRID_API_KEY=SG.tu_api_key_completa_sin_espacios

# Email que verificaste en el paso 2
SENDGRID_FROM_EMAIL=ject2583@gmail.com
SENDGRID_FROM_NAME=SIM-Pay Sistema
```

### Paso 5: Probar
```cmd
cd backend
mvnw spring-boot:run
```

Deberías ver en la consola:
```
✅ Email enviado exitosamente vía SendGrid
Status Code: 202
```

---

## 🎯 Por Qué Single Sender es Mejor Para Ti

| Característica | Single Sender | Domain Authentication |
|----------------|---------------|----------------------|
| Configuración | ✅ 5 minutos | ❌ Requiere acceso DNS |
| Verificación | ✅ Click en email | ❌ Configurar registros CNAME/TXT |
| Funcionalidad | ✅ 100% completa | ✅ 100% completa |
| Emails/día (gratis) | ✅ 100 | ✅ 100 |
| Dominio personalizado | ❌ Usa @gmail.com | ✅ Usa @tudominio.com |

**Para desarrollo y producción inicial, Single Sender es perfecto.**

---

## 🔍 Verificar Configuración

### 1. Ver tu Sender Verificado
https://app.sendgrid.com/settings/sender_auth/senders

Debe aparecer con un ✅ verde.

### 2. Probar Envío desde SendGrid
1. Ve a: https://app.sendgrid.com/guide/integrate
2. Selecciona "Web API" → "cURL"
3. Verás un comando de prueba con tu API Key

### 3. Ver Actividad de Emails
https://app.sendgrid.com/email_activity

Aquí aparecerán todos los emails que envíes.

---

## 🚨 Troubleshooting

### Error: "The from email does not contain a valid address"
**Causa**: El email no está verificado  
**Solución**: Verificar en Single Sender Verification

### Error: "Unauthorized"
**Causa**: API Key incorrecta o sin permisos  
**Solución**: Regenerar API Key con Full Access

### Error: Emails no llegan
**Solución**: 
1. Revisa spam/promociones en Gmail
2. Verifica en SendGrid Activity Dashboard
3. Confirma que el sender esté verificado (✅ verde)

---

## 📊 Límites del Plan Gratuito

- **100 emails/día** (3,000/mes)
- Suficiente para:
  - Notificaciones de nuevos usuarios
  - Códigos 2FA
  - Alertas de dispositivos
  - Cambios en datos

Si necesitas más:
- **Essentials Plan**: $19.95/mes = 50,000 emails/mes
- **Pro Plan**: $89.95/mes = 100,000 emails/mes

---

## ✅ Checklist Final

- [ ] Cancelar/Ignorar autenticación de dominio
- [ ] Crear Single Sender con `ject2583@gmail.com`
- [ ] Verificar email (click en link recibido)
- [ ] Crear API Key en SendGrid
- [ ] Copiar API Key completa
- [ ] Pegar en `backend/.env`
- [ ] Reiniciar backend
- [ ] Probar enviando un email

---

**¿Listo?** Una vez verificado el email y configurada la API Key, los emails llegarán automáticamente a `ject2583@gmail.com` cada vez que:
- Un nuevo usuario se registre
- Alguien intente login con OAuth
- Haya cambios en datos críticos
- Se solicite un código 2FA
