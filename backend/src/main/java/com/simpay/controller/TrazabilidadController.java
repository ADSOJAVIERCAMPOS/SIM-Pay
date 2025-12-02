package com.simpay.controller;

import com.simpay.entity.Transaccion;
import com.simpay.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/trazabilidad")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TrazabilidadController {

    @Autowired
    private TransaccionService transaccionService;

    @GetMapping("/transacciones")
    public ResponseEntity<Page<Transaccion>> getAllTransacciones(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaccion> transacciones = transaccionService.getAllTransacciones(pageable);
        
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Transaccion>> getTrazabilidadProducto(@PathVariable UUID productoId) {
        try {
            List<Transaccion> trazabilidad = transaccionService.getTrazabilidadProducto(productoId);
            return ResponseEntity.ok(trazabilidad);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<Transaccion>> getTransaccionesUsuario(
            @PathVariable UUID usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaccion> transacciones = transaccionService.getTransaccionesByUsuario(usuarioId, pageable);
        
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/verificar-integridad/{productoId}")
    public ResponseEntity<?> verificarIntegridad(@PathVariable UUID productoId) {
        try {
            boolean integro = transaccionService.verificarIntegridadTrazabilidad(productoId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("productoId", productoId);
            response.put("integridadVerificada", integro);
            response.put("mensaje", integro ? "Trazabilidad íntegra" : "Trazabilidad comprometida");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.MessageResponse("Error verificando integridad: " + e.getMessage()));
        }
    }

    @GetMapping("/reporte-ventas")
    public ResponseEntity<?> getReporteVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        try {
            Double totalVentas = transaccionService.calcularVentasPorPeriodo(fechaInicio, fechaFin);
            
            Map<String, Object> reporte = new HashMap<>();
            reporte.put("fechaInicio", fechaInicio);
            reporte.put("fechaFin", fechaFin);
            reporte.put("totalVentas", totalVentas);
            reporte.put("periodo", fechaInicio.toLocalDate() + " - " + fechaFin.toLocalDate());
            
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.MessageResponse("Error generando reporte: " + e.getMessage()));
        }
    }

    @GetMapping("/auditoria/{productoId}/hash-chain")
    public ResponseEntity<?> getHashChain(@PathVariable UUID productoId) {
        try {
            List<Transaccion> transacciones = transaccionService.getTrazabilidadProducto(productoId);
            
            Map<String, Object> hashChain = new HashMap<>();
            hashChain.put("productoId", productoId);
            hashChain.put("totalTransacciones", transacciones.size());
            hashChain.put("cadenaHash", transacciones.stream()
                    .map(t -> Map.of(
                            "id", t.getId(),
                            "hash", t.getHashTransaccion(),
                            "hashCadena", t.getHashCadena(),
                            "timestamp", t.getCreatedAt(),
                            "tipo", t.getTipo(),
                            "stockAnterior", t.getStockAnterior(),
                            "stockNuevo", t.getStockNuevo()
                    ))
                    .toList());
            
            return ResponseEntity.ok(hashChain);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.MessageResponse("Error obteniendo cadena hash: " + e.getMessage()));
        }
    }
}