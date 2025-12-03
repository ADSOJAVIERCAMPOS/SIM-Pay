setup-resend.bat@echo off
echo ===============================================
echo    CONFIGURACION RESEND - SIM-Pay
echo    (Email Gratis Permanente)
echo ===============================================
echo.

echo [PASO 1] Crear cuenta en Resend
echo.
echo 1. Abre: https://resend.com/signup
echo 2. Registrate con: ject2583@gmail.com
echo 3. Verifica tu email
echo.
pause

echo.
echo [PASO 2] Obtener API Key
echo.
echo 1. Inicia sesion en Resend
echo 2. Ve a: https://resend.com/api-keys
echo 3. Click "Create API Key"
echo 4. Name: SIM-Pay Backend
echo 5. Permission: Full access
echo 6. Click "Add"
echo 7. COPIA la API Key (ejemplo: re_xxxxxxxxxxxx)
echo.
set /p apikey="Pega tu API Key aqui y presiona Enter: "

if "%apikey%"=="" (
    echo.
    echo [ERROR] No ingresaste ninguna API Key
    pause
    exit /b 1
)

echo.
echo [PASO 3] Configurando backend\.env...
echo.

REM Crear backup del .env anterior
if exist backend\.env (
    copy backend\.env backend\.env.backup >nul
    echo [OK] Backup creado: backend\.env.backup
)

REM Crear nuevo .env
(
echo # ========================================
echo # CONFIGURACION RESEND - Email Service ^(GRATIS PERMANENTE^)
echo # ========================================
echo.
echo # Obten tu API Key en: https://resend.com/api-keys
echo RESEND_API_KEY=%apikey%
echo.
echo # Email remitente ^(debe ser tuyo o un dominio verificado^)
echo RESEND_FROM_EMAIL=ject2583@gmail.com
echo RESEND_FROM_NAME=SIM-Pay Sistema
echo.
echo # ========================================
echo # BASE DE DATOS POSTGRESQL ^(si usas local^)
echo # ========================================
echo DATABASE_URL=jdbc:postgresql://localhost:5432/simpay
echo DB_DRIVER=org.postgresql.Driver
echo DB_USERNAME=postgres
echo DB_PASSWORD=
echo DB_DIALECT=org.hibernate.dialect.PostgreSQLDialect
echo.
echo # ========================================
echo # DESARROLLO
echo # ========================================
echo SPRING_PROFILES_ACTIVE=dev
echo DDL_AUTO=update
echo SHOW_SQL=true
) > backend\.env

echo [OK] Archivo backend\.env configurado
echo.

echo ===============================================
echo    CONFIGURACION COMPLETADA
echo ===============================================
echo.
echo API Key configurada: %apikey:~0,10%...
echo From Email: ject2583@gmail.com
echo.
echo [PASO 4] Iniciar backend
echo.
echo Ejecuta en otra terminal:
echo    cd backend
echo    mvnw spring-boot:run
echo.
echo [PASO 5] Iniciar frontend
echo.
echo Ejecuta en otra terminal:
echo    cd frontend
echo    npm run dev
echo.
echo [PASO 6] Probar el sistema
echo.
echo 1. Ve a: http://localhost:3000/login
echo 2. Registra un nuevo usuario
echo 3. Revisa tu email: ject2583@gmail.com
echo 4. Deberias recibir: "NUEVO USUARIO REGISTRADO"
echo.
echo Dashboard de Resend: https://resend.com/emails
echo.
echo ===============================================
pause
