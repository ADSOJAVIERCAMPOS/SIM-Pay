@echo off
echo ========================================
echo      SIM-Pay - Sistema de Inventario
echo      Tesis Doctoral - Inicio Rapido
echo ========================================
echo.

:: Verificar si Java esta instalado
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java no esta instalado o no esta en el PATH
    echo Por favor instale Java 17 o superior
    pause
    exit /b 1
)

:: Verificar si Node.js esta instalado
node --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Node.js no esta instalado o no esta en el PATH
    echo Por favor instale Node.js 18 o superior
    pause
    exit /b 1
)

echo ✓ Java detectado
echo ✓ Node.js detectado
echo.

echo Iniciando servicios...
echo.

:: Iniciar backend en una nueva ventana
echo 1. Iniciando Backend Spring Boot (Puerto 8080)...
start "SIM-Pay Backend" cmd /c "cd /d backend && mvnw spring-boot:run && pause"

:: Esperar un poco antes de iniciar el frontend
timeout /t 3 /nobreak >nul

:: Iniciar frontend en una nueva ventana
echo 2. Iniciando Frontend Next.js (Puerto 3001)...
start "SIM-Pay Frontend" cmd /c "cd /d frontend && npm run dev && pause"

echo.
echo ✓ Servicios iniciando...
echo.
echo URLs de acceso:
echo - Backend API: http://localhost:8080
echo - Frontend Web: http://localhost:3001
echo - Documentacion API: http://localhost:8080/swagger-ui.html
echo.
echo ========================================
echo   SIM-Pay esta iniciando correctamente
echo   Presiona cualquier tecla para salir
echo ========================================
pause >nul