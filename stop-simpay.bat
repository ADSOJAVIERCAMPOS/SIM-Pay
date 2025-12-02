@echo off
echo ========================================
echo      SIM-Pay - Deteniendo Servicios
echo ========================================
echo.

:: Matar procesos Java (Spring Boot)
echo Deteniendo Backend Spring Boot...
taskkill /f /im java.exe 2>nul
if not errorlevel 1 echo ✓ Backend detenido

:: Matar procesos Node.js (Next.js)
echo Deteniendo Frontend Next.js...
taskkill /f /im node.exe 2>nul
if not errorlevel 1 echo ✓ Frontend detenido

:: Liberar puertos especificos
echo Liberando puertos 8080 y 3001...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8080"') do taskkill /f /pid %%a 2>nul
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":3001"') do taskkill /f /pid %%a 2>nul

echo.
echo ✓ Todos los servicios SIM-Pay han sido detenidos
echo.
pause