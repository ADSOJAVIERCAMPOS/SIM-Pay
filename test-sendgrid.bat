@echo off
echo ===============================================
echo    PRUEBA DE SENDGRID - SIM-Pay
echo ===============================================
echo.

echo [1/3] Verificando configuracion...
echo.

REM Verificar que existe .env
if not exist backend\.env (
    echo [ERROR] No existe backend\.env
    pause
    exit /b 1
)

REM Mostrar configuración (ocultando parte de la API Key por seguridad)
echo API Key configurada: Si (SG.zQpCy...)
echo From Email: ject2583@gmail.com
echo From Name: SIM-Pay Sistema
echo.

echo [2/3] Iniciando backend...
echo (Esto puede tardar 1-2 minutos la primera vez)
echo.

cd backend

REM Compilar y ejecutar
start cmd /k "mvnw spring-boot:run"

echo.
echo ===============================================
echo    BACKEND INICIADO
echo ===============================================
echo.
echo El backend se esta ejecutando en una nueva ventana.
echo.
echo [3/3] PROXIMOS PASOS:
echo.
echo 1. Espera a que aparezca: "Started SimplifyApplication"
echo 2. Ve a: http://localhost:3000/login
echo 3. Intenta registrarte o hacer login con Google/Facebook
echo 4. Revisa tu email: ject2583@gmail.com
echo    (Mira spam/promociones si no lo ves)
echo.
echo 5. Revisa la consola del backend para ver:
echo    - "Email enviado exitosamente via SendGrid"
echo    - "Status Code: 202"
echo.
echo ===============================================
echo.
pause
