package com.simpay.service;

import com.simpay.dto.JwtResponse;
import com.simpay.dto.LoginRequest;
import com.simpay.entity.Usuario;
import com.simpay.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private int jwtExpiration;

    public JwtResponse login(LoginRequest loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndActivoTrue(loginRequest.getEmail());
        
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        Usuario usuario = usuarioOpt.get();
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPasswordHash())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        
        String token = generateJwtToken(usuario);
        
        return new JwtResponse(
            token,
            usuario.getId(),
            usuario.getEmail(),
            usuario.getNombre(),
            usuario.getRol().name()
        );
    }
    
    private String generateJwtToken(Usuario usuario) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpiration);
        
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .claim("userId", usuario.getId().toString())
                .claim("rol", usuario.getRol().name())
                .signWith(key)
                .compact();
    }
    
    public Usuario getUserFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            String email = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
                    
            return usuarioRepository.findByEmailAndActivoTrue(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        } catch (Exception e) {
            throw new RuntimeException("Token inválido", e);
        }
    }
}