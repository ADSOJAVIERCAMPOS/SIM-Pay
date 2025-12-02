package com.simpay.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simpay.dto.ProductoDTO;
import com.simpay.entity.Producto;
import com.simpay.repository.ProductoRepository;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public Page<Producto> getAllProductos(Pageable pageable) {
        return productoRepository.findByActivoTrue(pageable);
    }

    public Producto getProductoById(UUID id) {
        return productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    public List<String> getCategorias() {
        return productoRepository.findDistinctCategorias();
    }

    public Page<Producto> buscarProductos(String busqueda, Pageable pageable) {
        if (busqueda == null || busqueda.trim().isEmpty()) {
            return getAllProductos(pageable);
        }
        return productoRepository.buscarProductos(busqueda.trim(), pageable);
    }

    public Page<Producto> getProductosByCategoria(String categoria, Pageable pageable) {
        return productoRepository.findByActivoTrueAndCategoriaIgnoreCase(categoria, pageable);
    }

    public Producto crearProducto(ProductoDTO productoDTO) {
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setCategoria(productoDTO.getCategoria());
        producto.updateHashStock(); // Genera hash inicial
        
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(UUID id, ProductoDTO productoDTO) {
        Producto producto = getProductoById(id);
        
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setCategoria(productoDTO.getCategoria());
        
        // Si cambió el stock, actualizar hash
        if (!producto.getStock().equals(productoDTO.getStock())) {
            producto.setStock(productoDTO.getStock());
            producto.updateHashStock();
        }
        
        return productoRepository.save(producto);
    }

    public void eliminarProducto(UUID id) {
        Producto producto = getProductoById(id);
        producto.setActivo(false); // Soft delete
        productoRepository.save(producto);
    }

    public List<Producto> getProductosStockBajo(Integer stockMinimo) {
        return productoRepository.findByActivoTrueAndStockGreaterThan(stockMinimo);
    }

    public long countProductosStockBajo(Integer stockMinimo) {
        return productoRepository.countProductosStockBajo(stockMinimo);
    }

    // Método interno para actualizar stock (usado por TransaccionService)
    public Producto actualizarStock(UUID productoId, Integer nuevoStock) {
        Producto producto = getProductoById(productoId);
        producto.actualizarStock(nuevoStock);
        return productoRepository.save(producto);
    }
}