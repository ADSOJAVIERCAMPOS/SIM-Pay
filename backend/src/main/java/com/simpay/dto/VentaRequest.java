package com.simpay.dto;

import com.simpay.entity.Transaccion;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class VentaRequest {
    
    @NotNull(message = "El ID del producto es obligatorio")
    private UUID productoId;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor que 0")
    private Integer cantidad;
    
    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    private BigDecimal precioUnitario;
    
    private String metodoPago;
    
    private String numeroCliente; // Para WhatsApp
    
    // Constructors
    public VentaRequest() {}
    
    public VentaRequest(UUID productoId, Integer cantidad, BigDecimal precioUnitario) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    // Getters and Setters
    public UUID getProductoId() { return productoId; }
    public void setProductoId(UUID productoId) { this.productoId = productoId; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    
    public String getNumeroCliente() { return numeroCliente; }
    public void setNumeroCliente(String numeroCliente) { this.numeroCliente = numeroCliente; }
}

// DTO para respuesta de venta
class VentaResponse {
    
    private UUID transaccionId;
    private BigDecimal total;
    private String hashTransaccion;
    private Transaccion.EstadoTransaccion estado;
    private String linkPago;
    private String mensajeWhatsApp;
    private LocalDateTime createdAt;
    
    public VentaResponse() {}
    
    public VentaResponse(UUID transaccionId, BigDecimal total, String hashTransaccion, 
                        Transaccion.EstadoTransaccion estado, String linkPago, String mensajeWhatsApp) {
        this.transaccionId = transaccionId;
        this.total = total;
        this.hashTransaccion = hashTransaccion;
        this.estado = estado;
        this.linkPago = linkPago;
        this.mensajeWhatsApp = mensajeWhatsApp;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public UUID getTransaccionId() { return transaccionId; }
    public void setTransaccionId(UUID transaccionId) { this.transaccionId = transaccionId; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public String getHashTransaccion() { return hashTransaccion; }
    public void setHashTransaccion(String hashTransaccion) { this.hashTransaccion = hashTransaccion; }
    
    public Transaccion.EstadoTransaccion getEstado() { return estado; }
    public void setEstado(Transaccion.EstadoTransaccion estado) { this.estado = estado; }
    
    public String getLinkPago() { return linkPago; }
    public void setLinkPago(String linkPago) { this.linkPago = linkPago; }
    
    public String getMensajeWhatsApp() { return mensajeWhatsApp; }
    public void setMensajeWhatsApp(String mensajeWhatsApp) { this.mensajeWhatsApp = mensajeWhatsApp; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}