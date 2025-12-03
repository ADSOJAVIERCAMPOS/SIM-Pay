package com.simpay.service;

import java.time.format.DateTimeFormatter;


import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private static final String SUPERADMIN_EMAIL = "ject2583@gmail.com";
    
    /**
     * Envía notificación de nuevo dispositivo al superadmin
     */
    public void sendNewDeviceNotification(String provider, String userEmail, 
                                         String deviceInfo, String verificationCode) {
        String subject = "🔐 ALERTA: Nuevo dispositivo detectado - SIM-Pay";
        String body = buildNewDeviceEmail(provider, userEmail, deviceInfo, verificationCode);
        
        sendEmail(SUPERADMIN_EMAIL, subject, body);
        System.out.println("\n✉️ EMAIL ENVIADO A SUPERADMIN: " + SUPERADMIN_EMAIL);
    }
    
    /**
     * Envía notificación de nuevo registro al superadmin
     */
    public void sendNewUserNotification(String userEmail, String nombre, String rol) {
        String subject = "👤 NUEVO USUARIO REGISTRADO - SIM-Pay";
        String body = buildNewUserEmail(userEmail, nombre, rol);
        
        sendEmail(SUPERADMIN_EMAIL, subject, body);
        System.out.println("\n✉️ EMAIL ENVIADO A SUPERADMIN: " + SUPERADMIN_EMAIL);
    }
    
    /**
     * Envía notificación de cambio de datos al superadmin
     */
    public void sendDataChangeNotification(String entity, String action, String details) {
        String subject = "📝 CAMBIO DE DATOS - SIM-Pay";
        String body = buildDataChangeEmail(entity, action, details);
        
        sendEmail(SUPERADMIN_EMAIL, subject, body);
        System.out.println("\n✉️ EMAIL ENVIADO A SUPERADMIN: " + SUPERADMIN_EMAIL);
    }
    
    /**
     * Envía código de verificación al usuario
     */
    public void sendVerificationCode(String userEmail, String code) {
        String subject = "🔑 Código de Verificación - SIM-Pay";
        String body = buildVerificationEmail(code);
        
        sendEmail(userEmail, subject, body);
        
        // Copia al superadmin
        sendEmail(SUPERADMIN_EMAIL, "[COPIA] " + subject + " - Usuario: " + userEmail, body);
    }
    
    // ========== BUILDERS DE EMAILS ==========
    
    private String buildNewDeviceEmail(String provider, String userEmail, 
                                      String deviceInfo, String code) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("""
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            🔐 ALERTA DE SEGURIDAD - NUEVO DISPOSITIVO
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            
            Proveedor: %s
            Usuario: %s
            Dispositivo: %s
            Fecha/Hora: %s
            Código Verificación: %s
            
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            Sistema SIM-Pay - Notificación Superadmin
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            """, 
            provider.toUpperCase(), 
            userEmail != null ? userEmail : "N/A",
            deviceInfo,
            LocalDateTime.now().format(formatter),
            code
        );
    }
    
    private String buildNewUserEmail(String email, String nombre, String rol) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("""
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            👤 NUEVO USUARIO REGISTRADO
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            
            Email: %s
            Nombre: %s
            Rol: %s
            Fecha/Hora: %s
            
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            Sistema SIM-Pay - Notificación Superadmin
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            """,
            email, nombre, rol,
            LocalDateTime.now().format(formatter)
        );
    }
    
    private String buildDataChangeEmail(String entity, String action, String details) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("""
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            📝 CAMBIO DE DATOS REGISTRADO
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            
            Entidad: %s
            Acción: %s
            Detalles: %s
            Fecha/Hora: %s
            
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            Sistema SIM-Pay - Notificación Superadmin
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            """,
            entity, action, details,
            LocalDateTime.now().format(formatter)
        );
    }
    
    private String buildVerificationEmail(String code) {
        return String.format("""
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            🔑 CÓDIGO DE VERIFICACIÓN
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            
            Tu código de verificación es:
            
                    %s
            
            Este código expira en 5 minutos.
            Si no solicitaste este código, ignora este mensaje.
            
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            Sistema SIM-Pay - Seguridad
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            """,
            code
        );
    }
    
    // ========== MÉTODO DE ENVÍO ==========
    
    private void sendEmail(String to, String subject, String body) {
        // TODO: Integrar con servicio real de email (SendGrid, AWS SES, SMTP, etc.)
        // Por ahora, simulación en consola
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📧 EMAIL SIMULADO");
        System.out.println("=".repeat(60));
        System.out.println("Para: " + to);
        System.out.println("Asunto: " + subject);
        System.out.println("-".repeat(60));
        System.out.println(body);
        System.out.println("=".repeat(60) + "\n");
        
        /*
         * EJEMPLO DE INTEGRACIÓN REAL CON SENDGRID:
         * 
         * SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
         * Email from = new Email("noreply@simpay.com");
         * Email toEmail = new Email(to);
         * Content content = new Content("text/plain", body);
         * Mail mail = new Mail(from, subject, toEmail, content);
         * 
         * Request request = new Request();
         * request.setMethod(Method.POST);
         * request.setEndpoint("mail/send");
         * request.setBody(mail.build());
         * sg.api(request);
         */
    }
}
