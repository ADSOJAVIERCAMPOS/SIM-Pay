package com.simpay.controller;

import com.simpay.dto.VentaRequest;
import com.simpay.entity.Transaccion;
import com.simpay.entity.Usuario;
import com.simpay.service.AuthService;
import com.simpay.service.PaymentService;
import com.simpay.service.TransaccionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/ventas")

public class VentaController {

    @Autowired
    private TransaccionService transaccionService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<?> procesarVenta(
            @Valid @RequestBody VentaRequest ventaRequest,
            @RequestHeader("Authorization") String token) {
        try {
            // Obtener usuario del token
            String jwtToken = token.replace("Bearer ", "");
            Usuario usuario = authService.getUserFromToken(jwtToken);

            // Procesar venta
            Transaccion transaccion = transaccionService.procesarVenta(ventaRequest, usuario);

            // Generar links de pago
            String merchantId = "SIM-Pay";
            String nequiLink = paymentService.generateNequiLink(transaccion, merchantId);
            String daviplataLink = paymentService.generateDaviplataLink(transaccion, merchantId);

            // Generar mensaje de WhatsApp
            String mensajeWhatsApp = paymentService.generateWhatsAppMessage(transaccion, nequiLink, daviplataLink);
            String whatsAppLink = null;

            if (ventaRequest.getNumeroCliente() != null && !ventaRequest.getNumeroCliente().trim().isEmpty()) {
                whatsAppLink = paymentService.generateWhatsAppLink(ventaRequest.getNumeroCliente(), mensajeWhatsApp);
            }

            // Respuesta completa
            Map<String, Object> response = new HashMap<>();
            response.put("transaccion", transaccion);
            response.put("nequiLink", nequiLink);
            response.put("daviplataLink", daviplataLink);
            response.put("whatsAppLink", whatsAppLink);
            response.put("mensajeWhatsApp", mensajeWhatsApp);
            response.put("referencia", transaccion.getId().toString().substring(0, 8).toUpperCase());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.MessageResponse("Error procesando venta: " + e.getMessage()));
        }
    }

    @PostMapping("/{transaccionId}/confirmar-pago")
    public ResponseEntity<?> confirmarPago(
            @PathVariable UUID transaccionId,
            @RequestBody Map<String, String> payload,
            @RequestHeader("Authorization") String token) {
        try {
            String referenciaPago = payload.get("referenciaPago");

            if (referenciaPago == null || referenciaPago.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new AuthController.MessageResponse("Referencia de pago requerida"));
            }

            // Validar referencia
            if (!paymentService.validarReferenciaPago(referenciaPago)) {
                return ResponseEntity.badRequest()
                        .body(new AuthController.MessageResponse("Referencia de pago inválida"));
            }

            Transaccion transaccion = transaccionService.confirmarPago(transaccionId, referenciaPago);

            Map<String, Object> response = new HashMap<>();
            response.put("transaccion", transaccion);
            response.put("mensaje", "Pago confirmado exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.MessageResponse("Error confirmando pago: " + e.getMessage()));
        }
    }

    @PostMapping("/{transaccionId}/cancelar")
    public ResponseEntity<?> cancelarTransaccion(
            @PathVariable UUID transaccionId,
            @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            Usuario usuario = authService.getUserFromToken(jwtToken);

            Transaccion transaccion = transaccionService.cancelarTransaccion(transaccionId, usuario);

            return ResponseEntity.ok(transaccion);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.MessageResponse("Error cancelando transacción: " + e.getMessage()));
        }
    }

    @GetMapping("/generar-whatsapp")
    public ResponseEntity<?> generarWhatsApp(
            @RequestParam String numero,
            @RequestParam String mensaje) {
        try {
            String whatsAppLink = paymentService.generateWhatsAppLink(numero, mensaje);

            Map<String, String> response = new HashMap<>();
            response.put("whatsAppLink", whatsAppLink);
            response.put("mensaje", "Link generado exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.MessageResponse("Error generando link: " + e.getMessage()));
        }
    }
}