-- Inicialización de la base de datos SIM-Pay
-- Script para crear las tablas iniciales y datos de prueba

-- Extensiones necesarias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    rol VARCHAR(50) DEFAULT 'USUARIO',
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de productos
CREATE TABLE IF NOT EXISTS productos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    categoria VARCHAR(100),
    activo BOOLEAN DEFAULT true,
    hash_stock_last_commit VARCHAR(64),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de transacciones (para trazabilidad inmutable)
CREATE TABLE IF NOT EXISTS transacciones (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tipo VARCHAR(50) NOT NULL, -- VENTA, COMPRA, AJUSTE
    producto_id UUID NOT NULL REFERENCES productos(id),
    usuario_id UUID NOT NULL REFERENCES usuarios(id),
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    stock_anterior INTEGER NOT NULL,
    stock_nuevo INTEGER NOT NULL,
    hash_transaccion VARCHAR(64) NOT NULL,
    hash_cadena VARCHAR(64),
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    metodo_pago VARCHAR(50),
    referencia_pago VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de auditoría
CREATE TABLE IF NOT EXISTS auditoria (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tabla VARCHAR(50) NOT NULL,
    registro_id UUID NOT NULL,
    accion VARCHAR(50) NOT NULL, -- INSERT, UPDATE, DELETE
    datos_anteriores JSONB,
    datos_nuevos JSONB,
    usuario_id UUID REFERENCES usuarios(id),
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para optimización
CREATE INDEX IF NOT EXISTS idx_productos_categoria ON productos(categoria);
CREATE INDEX IF NOT EXISTS idx_productos_activo ON productos(activo);
CREATE INDEX IF NOT EXISTS idx_transacciones_producto_id ON transacciones(producto_id);
CREATE INDEX IF NOT EXISTS idx_transacciones_usuario_id ON transacciones(usuario_id);
CREATE INDEX IF NOT EXISTS idx_transacciones_created_at ON transacciones(created_at);
CREATE INDEX IF NOT EXISTS idx_auditoria_tabla_registro ON auditoria(tabla, registro_id);

-- Función para calcular hash de trazabilidad
CREATE OR REPLACE FUNCTION calcular_hash_trazabilidad(
    p_producto_id UUID,
    p_stock_anterior INTEGER,
    p_stock_nuevo INTEGER,
    p_timestamp TIMESTAMP
) RETURNS VARCHAR(64) AS $$
BEGIN
    RETURN encode(
        digest(
            p_producto_id::text || 
            p_stock_anterior::text || 
            p_stock_nuevo::text || 
            extract(epoch from p_timestamp)::text,
            'sha256'
        ),
        'hex'
    );
END;
$$ LANGUAGE plpgsql;

-- Trigger para auditoría automática en productos
CREATE OR REPLACE FUNCTION trigger_auditoria_productos()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO auditoria (tabla, registro_id, accion, datos_nuevos)
        VALUES ('productos', NEW.id, 'INSERT', row_to_json(NEW));
        RETURN NEW;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO auditoria (tabla, registro_id, accion, datos_anteriores, datos_nuevos)
        VALUES ('productos', NEW.id, 'UPDATE', row_to_json(OLD), row_to_json(NEW));
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO auditoria (tabla, registro_id, accion, datos_anteriores)
        VALUES ('productos', OLD.id, 'DELETE', row_to_json(OLD));
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_productos_auditoria
    AFTER INSERT OR UPDATE OR DELETE ON productos
    FOR EACH ROW EXECUTE FUNCTION trigger_auditoria_productos();

-- Datos de prueba
INSERT INTO usuarios (email, password_hash, nombre, rol) VALUES 
('admin@simpay.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye...', 'Administrador', 'ADMIN'),
('vendedor@simpay.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye...', 'Vendedor', 'VENDEDOR')
ON CONFLICT (email) DO NOTHING;

INSERT INTO productos (nombre, descripcion, precio, stock, categoria) VALUES 
('Producto Demo 1', 'Producto de demostración para pruebas', 15000.00, 100, 'ELECTRONICA'),
('Producto Demo 2', 'Otro producto de prueba', 25000.00, 50, 'HOGAR'),
('Producto Demo 3', 'Producto para inventario', 8500.00, 200, 'OFICINA')
ON CONFLICT DO NOTHING;