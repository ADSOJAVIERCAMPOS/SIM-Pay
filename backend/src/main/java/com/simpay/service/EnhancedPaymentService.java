package com.simpay.service;

import com.simpay.dto.PaymentRequest;
import com.simpay.dto.PaymentResponse;
import com.simpay.dto.WhatsAppPaymentRequest;
import com.simpay.entity.Transaccion;
import com.simpay.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    // Configuración de merchants (en producción esto sería configurable)
    private static final String NEQUI_MERCHANT_ID = "SIMPAY001";
    private static final String DAVIPLATA_MERCHANT_ID = "SIMPAY002";
    
    // Almacén temporal de pagos (en producción sería una base de datos)
    private Map<UUID, PaymentResponse> payments = new HashMap<>();
    
    /**
     * Genera un pago para Nequi con trazabilidad completa
     */
    public PaymentResponse generateNequiPayment(PaymentRequest request) {
        UUID paymentId = UUID.randomUUID();
        String paymentUrl = generateNequiPaymentLink(request);
        String confirmationCode = generateConfirmationCode();
        
        PaymentResponse response = new PaymentResponse(
            paymentId, request.getTransaccionId(), "NEQUI", 
            request.getAmount(), "PENDING", paymentUrl
        );
        
        response.setConfirmationCode(confirmationCode);
        response.setCurrency("COP");
        response.setExpiresAt(LocalDateTime.now().plusHours(24));
        response.setQrCode(generateQRCode(paymentUrl));
        
        payments.put(paymentId, response);
        
        return response;
    }
    
    /**
     * Genera un pago para Daviplata con trazabilidad completa
     */
    public PaymentResponse generateDaviplataPayment(PaymentRequest request) {
        UUID paymentId = UUID.randomUUID();
        String paymentUrl = generateDaviplataPaymentLink(request);
        String confirmationCode = generateConfirmationCode();
        
        PaymentResponse response = new PaymentResponse(
            paymentId, request.getTransaccionId(), "DAVIPLATA", 
            request.getAmount(), "PENDING", paymentUrl
        );
        
        response.setConfirmationCode(confirmationCode);
        response.setCurrency("COP");
        response.setExpiresAt(LocalDateTime.now().plusHours(24));
        response.setQrCode(generateQRCode(paymentUrl));
        
        payments.put(paymentId, response);
        
        return response;
    }
    
    /**
     * Genera y envía pago por WhatsApp
     */
    public PaymentResponse sendWhatsAppPayment(WhatsAppPaymentRequest request) {
        // Generar pago según método seleccionado
        PaymentRequest paymentReq = new PaymentRequest(
            request.getTransaccionId(), request.getAmount(), request.getCurrency(),
            request.getDescription(), request.getCustomerName(), request.getCustomerPhone()
        );
        
        PaymentResponse payment = request.getPaymentMethod().equals("NEQUI") ?
            generateNequiPayment(paymentReq) :
            generateDaviplataPayment(paymentReq);
        
        // Generar mensaje de WhatsApp
        String whatsappMessage = generateWhatsAppMessage(request, payment);
        String whatsappUrl = generateWhatsAppUrl(request.getCustomerPhone(), whatsappMessage);
        
        payment.setWhatsappMessage(whatsappMessage);
        payment.setPaymentUrl(whatsappUrl); // URL de WhatsApp en lugar del pago directo
        
        return payment;
    }
    
    /**
     * Confirma un pago manualmente con auditoría
     */
    public void confirmPayment(UUID paymentId, String confirmationCode) {
        PaymentResponse payment = payments.get(paymentId);
        
        if (payment == null) {
            throw new RuntimeException("Pago no encontrado");
        }
        
        if (!payment.getConfirmationCode().equals(confirmationCode)) {
            throw new RuntimeException("Código de confirmación inválido");
        }
        
        if (LocalDateTime.now().isAfter(payment.getExpiresAt())) {
            throw new RuntimeException("Pago expirado");
        }
        
        payment.setStatus("CONFIRMED");
        payment.setConfirmedAt(LocalDateTime.now());
        
        // Actualizar transacción asociada
        updateTransactionStatus(payment.getTransaccionId(), "PAGADO", payment.getPaymentMethod());
    }
    
    /**
     * Obtiene el estado de un pago
     */
    public PaymentResponse getPaymentStatus(UUID paymentId) {
        PaymentResponse payment = payments.get(paymentId);
        if (payment == null) {
            throw new RuntimeException("Pago no encontrado");
        }
        return payment;
    }
    
    /**
     * Lista todos los pagos
     */
    public List<PaymentResponse> getAllPayments() {
        return new ArrayList<>(payments.values())
            .stream()
            .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
            .collect(Collectors.toList());
    }
    
    // =============== MÉTODOS DE COMPATIBILIDAD ===============
    
    public String generateNequiLink(Transaccion transaccion, String merchantId) {
        PaymentRequest request = new PaymentRequest(
            transaccion.getId(), transaccion.getTotal(), "COP",
            transaccion.getProducto().getNombre(), "Cliente", "3001234567"
        );
        return generateNequiPaymentLink(request);
    }
    
    public String generateDaviplataLink(Transaccion transaccion, String merchantId) {
        PaymentRequest request = new PaymentRequest(
            transaccion.getId(), transaccion.getTotal(), "COP",
            transaccion.getProducto().getNombre(), "Cliente", "3001234567"
        );
        return generateDaviplataPaymentLink(request);
    }
    
    public String generateWhatsAppMessage(Transaccion transaccion, String phoneNumber, String paymentMethod) {
        WhatsAppPaymentRequest request = new WhatsAppPaymentRequest(
            transaccion.getId(), transaccion.getTotal(), phoneNumber,
            paymentMethod, transaccion.getProducto().getNombre()
        );
        request.setCustomerName("Cliente");
        
        PaymentResponse payment = sendWhatsAppPayment(request);
        return payment.getWhatsappMessage();
    }
    
    // =============== MÉTODOS PRIVADOS ===============
    
    /**
     * Genera un link de pago para Nequi
     */
    private String generateNequiPaymentLink(PaymentRequest request) {
        String amount = formatCurrency(request.getAmount());
        String description = encodeDescription(request.getDescription());
        String reference = request.getTransaccionId().toString();
        
        return String.format(
            "https://nequi.com.co/pay?merchant_id=%s&amount=%s&description=%s&reference=%s",
            NEQUI_MERCHANT_ID, amount, description, reference
        );
    }
    
    /**
     * Genera un link de pago para Daviplata
     */
    private String generateDaviplataPaymentLink(PaymentRequest request) {
        String amount = formatCurrency(request.getAmount());
        String description = encodeDescription(request.getDescription());
        String reference = request.getTransaccionId().toString();
        
        return String.format(
            "https://daviplata.com/pay?merchant=%s&value=%s&desc=%s&ref=%s",
            DAVIPLATA_MERCHANT_ID, amount, description, reference
        );
    }
    
    /**
     * Genera un mensaje de WhatsApp profesional
     */
    private String generateWhatsAppMessage(WhatsAppPaymentRequest request, PaymentResponse payment) {
        String amount = formatCurrency(request.getAmount());
        String paymentMethod = request.getPaymentMethod();
        
        return String.format(
            "🛒 *SIM-Pay - Solicitud de Pago*\n\n" +
            "👤 Cliente: %s\n" +
            "📦 Producto: %s\n" +
            "💰 Total: %s\n" +
            "🏦 Método: %s\n" +
            "🔢 Código: %s\n\n" +
            "👆 *Link de pago:*\n%s\n\n" +
            "⏰ *Válido hasta:* %s\n\n" +
            "✅ Envía el comprobante con el código *%s* para confirmar.\n\n" +
            "_🔐 Powered by SIM-Pay - Trazabilidad Inmutable_",
            request.getCustomerName(),
            request.getDescription(),
            amount,
            paymentMethod,
            payment.getConfirmationCode(),
            generateNequiPaymentLink(new PaymentRequest(request.getTransaccionId(), request.getAmount(), "COP", request.getDescription(), request.getCustomerName(), request.getCustomerPhone())),
            payment.getExpiresAt().toString().substring(0, 16),
            payment.getConfirmationCode()
        );
    }
    
    /**
     * Genera URL de WhatsApp con mensaje prellenado
     */
    private String generateWhatsAppUrl(String phoneNumber, String message) {
        String cleanPhone = phoneNumber.replaceAll("[^0-9]", "");
        
        if (!cleanPhone.startsWith("57") && cleanPhone.length() == 10) {
            cleanPhone = "57" + cleanPhone;
        }
        
        String encodedMessage = message.replaceAll("\\s", "%20")
                                     .replaceAll("\\n", "%0A")
                                     .replaceAll("\\*", "%2A")
                                     .replaceAll("_", "%5F");
        
        return String.format("https://wa.me/%s?text=%s", cleanPhone, encodedMessage);
    }
    
    /**
     * Genera código de confirmación único
     */
    private String generateConfirmationCode() {
        return String.format("SP%06d", new Random().nextInt(1000000));
    }
    
    /**
     * Genera código QR para el pago (simulado)
     */
    private String generateQRCode(String data) {
        return String.format("data:image/png;base64,QR_CODE_FOR_%s", 
                           Base64.getEncoder().encodeToString(data.getBytes()));
    }
    
    /**
     * Actualiza el estado de la transacción
     */
    private void updateTransactionStatus(UUID transaccionId, String status, String paymentMethod) {
        transaccionRepository.findById(transaccionId).ifPresent(transaccion -> {
            transaccion.setEstado(Transaccion.EstadoTransaccion.valueOf(status));
            transaccion.setMetodoPago(paymentMethod);
            transaccion.setReferenciaPago("CONF_" + generateConfirmationCode());
            transaccionRepository.save(transaccion);
        });
    }
    
    /**
     * Formatea el monto como moneda colombiana
     */
    private String formatCurrency(BigDecimal amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        return formatter.format(amount).replace("COP", "").trim();
    }
    
    /**
     * Codifica la descripción para URLs
     */
    private String encodeDescription(String description) {
        return description.replaceAll("\\s+", "%20")
                         .replaceAll("[^a-zA-Z0-9%]", "");
    }
}