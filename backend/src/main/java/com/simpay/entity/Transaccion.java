package com.simpay.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transacciones")
@EntityListeners(AuditingEntityListener.class)
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransaccion tipo;

    @NotNull
    @Column(name = "producto_id", nullable = false)
    private UUID productoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Producto producto;

    @NotNull
    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Min(1)
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    @Column(name = "stock_anterior", nullable = false)
    private Integer stockAnterior;

    @Column(name = "stock_nuevo", nullable = false)
    private Integer stockNuevo;

    @Column(name = "hash_transaccion", length = 64, nullable = false)
    private String hashTransaccion;

    @Column(name = "hash_cadena", length = 64)
    private String hashCadena;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTransaccion estado = EstadoTransaccion.PENDIENTE;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @Column(name = "referencia_pago")
    private String referenciaPago;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public Transaccion() {}

    public Transaccion(TipoTransaccion tipo, UUID productoId, UUID usuarioId, 
                      Integer cantidad, BigDecimal precioUnitario, 
                      Integer stockAnterior, Integer stockNuevo) {
        this.tipo = tipo;
        this.productoId = productoId;
        this.usuarioId = usuarioId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        this.stockAnterior = stockAnterior;
        this.stockNuevo = stockNuevo;
        this.generateHashTransaccion();
    }

    // Método para generar hash de trazabilidad inmutable
    public void generateHashTransaccion() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String data = this.productoId + ":" + 
                         this.tipo + ":" + 
                         this.cantidad + ":" + 
                         this.stockAnterior + ":" + 
                         this.stockNuevo + ":" + 
                         System.currentTimeMillis();
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            this.hashTransaccion = hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generando hash de transacción", e);
        }
    }

    // Método para generar hash de cadena (enlace con transacción anterior)
    public void generateHashCadena(String hashTransaccionAnterior) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String data = this.hashTransaccion + ":" + 
                         (hashTransaccionAnterior != null ? hashTransaccionAnterior : "genesis");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            this.hashCadena = hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generando hash de cadena", e);
        }
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public TipoTransaccion getTipo() { return tipo; }
    public void setTipo(TipoTransaccion tipo) { this.tipo = tipo; }

    public UUID getProductoId() { return productoId; }
    public void setProductoId(UUID productoId) { this.productoId = productoId; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Integer getStockAnterior() { return stockAnterior; }
    public void setStockAnterior(Integer stockAnterior) { this.stockAnterior = stockAnterior; }

    public Integer getStockNuevo() { return stockNuevo; }
    public void setStockNuevo(Integer stockNuevo) { this.stockNuevo = stockNuevo; }

    public String getHashTransaccion() { return hashTransaccion; }
    public void setHashTransaccion(String hashTransaccion) { this.hashTransaccion = hashTransaccion; }

    public String getHashCadena() { return hashCadena; }
    public void setHashCadena(String hashCadena) { this.hashCadena = hashCadena; }

    public EstadoTransaccion getEstado() { return estado; }
    public void setEstado(EstadoTransaccion estado) { this.estado = estado; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getReferenciaPago() { return referenciaPago; }
    public void setReferenciaPago(String referenciaPago) { this.referenciaPago = referenciaPago; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public enum TipoTransaccion {
        VENTA, COMPRA, AJUSTE
    }

    public enum EstadoTransaccion {
        PENDIENTE, CONFIRMADO, CANCELADO, PAGADO
    }
}