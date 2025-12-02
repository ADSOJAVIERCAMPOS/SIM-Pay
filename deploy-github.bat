@echo off
echo 🚀 Iniciando deploy de SIM-Pay a GitHub...

REM Verificar que estamos en el directorio correcto
if not exist "frontend" (
    echo ❌ Error: No se encontró la carpeta frontend. Asegúrate de estar en el directorio raíz del proyecto.
    pause
    exit /b 1
)

REM Agregar todos los archivos al staging
echo 📦 Agregando archivos al repositorio...
git add .

REM Crear commit
echo 💾 Creando commit...
git commit -m "feat: SIM-Pay completo - Sistema de inventario con pagos móviles para tesis doctoral - ✅ Backend Spring Boot con JWT y PostgreSQL - 📱 Frontend Next.js con TypeScript - 💳 Sistema de pagos Nequi, Daviplata y WhatsApp - 🔐 Trazabilidad inmutable con SHA-256 - 🌐 Configuración para deploy en Railway + Vercel - 📊 Dashboard completo de gestión - 🧪 Tests y documentación incluidos"

REM Verificar estado del repositorio
echo 📊 Estado del repositorio:
git status

REM Mostrar instrucciones para GitHub
echo.
echo 🎯 INSTRUCCIONES PARA SUBIR A GITHUB:
echo.
echo 1. Ve a GitHub y crea un nuevo repositorio llamado 'SIM-Pay'
echo 2. NO inicializes con README, .gitignore o licencia
echo 3. Copia la URL del repositorio (ejemplo: https://github.com/ADSOJAVIERCAMPOS/SIM-Pay.git)
echo 4. Ejecuta estos comandos:
echo.
echo    git remote set-url origin https://github.com/ADSOJAVIERCAMPOS/SIM-Pay.git
echo    git push -u origin main
echo.
echo 📱 Una vez subido, configura los deploys:
echo.
echo 🔵 RAILWAY (Backend):
echo    - Conecta tu repo de GitHub
echo    - Variables: DATABASE_URL, JWT_SECRET, CORS_ORIGINS
echo    - Build command: mvn clean install
echo    - Start command: java -jar target/simpay-0.0.1-SNAPSHOT.jar
echo.
echo ⚫ VERCEL (Frontend):
echo    - Importa proyecto desde GitHub
echo    - Root directory: frontend
echo    - Build command: npm run build  
echo    - Variables: NEXT_PUBLIC_API_URL
echo.
echo ✅ Proyecto listo para deploy!
echo.
echo 🎓 SIM-Pay - Sistema listo para tesis doctoral
echo 🇨🇴 Hecho con ❤️ en Colombia

pause