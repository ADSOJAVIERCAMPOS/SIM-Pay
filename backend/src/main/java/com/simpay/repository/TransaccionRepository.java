package com.simpay.repository;

import com.simpay.entity.Transaccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, UUID> {
    
    Page<Transaccion> findByProductoIdOrderByCreatedAtDesc(UUID productoId, Pageable pageable);
    
    Page<Transaccion> findByUsuarioIdOrderByCreatedAtDesc(UUID usuarioId, Pageable pageable);
    
    List<Transaccion> findByProductoIdAndTipoOrderByCreatedAtDesc(UUID productoId, Transaccion.TipoTransaccion tipo);
    
    @Query("SELECT t FROM Transaccion t WHERE t.productoId = :productoId ORDER BY t.createdAt DESC")
    List<Transaccion> findTrazabilidadProducto(@Param("productoId") UUID productoId);
    
    @Query("SELECT t FROM Transaccion t WHERE t.createdAt BETWEEN :fechaInicio AND :fechaFin ORDER BY t.createdAt DESC")
    Page<Transaccion> findTransaccionesPorFecha(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                               @Param("fechaFin") LocalDateTime fechaFin, 
                                               Pageable pageable);
    
    @Query("SELECT t FROM Transaccion t ORDER BY t.createdAt DESC")
    Optional<Transaccion> findUltimaTransaccion();
    
    @Query("SELECT t FROM Transaccion t WHERE t.productoId = :productoId ORDER BY t.createdAt DESC")
    Optional<Transaccion> findUltimaTransaccionProducto(@Param("productoId") UUID productoId);
    
    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.tipo = :tipo AND t.createdAt >= :fecha")
    long countTransaccionesPorTipoDesde(@Param("tipo") Transaccion.TipoTransaccion tipo, 
                                       @Param("fecha") LocalDateTime fecha);
    
    @Query("SELECT SUM(t.total) FROM Transaccion t WHERE t.tipo = 'VENTA' AND t.estado = 'PAGADO' AND t.createdAt BETWEEN :fechaInicio AND :fechaFin")
    Optional<Double> calcularVentasPorPeriodo(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                             @Param("fechaFin") LocalDateTime fechaFin);
}