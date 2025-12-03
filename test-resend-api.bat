@echo off
setlocal enabledelayedexpansion

echo ===============================================
echo    PRUEBA RAPIDA DE RESEND API
echo ===============================================
echo.

REM Leer API Key desde .env
if not exist backend\.env (
    echo [ERROR] No existe backend\.env
    pause
    exit /b 1
)

for /f "tokens=2 delims==" %%a in ('findstr /C:"RESEND_API_KEY" backend\.env') do set API_KEY=%%a

if "!API_KEY!"=="" (
    echo [ERROR] RESEND_API_KEY no encontrada en backend\.env
    pause
    exit /b 1
)

echo API Key encontrada: !API_KEY:~0,10!...
echo.

echo Enviando email de prueba a: ject2583@gmail.com
echo.

REM Crear archivo JSON temporal
(
echo {
echo   "from": "SIM-Pay Sistema ^<ject2583@gmail.com^>",
echo   "to": ["ject2583@gmail.com"],
echo   "subject": "🧪 Prueba de Resend - SIM-Pay",
echo   "html": "^<html^>^<body style='font-family: Arial; padding: 20px;'^>^<h1 style='color: #16a34a;'^>✅ Funciona!^</h1^>^<p^>Este es un email de prueba desde Resend.^</p^>^<p^>^<strong^>Sistema:^</strong^> SIM-Pay^<br^>^<strong^>Fecha:^</strong^> %date% %time%^</p^>^<p style='color: #666;'^>Si recibes este email, la configuración de Resend está correcta.^</p^>^</body^>^</html^>"
echo }
) > temp_email.json

echo Archivo JSON creado
echo.

REM Verificar si curl existe
where curl >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] curl no encontrado
    echo.
    echo Instala curl o usa PowerShell:
    echo.
    echo Invoke-WebRequest -Uri "https://api.resend.com/emails" `
    echo   -Method POST `
    echo   -Headers @{"Authorization"="Bearer !API_KEY!"; "Content-Type"="application/json"} `
    echo   -Body (Get-Content temp_email.json -Raw^)
    echo.
    del temp_email.json
    pause
    exit /b 1
)

echo Enviando email via Resend API...
echo.

curl -X POST https://api.resend.com/emails ^
  -H "Authorization: Bearer !API_KEY!" ^
  -H "Content-Type: application/json" ^
  -d @temp_email.json

echo.
echo.

REM Limpiar archivo temporal
del temp_email.json

echo ===============================================
echo    RESULTADO
echo ===============================================
echo.
echo Si ves: {"id":"re_xxxxxxxxxxxx"}
echo    [OK] Email enviado exitosamente!
echo.
echo Si ves un error:
echo    [ERROR] Revisa la API Key en backend\.env
echo.
echo PROXIMOS PASOS:
echo.
echo 1. Revisa tu email: ject2583@gmail.com
echo    (Puede estar en spam/promociones)
echo.
echo 2. Ve a Resend Dashboard:
echo    https://resend.com/emails
echo.
echo 3. Si llego el email, ejecuta: test-resend.bat
echo    para probar el sistema completo
echo.
echo ===============================================
pause
