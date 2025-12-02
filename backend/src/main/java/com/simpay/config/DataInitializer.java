package com.simpay.config;

import com.simpay.entity.Producto;
import com.simpay.entity.Usuario;
import com.simpay.repository.ProductoRepository;
import com.simpay.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear usuarios de prueba si no existen
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario(
                "admin@simpay.com",
                passwordEncoder.encode("admin123"),
                "Administrador SIM-Pay",
                Usuario.Rol.ADMIN
            );
            usuarioRepository.save(admin);

            Usuario vendedor = new Usuario(
                "vendedor@simpay.com",
                passwordEncoder.encode("vendedor123"),
                "Vendedor Demo",
                Usuario.Rol.CAJERO
            );
            usuarioRepository.save(vendedor);

            System.out.println("\n✅ Usuarios de prueba creados:");
            System.out.println("   👤 Admin: admin@simpay.com / admin123");
            System.out.println("   👤 Vendedor: vendedor@simpay.com / vendedor123\n");
        }

        // Crear productos de prueba si no existen
        if (productoRepository.count() == 0) {
            Producto producto1 = new Producto(
                "iPhone 15 Pro",
                "Smartphone Apple iPhone 15 Pro 256GB",
                new BigDecimal("4500000"),
                15,
                "ELECTRONICA"
            );
            productoRepository.save(producto1);

            Producto producto2 = new Producto(
                "Samsung Galaxy S24",
                "Smartphone Samsung Galaxy S24 Ultra 512GB",
                new BigDecimal("4200000"),
                8,
                "ELECTRONICA"
            );
            productoRepository.save(producto2);

            Producto producto3 = new Producto(
                "MacBook Air M3",
                "Laptop Apple MacBook Air 13\" con chip M3",
                new BigDecimal("5800000"),
                5,
                "COMPUTADORES"
            );
            productoRepository.save(producto3);

            Producto producto4 = new Producto(
                "AirPods Pro 2",
                "Audífonos inalámbricos Apple AirPods Pro 2ª generación",
                new BigDecimal("980000"),
                25,
                "ACCESORIOS"
            );
            productoRepository.save(producto4);

            System.out.println("✅ Productos de demostación creados exitosamente\n");
        }

        System.out.println("🚀 SIM-Pay Backend iniciado correctamente");
        System.out.println("🌐 API disponible en: http://localhost:8080/api");
        System.out.println("📈 Actuator: http://localhost:8080/actuator/health");
        System.out.println("📊 H2 Console: http://localhost:8080/h2-console\n");
    }
}