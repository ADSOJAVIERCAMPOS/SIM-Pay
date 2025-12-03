@echo off
echo ===============================================
echo    PostgreSQL Admin - SIM-Pay Superadmin
echo ===============================================
echo.

REM Verificar si PostgreSQL está instalado
where psql >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [!] PostgreSQL no encontrado en PATH
    echo.
    echo Opciones:
    echo 1. Instalar PostgreSQL: https://www.postgresql.org/download/windows/
    echo 2. Usar pgAdmin (interfaz grafica): viene con PostgreSQL
    echo 3. Usar DBeaver (recomendado): https://dbeaver.io/download/
    echo.
    pause
    exit /b 1
)

echo [OK] PostgreSQL encontrado
echo.

:MENU
echo ===============================================
echo    MENU DE ADMINISTRACION
echo ===============================================
echo 1. Conectar a base de datos SIM-Pay
echo 2. Ver todas las tablas
echo 3. Ver usuarios registrados
echo 4. Ver logs de dispositivos (2FA)
echo 5. Ver productos
echo 6. Ver ventas
echo 7. Ejecutar consulta personalizada
echo 8. Abrir pgAdmin
echo 9. Salir
echo.
set /p opcion="Selecciona una opcion (1-9): "

if "%opcion%"=="1" goto CONECTAR
if "%opcion%"=="2" goto VER_TABLAS
if "%opcion%"=="3" goto VER_USUARIOS
if "%opcion%"=="4" goto VER_LOGS
if "%opcion%"=="5" goto VER_PRODUCTOS
if "%opcion%"=="6" goto VER_VENTAS
if "%opcion%"=="7" goto CONSULTA_CUSTOM
if "%opcion%"=="8" goto PGADMIN
if "%opcion%"=="9" goto FIN

echo Opcion invalida
goto MENU

:CONECTAR
echo.
echo Conectando a PostgreSQL...
psql -U postgres -d simpay
goto MENU

:VER_TABLAS
echo.
echo === TABLAS EN LA BASE DE DATOS ===
psql -U postgres -d simpay -c "\dt"
echo.
pause
goto MENU

:VER_USUARIOS
echo.
echo === USUARIOS REGISTRADOS ===
psql -U postgres -d simpay -c "SELECT id, nombre, email, rol, created_at FROM usuarios ORDER BY created_at DESC;"
echo.
pause
goto MENU

:VER_LOGS
echo.
echo === LOGS DE DISPOSITIVOS (2FA) ===
psql -U postgres -d simpay -c "SELECT id, provider, verification_code, verified, created_at, notification_sent_to FROM device_logs ORDER BY created_at DESC LIMIT 20;"
echo.
pause
goto MENU

:VER_PRODUCTOS
echo.
echo === PRODUCTOS ===
psql -U postgres -d simpay -c "SELECT id, nombre, precio, stock, categoria FROM productos LIMIT 20;"
echo.
pause
goto MENU

:VER_VENTAS
echo.
echo === ULTIMAS VENTAS ===
psql -U postgres -d simpay -c "SELECT id, total, metodo_pago, created_at FROM ventas ORDER BY created_at DESC LIMIT 20;"
echo.
pause
goto MENU

:CONSULTA_CUSTOM
echo.
echo Introduce tu consulta SQL (ejemplo: SELECT * FROM usuarios WHERE rol='SUPERADMIN';)
set /p consulta="SQL> "
psql -U postgres -d simpay -c "%consulta%"
echo.
pause
goto MENU

:PGADMIN
echo.
echo Abriendo pgAdmin...
start "" "C:\Program Files\PostgreSQL\16\pgAdmin 4\bin\pgAdmin4.exe" 2>nul
if %ERRORLEVEL% NEQ 0 (
    start "" "C:\Program Files\PostgreSQL\15\pgAdmin 4\bin\pgAdmin4.exe" 2>nul
)
if %ERRORLEVEL% NEQ 0 (
    echo pgAdmin no encontrado. Descarga desde: https://www.pgadmin.org/download/
)
goto MENU

:FIN
echo.
echo Saliendo...
exit /b 0
