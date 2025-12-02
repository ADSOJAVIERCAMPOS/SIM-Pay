package com.simpay.dto;

import java.util.UUID;

public class JwtResponse {
    
    private String token;
    private String type = "Bearer";
    private UUID id;
    private String email;
    private String nombre;
    private String rol;
    
    public JwtResponse(String accessToken, UUID id, String email, String nombre, String rol) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.rol = rol;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
}