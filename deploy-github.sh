#!/bin/bash

# 🚀 SIM-Pay Deploy Script
# Script para subir el proyecto completo a GitHub

echo "🚀 Iniciando deploy de SIM-Pay a GitHub..."

# Verificar que estamos en el directorio correcto
if [ ! -f "package.json" ] && [ ! -f "pom.xml" ]; then
    echo "❌ Error: No se encontró package.json ni pom.xml. Asegúrate de estar en el directorio raíz del proyecto."
    exit 1
fi

# Limpiar archivos temporales
echo "🧹 Limpiando archivos temporales..."
find . -name ".next" -type d -exec rm -rf {} + 2>/dev/null
find . -name "target" -type d -exec rm -rf {} + 2>/dev/null
find . -name "node_modules" -type d -exec rm -rf {} + 2>/dev/null

# Agregar todos los archivos al staging
echo "📦 Agregando archivos al repositorio..."
git add .

# Crear commit
echo "💾 Creando commit..."
git commit -m "feat: SIM-Pay completo - Sistema de inventario con pagos móviles para tesis doctoral

- ✅ Backend Spring Boot con JWT y PostgreSQL
- 📱 Frontend Next.js con TypeScript
- 💳 Sistema de pagos Nequi, Daviplata y WhatsApp
- 🔐 Trazabilidad inmutable con SHA-256
- 🌐 Configuración para deploy en Railway + Vercel
- 📊 Dashboard completo de gestión
- 🧪 Tests y documentación incluidos"

# Verificar estado del repositorio
echo "📊 Estado del repositorio:"
git status

# Mostrar instrucciones para GitHub
echo ""
echo "🎯 INSTRUCCIONES PARA SUBIR A GITHUB:"
echo ""
echo "1. Ve a GitHub y crea un nuevo repositorio llamado 'SIM-Pay'"
echo "2. NO inicialices con README, .gitignore o licencia"
echo "3. Copia la URL del repositorio (ejemplo: https://github.com/ADSOJAVIERCAMPOS/SIM-Pay.git)"
echo "4. Ejecuta estos comandos:"
echo ""
echo "   git remote set-url origin https://github.com/ADSOJAVIERCAMPOS/SIM-Pay.git"
echo "   git push -u origin main"
echo ""
echo "📱 Una vez subido, configura los deploys:"
echo ""
echo "🔵 RAILWAY (Backend):"
echo "   - Conecta tu repo de GitHub"
echo "   - Variables: DATABASE_URL, JWT_SECRET, CORS_ORIGINS"
echo "   - Build command: mvn clean install"
echo "   - Start command: java -jar target/simpay-0.0.1-SNAPSHOT.jar"
echo ""
echo "⚫ VERCEL (Frontend):"
echo "   - Importa proyecto desde GitHub"
echo "   - Root directory: frontend"
echo "   - Build command: npm run build"
echo "   - Variables: NEXT_PUBLIC_API_URL"
echo ""
echo "✅ Proyecto listo para deploy!"

# Mostrar resumen de archivos
echo ""
echo "📋 RESUMEN DEL PROYECTO:"
echo "- $(find . -name "*.java" | wc -l) archivos Java"
echo "- $(find . -name "*.tsx" -o -name "*.ts" | wc -l) archivos TypeScript/React"
echo "- $(find . -name "*.json" | wc -l) archivos de configuración"
echo "- $(du -sh . | cut -f1) tamaño total del proyecto"
echo ""
echo "🎓 SIM-Pay - Sistema listo para tesis doctoral"
echo "🇨🇴 Hecho con ❤️ en Colombia"