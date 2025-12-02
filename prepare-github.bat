@echo off
echo ========================================
echo   SIM-Pay - Preparacion para GitHub
echo   Despliegue Vercel + Railway
echo ========================================
echo.

:: Verificar Git
git --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Git no esta instalado
    echo Instalar desde: https://git-scm.com/
    pause
    exit /b 1
)

echo ✓ Git detectado
echo.

:: Inicializar repositorio si no existe
if not exist ".git" (
    echo Inicializando repositorio Git...
    git init
    echo ✓ Repositorio inicializado
) else (
    echo ✓ Repositorio Git ya existe
)

:: Crear .gitignore si no existe
if not exist ".gitignore" (
    echo Creando .gitignore...
    (
        echo # Dependencies
        echo node_modules/
        echo .pnp
        echo .pnp.js
        echo.
        echo # Production
        echo /frontend/build
        echo /frontend/dist
        echo /backend/target/
        echo.
        echo # Environment variables
        echo .env
        echo .env.local
        echo .env.development.local
        echo .env.test.local
        echo .env.production.local
        echo.
        echo # IDE
        echo .vscode/
        echo .idea/
        echo *.swp
        echo *.swo
        echo *~
        echo.
        echo # OS
        echo .DS_Store
        echo Thumbs.db
        echo.
        echo # Logs
        echo npm-debug.log*
        echo yarn-debug.log*
        echo yarn-error.log*
        echo *.log
        echo.
        echo # Runtime
        echo pids
        echo *.pid
        echo *.seed
        echo *.pid.lock
    ) > .gitignore
    echo ✓ .gitignore creado
)

:: Agregar archivos al staging
echo.
echo Agregando archivos al repositorio...
git add .

:: Commit inicial
git status
echo.
echo ¿Realizar commit inicial? (s/n)
set /p commit_choice=

if /i "%commit_choice%"=="s" (
    git commit -m "feat: SIM-Pay - Sistema completo para tesis doctoral

- Backend Spring Boot + PostgreSQL + JWT
- Frontend Next.js + TypeScript + Tailwind
- Trazabilidad inmutable SHA-256
- Pagos móviles Nequi/Daviplata/WhatsApp
- Configurado para Vercel + Railway
- Documentación completa incluida"
    echo ✓ Commit realizado
)

echo.
echo ========================================
echo        Próximos pasos:
echo ========================================
echo 1. Crear repositorio en GitHub
echo 2. git remote add origin [URL-DEL-REPO]
echo 3. git branch -M main
echo 4. git push -u origin main
echo.
echo 5. Configurar Railway (backend + PostgreSQL)
echo 6. Configurar Vercel (frontend)
echo 7. Actualizar variables de entorno
echo.
echo Ver guía completa: DEPLOYMENT-VERCEL.md
echo ========================================
pause