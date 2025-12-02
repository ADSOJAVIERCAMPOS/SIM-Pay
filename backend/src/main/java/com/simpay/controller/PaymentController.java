package com.simpay.controller;

import com.simpay.dto.PaymentRequest;
import com.simpay.dto.PaymentResponse;
import com.simpay.dto.WhatsAppPaymentRequest;
import com.simpay.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Generar link de pago para Nequi
     */
    @PostMapping("/nequi/generate")
    public ResponseEntity<PaymentResponse> generateNequiPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.generateNequiPayment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Generar link de pago para Daviplata
     */
    @PostMapping("/daviplata/generate")
    public ResponseEntity<PaymentResponse> generateDaviplataPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.generateDaviplataPayment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Generar y enviar pago por WhatsApp
     */
    @PostMapping("/whatsapp/send")
    public ResponseEntity<PaymentResponse> sendWhatsAppPayment(@RequestBody WhatsAppPaymentRequest request) {
        PaymentResponse response = paymentService.sendWhatsAppPayment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Confirmar pago manualmente
     */
    @PostMapping("/{paymentId}/confirm")
    public ResponseEntity<String> confirmPayment(
            @PathVariable UUID paymentId,
            @RequestParam String confirmationCode) {
        paymentService.confirmPayment(paymentId, confirmationCode);
        return ResponseEntity.ok("Pago confirmado exitosamente");
    }

    /**
     * Obtener estado de pago
     */
    @GetMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponse> getPaymentStatus(@PathVariable UUID paymentId) {
        PaymentResponse response = paymentService.getPaymentStatus(paymentId);
        return ResponseEntity.ok(response);
    }

    /**
     * Listar todos los pagos
     */
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    /**
     * Webhook para confirmaciones automáticas (futuro)
     */
    @PostMapping("/webhook/confirm")
    public ResponseEntity<String> webhookConfirmation(@RequestBody String payload) {
        // Implementación futura para webhooks de Nequi/Daviplata
        return ResponseEntity.ok("Webhook recibido");
    }
}