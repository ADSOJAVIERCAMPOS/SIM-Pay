package com.simpay.service;

import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.simpay.dto.JwtResponse;
import com.simpay.dto.LoginRequest;
import com.simpay.entity.Usuario;
import com.simpay.repository.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

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
        
    @Value("${app.jwt.expiration}")
    private int jwtExpiration;

    // Almacenamiento temporal de códigos de verificación (en producción usar Redis o base de datos)
    private Map<String, String> verificationCodes = new HashMap<>();
    private Map<String, Long> codeTimestamps = new HashMap<>();xception("Usuario no encontrado");
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

    // Métodos para 2FA y verificación de dispositivos
    public boolean isNewDevice(AuthController.DeviceCheckRequest request) {
        // En producción, verificar contra base de datos de dispositivos conocidos
        // Por ahora, simular que el 50% de los casos son nuevos dispositivos
        String userAgent = request.getDeviceInfo().getUserAgent();
        return userAgent != null && !userAgent.contains("Chrome");
    }

    public boolean requires2FAVerification(String provider) {
        // En producción, verificar configuración de usuario
        // Por ahora, siempre requerir 2FA para OAuth externo
        return true;
    }

    public String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000); // Genera código de 6 dígitos
        return String.valueOf(code);
    }

    public void sendVerificationNotification(String provider, String code, AuthController.DeviceInfo deviceInfo) {
        // Almacenar código con timestamp
        String key = provider + "_" + deviceInfo.getUserAgent();
        verificationCodes.put(key, code);
        codeTimestamps.put(key, System.currentTimeMillis());

        // En producción: enviar email o SMS real
        System.out.println("==============================================");
        System.out.println("NOTIFICACIÓN DE SEGURIDAD - NUEVO DISPOSITIVO");
        System.out.println("==============================================");
        System.out.println("Proveedor: " + provider.toUpperCase());
        System.out.println("Dispositivo: " + deviceInfo.getPlatform());
        System.out.println("Navegador: " + deviceInfo.getUserAgent());
        System.out.println("Fecha/Hora: " + deviceInfo.getTimestamp());
        System.out.println("----------------------------------------------");
        System.out.println("CÓDIGO DE VERIFICACIÓN: " + code);
        System.out.println("Este código expira en 5 minutos");
        System.out.println("==============================================");

        // TODO: Integrar con servicio de email (SendGrid, AWS SES, etc.)
        // TODO: Integrar con servicio de SMS (Twilio, etc.)
    }

    public boolean verifyCode(String code, String provider) {
        // Buscar el código almacenado para este proveedor
        for (Map.Entry<String, String> entry : verificationCodes.entrySet()) {
            if (entry.getKey().startsWith(provider + "_") && entry.getValue().equals(code)) {
                Long timestamp = codeTimestamps.get(entry.getKey());
                
                // Verificar que el código no haya expirado (5 minutos)
                if (timestamp != null && (System.currentTimeMillis() - timestamp) < 300000) {
                    // Eliminar código usado
                    verificationCodes.remove(entry.getKey());
                    codeTimestamps.remove(entry.getKey());
                    return true;
                }
            }
        }
        return false;
    }
}