package com.simpay.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {
    
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;
    
    @Value("${sendgrid.from.email}")
    private String fromEmail;
    
    @Value("${sendgrid.from.name}")
    private String fromName;
    
    @Value("${sendgrid.superadmin.email}")
    private String superadminEmail;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    /**
     * Envía notificación de nuevo dispositivo al superadmin
     */
    public void sendNewDeviceNotification(String provider, String userEmail, 
                                         String deviceInfo, String verificationCode) {
        String subject = "🔐 ALERTA: Nuevo dispositivo detectado - SIM-Pay";
        String body = buildNewDeviceEmail(provider, userEmail, deviceInfo, verificationCode);
        
        sendEmail(superadminEmail, subject, body);
        System.out.println("\n✉️ EMAIL ENVIADO A SUPERADMIN: " + superadminEmail);
    }
    
    /**
     * Envía notificación de nuevo registro al superadmin
     */
    public void sendNewUserNotification(String userEmail, String nombre, String rol) {
        String subject = "👤 NUEVO USUARIO REGISTRADO - SIM-Pay";
        String body = buildNewUserEmail(userEmail, nombre, rol);
        
        sendEmail(superadminEmail, subject, body);
        System.out.println("\n✉️ EMAIL ENVIADO A SUPERADMIN: " + superadminEmail);
    }
    
    /**
     * Envía notificación de cambio de datos al superadmin
     */
    public void sendDataChangeNotification(String entity, String action, String details) {
        String subject = "📝 CAMBIO DE DATOS - SIM-Pay";
        String body = buildDataChangeEmail(entity, action, details);
        
        sendEmail(superadminEmail, subject, body);
        System.out.println("\n✉️ EMAIL ENVIADO A SUPERADMIN: " + superadminEmail);
    }
    
    /**
     * Envía código de verificación al usuario
     */
    public void sendVerificationCode(String toEmail, String code) {
        String subject = "🔢 Código de Verificación - SIM-Pay";
        String body = buildVerificationCodeEmail(code);
        
        sendEmail(toEmail, subject, body);
        System.out.println("\n✉️ EMAIL DE VERIFICACIÓN ENVIADO A: " + toEmail);
    }
    
    /**
     * Método principal para enviar emails con SendGrid
     */
    private void sendEmail(String toEmail, String subject, String htmlBody) {
        // Si no hay API key configurada, solo mostrar en consola (modo desarrollo)
        if (sendGridApiKey == null || sendGridApiKey.trim().isEmpty()) {
            System.out.println("\n⚠️ SENDGRID_API_KEY no configurada - Modo simulación");
            System.out.println("═══════════════════════════════════════");
            System.out.println("Para: " + toEmail);
            System.out.println("Asunto: " + subject);
            System.out.println("Cuerpo:\n" + htmlBody);
            System.out.println("═══════════════════════════════════════\n");
            return;
        }
        
        try {
            Email from = new Email(fromEmail, fromName);
            Email to = new Email(toEmail);
            Content content = new Content("text/html", htmlBody);
            Mail mail = new Mail(from, subject, to, content);
            
            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            Response response = sg.api(request);
            
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("✅ Email enviado exitosamente vía SendGrid");
                System.out.println("Status Code: " + response.getStatusCode());
            } else {
                System.err.println("❌ Error al enviar email - Status: " + response.getStatusCode());
                System.err.println("Response: " + response.getBody());
            }
            
        } catch (IOException ex) {
            System.err.println("❌ Excepción al enviar email con SendGrid: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    // ========== BUILDERS DE EMAILS ==========
    
    private String buildNewDeviceEmail(String provider, String userEmail, 
                                      String deviceInfo, String verificationCode) {
        String timestamp = LocalDateTime.now().format(formatter);
        
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); 
                             color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .alert-box { background: #fff3cd; border-left: 4px solid #ffc107; 
                                padding: 15px; margin: 20px 0; }
                    .code { background: #e7f3ff; padding: 15px; font-size: 24px; 
                           text-align: center; letter-spacing: 5px; font-weight: bold; 
                           border-radius: 5px; margin: 20px 0; }
                    .info-row { padding: 10px 0; border-bottom: 1px solid #ddd; }
                    .label { font-weight: bold; color: #667eea; }
                    .footer { text-align: center; padding: 20px; color: #777; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🔐 ALERTA DE SEGURIDAD</h1>
                        <p>Nuevo dispositivo detectado</p>
                    </div>
                    <div class="content">
                        <div class="alert-box">
                            <strong>⚠️ ATENCIÓN SUPERADMIN</strong><br>
                            Se ha detectado un intento de acceso desde un nuevo dispositivo.
                        </div>
                        
                        <h3>Detalles del Intento:</h3>
                        <div class="info-row">
                            <span class="label">Proveedor:</span> %s
                        </div>
                        <div class="info-row">
                            <span class="label">Email Usuario:</span> %s
                        </div>
                        <div class="info-row">
                            <span class="label">Dispositivo:</span> %s
                        </div>
                        <div class="info-row">
                            <span class="label">Fecha/Hora:</span> %s
                        </div>
                        
                        <h3>Código de Verificación Generado:</h3>
                        <div class="code">%s</div>
                        
                        <p><strong>Acción requerida:</strong> Este código debe ser proporcionado al usuario 
                        solo después de verificar su identidad por un canal seguro.</p>
                    </div>
                    <div class="footer">
                        SIM-Pay - Sistema de Inventario Modular<br>
                        Trazabilidad Inmutable y Seguridad Empresarial<br>
                        © 2025 - Notificación automática del sistema
                    </div>
                </div>
            </body>
            </html>
            """, provider, userEmail, deviceInfo, timestamp, verificationCode);
    }
    
    private String buildNewUserEmail(String userEmail, String nombre, String rol) {
        String timestamp = LocalDateTime.now().format(formatter);
        
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #11998e 0%%, #38ef7d 100%%); 
                             color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .info-box { background: white; padding: 20px; border-radius: 5px; 
                               box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin: 20px 0; }
                    .info-row { padding: 10px 0; border-bottom: 1px solid #ddd; }
                    .label { font-weight: bold; color: #11998e; }
                    .footer { text-align: center; padding: 20px; color: #777; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>👤 NUEVO USUARIO REGISTRADO</h1>
                    </div>
                    <div class="content">
                        <p>Se ha registrado un nuevo usuario en el sistema SIM-Pay.</p>
                        
                        <div class="info-box">
                            <h3>Información del Usuario:</h3>
                            <div class="info-row">
                                <span class="label">Nombre:</span> %s
                            </div>
                            <div class="info-row">
                                <span class="label">Email:</span> %s
                            </div>
                            <div class="info-row">
                                <span class="label">Rol Asignado:</span> %s
                            </div>
                            <div class="info-row">
                                <span class="label">Fecha de Registro:</span> %s
                            </div>
                        </div>
                        
                        <p><em>Este registro ha sido almacenado en PostgreSQL con trazabilidad inmutable.</em></p>
                    </div>
                    <div class="footer">
                        SIM-Pay - Sistema de Inventario Modular<br>
                        © 2025 - Notificación automática del sistema
                    </div>
                </div>
            </body>
            </html>
            """, nombre, userEmail, rol, timestamp);
    }
    
    private String buildDataChangeEmail(String entity, String action, String details) {
        String timestamp = LocalDateTime.now().format(formatter);
        
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #f093fb 0%%, #f5576c 100%%); 
                             color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .change-box { background: white; padding: 20px; border-radius: 5px; 
                                 border-left: 4px solid #f5576c; margin: 20px 0; }
                    .info-row { padding: 10px 0; border-bottom: 1px solid #ddd; }
                    .label { font-weight: bold; color: #f5576c; }
                    .details { background: #fff9e6; padding: 15px; border-radius: 5px; 
                              margin-top: 15px; font-family: monospace; }
                    .footer { text-align: center; padding: 20px; color: #777; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>📝 CAMBIO DE DATOS</h1>
                    </div>
                    <div class="content">
                        <p>Se ha registrado una modificación en el sistema.</p>
                        
                        <div class="change-box">
                            <div class="info-row">
                                <span class="label">Entidad Afectada:</span> %s
                            </div>
                            <div class="info-row">
                                <span class="label">Acción Realizada:</span> %s
                            </div>
                            <div class="info-row">
                                <span class="label">Fecha/Hora:</span> %s
                            </div>
                            
                            <div class="details">
                                <strong>Detalles:</strong><br>
                                %s
                            </div>
                        </div>
                        
                        <p><em>Todos los cambios quedan registrados con hash SHA-256 para trazabilidad inmutable.</em></p>
                    </div>
                    <div class="footer">
                        SIM-Pay - Sistema de Inventario Modular<br>
                        © 2025 - Auditoría automática del sistema
                    </div>
                </div>
            </body>
            </html>
            """, entity, action, timestamp, details);
    }
    
    private String buildVerificationCodeEmail(String code) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); 
                             color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; text-align: center; }
                    .code { background: #667eea; color: white; padding: 20px; font-size: 32px; 
                           text-align: center; letter-spacing: 8px; font-weight: bold; 
                           border-radius: 10px; margin: 30px 0; box-shadow: 0 4px 6px rgba(0,0,0,0.2); }
                    .warning { background: #fff3cd; padding: 15px; border-radius: 5px; 
                              margin: 20px 0; border-left: 4px solid #ffc107; }
                    .footer { text-align: center; padding: 20px; color: #777; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🔢 Código de Verificación</h1>
                        <p>SIM-Pay Sistema</p>
                    </div>
                    <div class="content">
                        <p>Usa el siguiente código para completar tu verificación:</p>
                        
                        <div class="code">%s</div>
                        
                        <div class="warning">
                            <strong>⚠️ Importante:</strong><br>
                            • Este código expira en 10 minutos<br>
                            • No lo compartas con nadie<br>
                            • Si no solicitaste este código, ignora este mensaje
                        </div>
                        
                        <p>Si necesitas ayuda, contacta al administrador.</p>
                    </div>
                    <div class="footer">
                        SIM-Pay - Sistema de Inventario Modular<br>
                        © 2025 - Código generado automáticamente
                    </div>
                </div>
            </body>
            </html>
            """, code);
    }
}
