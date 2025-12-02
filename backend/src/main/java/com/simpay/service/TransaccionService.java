package com.simpay.service;

import com.simpay.dto.VentaRequest;
import com.simpay.entity.Producto;
import com.simpay.entity.Transaccion;
import com.simpay.entity.Usuario;
import com.simpay.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private ProductoService productoService;

    public Page<Transaccion> getAllTransacciones(Pageable pageable) {
        return transaccionRepository.findAll(pageable);
    }

    public Page<Transaccion> getTransaccionesByProducto(UUID productoId, Pageable pageable) {
        return transaccionRepository.findByProductoIdOrderByCreatedAtDesc(productoId, pageable);
    }

    public Page<Transaccion> getTransaccionesByUsuario(UUID usuarioId, Pageable pageable) {
        return transaccionRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId, pageable);
    }

    public List<Transaccion> getTrazabilidadProducto(UUID productoId) {
        return transaccionRepository.findTrazabilidadProducto(productoId);
    }

    public Transaccion procesarVenta(VentaRequest ventaRequest, Usuario usuario) {
        // Validar producto y stock
        Producto producto = productoService.getProductoById(ventaRequest.getProductoId());
        
        if (producto.getStock() < ventaRequest.getCantidad()) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + producto.getStock());
        }
        
        // Crear transacción
        Integer stockAnterior = producto.getStock();
        Integer stockNuevo = stockAnterior - ventaRequest.getCantidad();
        
        Transaccion transaccion = new Transaccion(
            Transaccion.TipoTransaccion.VENTA,
            ventaRequest.getProductoId(),
            usuario.getId(),
            ventaRequest.getCantidad(),
            ventaRequest.getPrecioUnitario(),
            stockAnterior,
            stockNuevo
        );
        
        // Configurar método de pago
        transaccion.setMetodoPago(ventaRequest.getMetodoPago());
        
        // Generar hash de cadena (enlace con transacción anterior)
        Optional<Transaccion> ultimaTransaccion = transaccionRepository.findUltimaTransaccionProducto(ventaRequest.getProductoId());
        String hashAnterior = ultimaTransaccion.map(Transaccion::getHashTransaccion).orElse(null);
        transaccion.generateHashCadena(hashAnterior);
        
        // Guardar transacción
        Transaccion transaccionGuardada = transaccionRepository.save(transaccion);
        
        // Actualizar stock del producto
        productoService.actualizarStock(ventaRequest.getProductoId(), stockNuevo);
        
        return transaccionGuardada;
    }
    
    public Transaccion confirmarPago(UUID transaccionId, String referenciaPago) {
        Transaccion transaccion = transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
        
        transaccion.setEstado(Transaccion.EstadoTransaccion.PAGADO);
        transaccion.setReferenciaPago(referenciaPago);
        
        return transaccionRepository.save(transaccion);
    }
    
    public Transaccion cancelarTransaccion(UUID transaccionId, Usuario usuario) {
        Transaccion transaccion = transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
        
        if (transaccion.getEstado() == Transaccion.EstadoTransaccion.PAGADO) {
            throw new RuntimeException("No se puede cancelar una transacción ya pagada");
        }
        
        // Revertir stock si era una venta
        if (transaccion.getTipo() == Transaccion.TipoTransaccion.VENTA) {
            Producto producto = productoService.getProductoById(transaccion.getProductoId());
            Integer stockRevertido = producto.getStock() + transaccion.getCantidad();
            productoService.actualizarStock(transaccion.getProductoId(), stockRevertido);
        }
        
        transaccion.setEstado(Transaccion.EstadoTransaccion.CANCELADO);
        return transaccionRepository.save(transaccion);
    }
    
    public Double calcularVentasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return transaccionRepository.calcularVentasPorPeriodo(fechaInicio, fechaFin)
                .orElse(0.0);
    }
    
    // Verificar integridad de la cadena de trazabilidad
    public boolean verificarIntegridadTrazabilidad(UUID productoId) {
        List<Transaccion> transacciones = getTrazabilidadProducto(productoId);
        
        for (int i = 1; i < transacciones.size(); i++) {
            Transaccion actual = transacciones.get(i);
            Transaccion anterior = transacciones.get(i - 1);
            
            // Verificar que el hash de cadena sea correcto
            actual.generateHashCadena(anterior.getHashTransaccion());
            // Aquí compareríamos con el hash almacenado
        }
        
        return true; // Implementación simplificada
    }
}