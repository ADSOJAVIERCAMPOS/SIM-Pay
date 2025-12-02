package com.simpay.service;

import com.simpay.entity.Transaccion;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class PaymentService {

    private static final String NEQUI_BASE_URL = "https://nequi.com.co/pagar";
    private static final String DAVIPLATA_BASE_URL = "https://daviplata.com/pagar";
    private static final String WHATSAPP_BASE_URL = "https://wa.me/57";
    
    public String generateNequiLink(Transaccion transaccion, String merchantId) {
        try {
            String amount = formatAmount(transaccion.getTotal());
            String reference = transaccion.getId().toString().substring(0, 8).toUpperCase();
            
            return String.format("%s?amount=%s&merchant=%s&reference=%s",
                    NEQUI_BASE_URL,
                    URLEncoder.encode(amount, StandardCharsets.UTF_8),
                    URLEncoder.encode(merchantId, StandardCharsets.UTF_8),
                    URLEncoder.encode(reference, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Error generando link de Nequi", e);
        }
    }
    
    public String generateDaviplataLink(Transaccion transaccion, String merchantId) {
        try {
            String amount = formatAmount(transaccion.getTotal());
            String reference = transaccion.getId().toString().substring(0, 8).toUpperCase();
            
            return String.format("%s?amount=%s&merchant=%s&reference=%s",
                    DAVIPLATA_BASE_URL,
                    URLEncoder.encode(amount, StandardCharsets.UTF_8),
                    URLEncoder.encode(merchantId, StandardCharsets.UTF_8),
                    URLEncoder.encode(reference, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Error generando link de Daviplata", e);
        }
    }
    
    public String generateWhatsAppMessage(Transaccion transaccion, String nequiLink, String daviplataLink) {
        String amount = formatCurrency(transaccion.getTotal());
        String reference = transaccion.getId().toString().substring(0, 8).toUpperCase();
        
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("¡Hola! Tu compra en SIM-Pay:\n\n");
        mensaje.append("💰 *Total a pagar:* ").append(amount).append("\n");
        mensaje.append("🔢 *Referencia:* ").append(reference).append("\n\n");
        mensaje.append("📱 *Opciones de pago:*\n");
        mensaje.append("• Nequi: ").append(nequiLink).append("\n");
        mensaje.append("• Daviplata: ").append(daviplataLink).append("\n\n");
        mensaje.append("✅ Una vez realices el pago, por favor envía el comprobante.\n\n");
        mensaje.append("*Gracias por tu compra con SIM-Pay* 🚀");
        
        return mensaje.toString();
    }
    
    public String generateWhatsAppLink(String phoneNumber, String message) {
        try {
            // Limpiar número de teléfono (solo dígitos)
            String cleanPhone = phoneNumber.replaceAll("[^0-9]", "");
            
            // Asegurar que tenga el código de país de Colombia (57)
            if (!cleanPhone.startsWith("57")) {
                cleanPhone = "57" + cleanPhone;
            }
            
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
            
            return String.format("%s%s?text=%s", WHATSAPP_BASE_URL, cleanPhone, encodedMessage);
        } catch (Exception e) {
            throw new RuntimeException("Error generando link de WhatsApp", e);
        }
    }
    
    private String formatAmount(BigDecimal amount) {
        return amount.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }
    
    private String formatCurrency(BigDecimal amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        return formatter.format(amount);
    }
    
    // Validar referencia de pago (formato básico)
    public boolean validarReferenciaPago(String referencia) {
        return referencia != null && 
               referencia.length() >= 6 && 
               referencia.matches("[A-Za-z0-9]+");
    }
    
    // Simular verificación de pago (en producción sería una API real)
    public boolean verificarPago(String metodoPago, String referencia, BigDecimal monto) {
        // Simulación: cualquier referencia válida se considera como pago exitoso
        return validarReferenciaPago(referencia);
    }
}