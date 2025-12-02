package com.simpay.controller;

import com.simpay.dto.ProductoDTO;
import com.simpay.entity.Producto;
import com.simpay.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")

public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<Page<Producto>> getAllProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Producto> productos = productoService.getAllProductos(pageable);

        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable UUID id) {
        try {
            Producto producto = productoService.getProductoById(id);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<String>> getCategorias() {
        List<String> categorias = productoService.getCategorias();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<Producto>> buscarProductos(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productos = productoService.buscarProductos(q, pageable);

        return ResponseEntity.ok(productos);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<Page<Producto>> getProductosByCategoria(
            @PathVariable String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productos = productoService.getProductosByCategoria(categoria, pageable);

        return ResponseEntity.ok(productos);
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        try {
            Producto producto = productoService.crearProducto(productoDTO);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable UUID id,
            @Valid @RequestBody ProductoDTO productoDTO) {
        try {
            Producto producto = productoService.actualizarProducto(id, productoDTO);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.MessageResponse("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable UUID id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok(new AuthController.MessageResponse("Producto eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Producto>> getProductosStockBajo(
            @RequestParam(defaultValue = "10") Integer stockMinimo) {

        List<Producto> productos = productoService.getProductosStockBajo(stockMinimo);
        return ResponseEntity.ok(productos);
    }
}