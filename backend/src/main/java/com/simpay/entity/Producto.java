package com.simpay.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "productos")
@EntityListeners(AuditingEntityListener.class)
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;

    @Min(0)
    @Column(nullable = false)
    private Integer stock = 0;

    private String categoria;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "hash_stock_last_commit", length = 64)
    private String hashStockLastCommit;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Producto() {}

    public Producto(String nombre, String descripcion, BigDecimal precio, Integer stock, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.updateHashStock();
    }

    // Método para actualizar el hash del stock (trazabilidad)
    public void updateHashStock() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String data = this.id + ":" + this.stock + ":" + System.currentTimeMillis();
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            this.hashStockLastCommit = hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generando hash de trazabilidad", e);
        }
    }

    // Método para actualizar stock con trazabilidad
    public void actualizarStock(Integer nuevoStock) {
        this.stock = nuevoStock;
        this.updateHashStock();
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { 
        this.stock = stock;
        this.updateHashStock();
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public String getHashStockLastCommit() { return hashStockLastCommit; }
    public void setHashStockLastCommit(String hashStockLastCommit) { this.hashStockLastCommit = hashStockLastCommit; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}