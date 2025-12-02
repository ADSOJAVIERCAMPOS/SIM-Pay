# 🚀 Guía de Despliegue Backend en Render

Dado que Vercel no soporta nativamente aplicaciones Java Spring Boot, usaremos **Render** como alternativa gratuita y compatible.

## 📋 Pre-requisitos
1.  Cuenta en [Render.com](https://render.com)
2.  Código subido a GitHub

## 🛠️ Pasos para Desplegar

### 1. Crear Base de Datos (PostgreSQL)
Render ofrece PostgreSQL gratuito por 90 días, o puedes usar Neon.tech (recomendado para producción gratuita).

**Opción A: Neon.tech (Recomendado)**
1.  Ve a [Neon.tech](https://neon.tech) y crea un proyecto.
2.  Copia la "Connection String" (ej: `postgres://user:pass@host/db...`).

**Opción B: Render PostgreSQL**
1.  En Render Dashboard, New + -> PostgreSQL.
2.  Nombre: `simpay-db`.
3.  Copia la `Internal DB URL` (si despliegas backend en Render) o `External DB URL`.

### 2. Crear Web Service en Render
1.  En Render Dashboard, click **New +** -> **Web Service**.
2.  Conecta tu repositorio de GitHub.
3.  Selecciona la carpeta raíz (o `backend` si te pregunta Root Directory, pero Render suele detectar el Dockerfile en la raíz del contexto).
    *   **Importante:** Si tu repositorio tiene el backend en una subcarpeta `/backend`, debes configurar:
        *   **Root Directory:** `backend`
4.  **Runtime:** Docker (Render detectará el `Dockerfile` automáticamente).
5.  **Instance Type:** Free.

### 3. Configurar Variables de Entorno
En la sección "Environment" del servicio en Render, agrega:

| Clave | Valor |
|-------|-------|
| `PORT` | `8080` |
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `DATABASE_URL` | Tu URL de conexión. **IMPORTANTE:** Debe empezar por `jdbc:postgresql://`. Si Render te da `postgres://...`, cámbialo a `jdbc:postgresql://...` |
| `DB_USERNAME` | Tu usuario de DB (si no está en la URL) |
| `DB_PASSWORD` | Tu contraseña de DB (si no está en la URL) |
| `JWT_SECRET` | Genera uno seguro (ej: base64 string largo) |
| `JWT_EXPIRATION` | `86400000` |
| `CORS_ORIGINS` | `https://tu-proyecto-frontend.vercel.app` (Lo actualizarás después de desplegar el frontend) |

### 4. Desplegar
Click en **Create Web Service**. Render comenzará a construir la imagen Docker. Esto puede tardar unos 5-10 minutos la primera vez.

Una vez finalizado, obtendrás una URL como: `https://simpay-backend.onrender.com`.

---

## ✅ Verificación
Visita `https://tu-backend.onrender.com/api/actuator/health`. Deberías ver `{"status":"UP"}`.
