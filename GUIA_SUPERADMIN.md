# 🔧 Guía Completa de Administración - SIM-Pay Superadmin

## 📧 1. Configurar SendGrid (Notificaciones por Email)

### Paso 1: Crear Cuenta en SendGrid
1. Ve a: https://signup.sendgrid.com/
2. Regístrate (plan gratuito: 100 emails/día)
3. Verifica tu email

### Paso 2: Obtener API Key
1. Accede a: https://app.sendgrid.com/settings/api_keys
2. Click en **"Create API Key"**
3. Nombre: `SIM-Pay Production`
4. Permisos: **Full Access**
5. Copia la clave (se muestra solo una vez)

### Paso 3: Verificar Email Remitente
1. Ve a: https://app.sendgrid.com/settings/sender_auth/senders
2. Click **"Create New Sender"**
3. Completa con:
   - From Name: `SIM-Pay Sistema`
   - From Email: `ject2583@gmail.com`
   - Reply To: `ject2583@gmail.com`
4. Verifica el email que te llega

### Paso 4: Configurar en el Backend
Edita el archivo `backend/.env`:
```bash
SENDGRID_API_KEY=SG.tu_api_key_completa_aqui
SENDGRID_FROM_EMAIL=ject2583@gmail.com
SENDGRID_FROM_NAME=SIM-Pay Sistema
```

### Paso 5: Probar
```cmd
cd backend
mvnw spring-boot:run
```
Los emails ahora se enviarán realmente a `ject2583@gmail.com`

---

## 🗄️ 2. Administrar Base de Datos PostgreSQL

### Opción A: Script de Administración (Recomendado)
```cmd
admin-database.bat
```

### Opción B: Herramientas Gráficas

#### pgAdmin (Instalado con PostgreSQL)
- **Ubicación**: Menu Inicio → PostgreSQL → pgAdmin 4
- **Primera vez:**
  1. Crear Master Password
  2. Click derecho en "Servers" → Register → Server
  3. Name: `SIM-Pay Local`
  4. Connection:
     - Host: `localhost`
     - Port: `5432`
     - Database: `simpay`
     - Username: `postgres`
     - Password: tu contraseña de postgres

#### DBeaver (Más Moderno - Recomendado)
1. Descargar: https://dbeaver.io/download/
2. Instalar y abrir
3. Database → New Database Connection
4. Seleccionar PostgreSQL
5. Configurar:
   - Host: `localhost`
   - Port: `5432`
   - Database: `simpay`
   - Username: `postgres`
   - Password: tu contraseña

---

## 📊 3. Consultas SQL Útiles para Superadmin

### Ver todos los usuarios
```sql
SELECT id, nombre, email, rol, created_at 
FROM usuarios 
ORDER BY created_at DESC;
```

### Ver logs de dispositivos (2FA)
```sql
SELECT 
    id,
    provider,
    verification_code,
    verified,
    verified_at,
    notification_sent_to,
    created_at
FROM device_logs 
ORDER BY created_at DESC;
```

### Ver intentos de login OAuth
```sql
SELECT 
    provider,
    COUNT(*) as intentos,
    COUNT(CASE WHEN verified = true THEN 1 END) as verificados
FROM device_logs 
GROUP BY provider;
```

### Ver usuarios superadmin
```sql
SELECT nombre, email, created_at 
FROM usuarios 
WHERE rol = 'SUPERADMIN';
```

### Ver actividad reciente
```sql
SELECT 
    'Usuario' as tipo,
    email as detalle,
    created_at
FROM usuarios
UNION ALL
SELECT 
    'DeviceLog' as tipo,
    provider || ' - ' || COALESCE(notification_sent_to, 'N/A') as detalle,
    created_at
FROM device_logs
ORDER BY created_at DESC
LIMIT 20;
```

---

## 🔐 4. Gestión de Roles de Superadmin

### Promover usuario a Superadmin
```sql
UPDATE usuarios 
SET rol = 'SUPERADMIN' 
WHERE email = 'ject2583@gmail.com';
```

### Ver permisos por rol
```sql
SELECT rol, COUNT(*) as cantidad 
FROM usuarios 
GROUP BY rol;
```

---

## 📱 5. Monitorear Notificaciones

### Verificar emails enviados
1. Dashboard SendGrid: https://app.sendgrid.com/email_activity
2. Filtrar por destinatario: `ject2583@gmail.com`

### Ver notificaciones en base de datos
```sql
SELECT 
    notification_sent_to,
    COUNT(*) as cantidad
FROM device_logs 
WHERE notification_sent_to IS NOT NULL
GROUP BY notification_sent_to;
```

---

## 🔧 6. Comandos Rápidos PostgreSQL

### Conectar desde terminal
```cmd
psql -U postgres -d simpay
```

### Listar todas las tablas
```sql
\dt
```

### Describir estructura de tabla
```sql
\d usuarios
\d device_logs
```

### Backup de base de datos
```cmd
pg_dump -U postgres simpay > backup_simpay_%date%.sql
```

### Restaurar backup
```cmd
psql -U postgres simpay < backup_simpay_20251202.sql
```

---

## 🚨 7. Troubleshooting

### PostgreSQL no se conecta
```cmd
# Verificar que esté corriendo
sc query postgresql-x64-16

# Iniciar servicio
net start postgresql-x64-16
```

### Ver logs de PostgreSQL
```
C:\Program Files\PostgreSQL\16\data\log\
```

### SendGrid - Emails no llegan
1. Revisar spam/promociones
2. Verificar en SendGrid Activity
3. Confirmar que el remitente esté verificado

---

## 📚 8. Recursos Adicionales

- **SendGrid Docs**: https://docs.sendgrid.com/
- **PostgreSQL Docs**: https://www.postgresql.org/docs/
- **pgAdmin Docs**: https://www.pgadmin.org/docs/
- **DBeaver Docs**: https://dbeaver.com/docs/

---

## ✅ Checklist de Configuración

- [ ] Cuenta SendGrid creada
- [ ] API Key generada y copiada
- [ ] Email remitente verificado
- [ ] Variables en `backend/.env` configuradas
- [ ] PostgreSQL instalado y corriendo
- [ ] pgAdmin o DBeaver configurado
- [ ] Primera conexión a base de datos exitosa
- [ ] Rol SUPERADMIN asignado a tu usuario

---

**¿Necesitas ayuda?** Revisa los logs del backend o ejecuta `admin-database.bat` para diagnóstico.
