@echo off
echo ===============================================
echo    SendGrid Configuration Check - SIM-Pay
echo ===============================================
echo.

REM Verificar si existe .env
if exist backend\.env (
    echo [OK] Archivo .env encontrado
) else (
    echo [!] ADVERTENCIA: No existe backend\.env
    echo     Crea uno basado en .env.example
    echo.
)

REM Verificar variables de entorno
if defined SENDGRID_API_KEY (
    echo [OK] SENDGRID_API_KEY esta configurada
    echo     Valor: %SENDGRID_API_KEY:~0,10%...
) else (
    echo [!] SENDGRID_API_KEY no configurada
    echo     El sistema funcionara en modo simulacion
    echo.
)

if defined SENDGRID_FROM_EMAIL (
    echo [OK] SENDGRID_FROM_EMAIL: %SENDGRID_FROM_EMAIL%
) else (
    echo [!] SENDGRID_FROM_EMAIL no configurada
    echo     Se usara el valor por defecto: noreply@simpay.com
    echo.
)

echo.
echo ===============================================
echo    Instrucciones:
echo ===============================================
echo 1. Obtener API Key en: https://app.sendgrid.com/settings/api_keys
echo 2. Crear backend\.env con:
echo    SENDGRID_API_KEY=SG.tu_api_key
echo    SENDGRID_FROM_EMAIL=ject2583@gmail.com
echo 3. Ejecutar: mvnw spring-boot:run
echo.
echo Para configurar temporalmente (esta sesion):
echo    set SENDGRID_API_KEY=SG.tu_api_key
echo    set SENDGRID_FROM_EMAIL=ject2583@gmail.com
echo.
echo ===============================================

pause
