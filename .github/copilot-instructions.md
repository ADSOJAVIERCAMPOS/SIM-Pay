<!-- SIM-Pay: Sistema de Inventario Modular con Pasarela de Pagos Móviles -->
<!-- Proyecto de Tesis Doctoral - Arquitectura Robusta con Trazabilidad Inmutable -->

## 🎯 Proyecto SIM-Pay - Especificaciones para Copilot

### Descripción del Proyecto
Sistema de inventario empresarial con trazabilidad inmutable y pasarela de pagos móviles para tesis doctoral. Enfoque en robustez, escalabilidad y cumplimiento legal en Colombia.

### Arquitectura del Sistema
- **Backend**: Java 17 + Spring Boot + Spring Security + JPA
- **Frontend**: Next.js + React + TypeScript + Tailwind CSS
- **Base de Datos**: PostgreSQL con esquemas de auditoría
- **Contenedores**: Docker + Docker Compose
- **Despliegue**: Railway/Render (backend) + Vercel (frontend)

### Componentes Clave para la Tesis

#### 1. Algoritmo de Trazabilidad Inmutable
- Hash SHA-256 para cada transacción de inventario
- Registro de auditoría con timestamp y usuario responsable
- Cadena de custody para cambios de stock

#### 2. Integración de Pagos Móviles
- Generación de links de cobro para Nequi/Daviplata
- Integración con WhatsApp para envío automatizado
- Confirmación manual auditada de transacciones

#### 3. Seguridad Empresarial
- Autenticación JWT con refresh tokens
- Encriptación de datos sensibles
- Logs de seguridad y acceso

### Instrucciones Específicas para Copilot

#### Backend (Java/Spring Boot)
- Usar anotaciones Spring Boot modernas (@RestController, @Service, @Entity)
- Implementar DTOs para todas las transferencias de datos
- Configurar validación con Bean Validation
- Crear interceptores para logging de transacciones
- Implementar cache con Redis (simulado en desarrollo)

#### Frontend (Next.js)
- Usar TypeScript estricto con interfaces completas
- Implementar diseño mobile-first responsive
- Usar React Query para manejo de estado del servidor
- Crear components reutilizables con Storybook
- Implementar PWA para experiencia de aplicación móvil

#### Base de Datos
- Diseñar esquemas normalizados con relaciones apropiadas
- Crear índices para consultas frecuentes de inventario
- Implementar triggers para auditoría automática
- Usar UUID para identificadores únicos de transacciones

#### Docker y Despliegue
- Multi-stage builds para optimización de imágenes
- Docker Compose para desarrollo local completo
- Variables de entorno para configuración por ambiente
- Health checks para monitoreo de servicios

### Patrones de Desarrollo
- Clean Architecture con separación de capas
- Repository Pattern para acceso a datos
- Factory Pattern para generación de hash de trazabilidad
- Observer Pattern para notificaciones de cambios

### Consideraciones Legales (Colombia)
- Cumplir con Ley 1581 (Protección de Datos Personales)
- Implementar retención de logs según normativa contable
- Generar reportes de trazabilidad para auditorías
- Preparar documentación técnica para solicitud de patente SIC

### Métricas de Calidad para Tesis
- Cobertura de pruebas > 80%
- Tiempo de respuesta API < 200ms
- Documentación técnica completa con diagramas
- Análisis de rendimiento y escalabilidad

## ✅ Checklist de Implementación

- [x] Verificar copilot-instructions.md
- [ ] Clarificar Project Requirements  
- [ ] Scaffold the Project
- [ ] Customize the Project
- [ ] Install Required Extensions
- [ ] Compile the Project  
- [ ] Create and Run Task
- [ ] Launch the Project
- [ ] Ensure Documentation is Complete