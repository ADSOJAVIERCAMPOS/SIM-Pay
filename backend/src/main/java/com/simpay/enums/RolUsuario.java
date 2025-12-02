package com.simpay.enums;

/**
 * Enumeración que define los roles disponibles en el sistema SIM-Pay
 */
public enum RolUsuario {
    ADMIN("Administrador del sistema"),
    GERENTE("Gerente de inventario"),
    CAJERO("Operador de ventas"),
    CONTADOR("Contador y auditor"),
    VIEWER("Solo visualización");

    private final String descripcion;

    RolUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}