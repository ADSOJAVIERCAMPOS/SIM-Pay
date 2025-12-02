package com.simpay.repository;

import com.simpay.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {
    
    Page<Producto> findByActivoTrue(Pageable pageable);
    
    List<Producto> findByActivoTrueAndStockGreaterThan(Integer stock);
    
    Page<Producto> findByActivoTrueAndCategoriaIgnoreCase(String categoria, Pageable pageable);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.categoria) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    Page<Producto> buscarProductos(@Param("busqueda") String busqueda, Pageable pageable);
    
    @Query("SELECT DISTINCT p.categoria FROM Producto p WHERE p.activo = true AND p.categoria IS NOT NULL")
    List<String> findDistinctCategorias();
    
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.activo = true AND p.stock <= :stockMinimo")
    long countProductosStockBajo(@Param("stockMinimo") Integer stockMinimo);
    
    Optional<Producto> findByIdAndActivoTrue(UUID id);
}