package com.simpay.config;

import com.simpay.entity.Usuario;
import com.simpay.entity.Producto;
import com.simpay.repository.UsuarioRepository;
import com.simpay.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@Profile("prod")
public class ProductionDataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeProductionData();
    }

    private void initializeProductionData() {
        // Crear usuario administrador si no existe
        createAdminUser();
        
        // Crear productos de ejemplo para demostración
        createDemoProducts();
    }

    private void createAdminUser() {
        Optional<Usuario> existingAdmin = usuarioRepository.findByEmailAndActivoTrue("admin@simpay.com");
        
        if (existingAdmin.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setId(UUID.randomUUID());
            admin.setEmail("admin@simpay.com");
            admin.setNombre("Administrador SIM-Pay");
            admin.setPasswordHash(passwordEncoder.encode("Admin123!"));
            admin.setRol(Usuario.Rol.ADMIN);
            admin.setActivo(true);
            admin.setCreatedAt(LocalDateTime.now());
            
            usuarioRepository.save(admin);
            System.out.println("✅ Usuario administrador creado en producción");
        }

        // Crear usuario vendedor de demostración
        Optional<Usuario> existingVendedor = usuarioRepository.findByEmailAndActivoTrue("vendedor@simpay.com");
        
        if (existingVendedor.isEmpty()) {
            Usuario vendedor = new Usuario();
            vendedor.setId(UUID.randomUUID());
            vendedor.setEmail("vendedor@simpay.com");
            vendedor.setNombre("Vendedor Demo");
            vendedor.setPasswordHash(passwordEncoder.encode("Vendedor123!"));
            vendedor.setRol(Usuario.Rol.CAJERO);
            vendedor.setActivo(true);
            vendedor.setCreatedAt(LocalDateTime.now());
            
            usuarioRepository.save(vendedor);
            System.out.println("✅ Usuario vendedor demo creado en producción");
        }
    }

    private void createDemoProducts() {
        if (productoRepository.count() == 0) {
            // Productos de demostración para la tesis
            createProduct("Laptop Dell Inspiron 15", "Laptop para oficina con Intel i5", 
                         "TECNOLOGÍA", new BigDecimal("2500000"), 10);
            
            createProduct("Mouse Inalámbrico Logitech", "Mouse ergonómico inalámbrico", 
                         "TECNOLOGÍA", new BigDecimal("85000"), 25);
            
            createProduct("Escritorio Ejecutivo", "Escritorio de madera para oficina", 
                         "MOBILIARIO", new BigDecimal("450000"), 5);
            
            createProduct("Silla Ergonómica", "Silla de oficina con soporte lumbar", 
                         "MOBILIARIO", new BigDecimal("320000"), 12);
            
            createProduct("Café Premium Colombia", "Café 100% colombiano tostado", 
                         "ALIMENTOS", new BigDecimal("35000"), 50);
            
            System.out.println("✅ Productos de demostración creados en producción");
        }
    }

    private void createProduct(String nombre, String descripcion, String categoria, 
                              BigDecimal precio, int stock) {
        Producto producto = new Producto();
        producto.setId(UUID.randomUUID());
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setCategoria(categoria);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setActivo(true);
        producto.setCreatedAt(LocalDateTime.now());
        
        productoRepository.save(producto);
    }
}