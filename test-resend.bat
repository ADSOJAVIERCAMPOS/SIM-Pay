@echo off
echo ===============================================
echo    PRUEBA DE RESEND - SIM-Pay
echo ===============================================
echo.

REM Verificar que existe .env
if not exist backend\.env (
    echo [ERROR] No existe backend\.env
    echo.
    echo Ejecuta primero: setup-resend.bat
    pause
    exit /b 1
)

REM Verificar que la API Key este configurada
findstr /C:"RESEND_API_KEY=re_" backend\.env >nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] RESEND_API_KEY no configurada en backend\.env
    echo.
    echo Edita backend\.env y agrega tu API Key de Resend
    pause
    exit /b 1
)

echo [OK] API Key de Resend configurada
echo.

echo ===============================================
echo    PASO 1: Compilar Backend
echo ===============================================
echo.
cd backend
call mvnw clean install -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Fallo la compilacion
    pause
    exit /b 1
)

echo.
echo [OK] Backend compilado exitosamente
echo.

echo ===============================================
echo    PASO 2: Iniciar Backend
echo ===============================================
echo.
echo Iniciando Spring Boot en una nueva ventana...
echo.
start "SIM-Pay Backend" cmd /k "mvnw spring-boot:run"

timeout /t 5 /nobreak >nul

echo.
echo [OK] Backend iniciado en nueva ventana
echo.
echo ESPERA 30-60 segundos hasta ver: "Started SimplifyApplication"
echo.

cd ..

echo ===============================================
echo    PASO 3: Iniciar Frontend
echo ===============================================
echo.
echo Iniciando Next.js en una nueva ventana...
echo.
cd frontend
start "SIM-Pay Frontend" cmd /k "npm run dev"

timeout /t 3 /nobreak >nul

cd ..

echo.
echo [OK] Frontend iniciado en nueva ventana
echo.

echo ===============================================
echo    PASO 4: Probar el Sistema
echo ===============================================
echo.
echo 1. Espera a que el backend termine de iniciar
echo    (Busca: "Started SimplifyApplication" en la ventana del backend)
echo.
echo 2. Abre tu navegador: http://localhost:3000/login
echo.
echo 3. Click en "Crear cuenta"
echo.
echo 4. Completa el formulario:
echo    - Nombre: Usuario Prueba
echo    - Email: prueba@ejemplo.com
echo    - Contraseña: Test123!
echo    - Rol: CAJERO
echo.
echo 5. Click "Registrarse"
echo.
echo 6. REVISA LA CONSOLA DEL BACKEND (ventana negra)
echo    Deberas ver:
echo    - "Enviando notificacion de nuevo usuario..."
echo    - "Email enviado exitosamente via Resend"
echo    - "Email ID: re_xxxxxxxxxxxx"
echo.
echo 7. REVISA TU EMAIL: ject2583@gmail.com
echo    Busca: "NUEVO USUARIO REGISTRADO - SIM-Pay"
echo    (Puede estar en spam/promociones)
echo.
echo 8. VERIFICA EN RESEND DASHBOARD:
echo    https://resend.com/emails
echo.
echo ===============================================
echo    Presiona cualquier tecla para abrir el navegador...
echo ===============================================
pause >nul

start http://localhost:3000/login

echo.
echo Sistema listo! Sigue las instrucciones arriba.
echo.
pause
