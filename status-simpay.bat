@echo off
echo ========================================
echo      SIM-Pay - Estado de Servicios
echo ========================================
echo.

:: Verificar Backend (Puerto 8080)
netstat -an | findstr ":8080" >nul 2>&1
if errorlevel 1 (
    echo ❌ Backend Spring Boot: NO EJECUTANDOSE
) else (
    echo ✅ Backend Spring Boot: EJECUTANDOSE (Puerto 8080)
)

:: Verificar Frontend (Puerto 3001)
netstat -an | findstr ":3001" >nul 2>&1
if errorlevel 1 (
    echo ❌ Frontend Next.js: NO EJECUTANDOSE
) else (
    echo ✅ Frontend Next.js: EJECUTANDOSE (Puerto 3001)
)

:: Verificar Base de Datos H2
netstat -an | findstr ":9092" >nul 2>&1
if errorlevel 1 (
    echo ⚪ Base de Datos H2: Embebida (No puerto externo)
) else (
    echo ✅ Base de Datos H2: EJECUTANDOSE (Puerto 9092)
)

echo.
echo ========================================
echo           URLs de Acceso
echo ========================================
echo - Frontend: http://localhost:3001
echo - Backend API: http://localhost:8080
echo - H2 Console: http://localhost:8080/h2-console
echo - Swagger UI: http://localhost:8080/swagger-ui.html
echo.

:: Verificar procesos Java y Node
echo ========================================
echo         Procesos del Sistema
echo ========================================
tasklist | findstr "java.exe" >nul 2>&1
if not errorlevel 1 echo ✅ Proceso Java detectado
tasklist | findstr "node.exe" >nul 2>&1
if not errorlevel 1 echo ✅ Proceso Node.js detectado

echo.
pause