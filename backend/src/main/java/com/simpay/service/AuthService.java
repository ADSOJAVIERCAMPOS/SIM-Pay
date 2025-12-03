package com.simpay.service;

import com.simpay.controller.AuthController;
import com.simpay.dto.JwtResponse;
import com.simpay.dto.LoginRequest;
import com.simpay.entity.DeviceLog;
import com.simpay.entity.Usuario;
import com.simpay.repository.DeviceLogRepository;
import com.simpay.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DeviceLogRepository deviceLogRepository;

    @Autowired
    private EmailService emailService;

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
        
        // Notificar al superadmin del login
        emailService.sendDataChangeNotification(
            "USUARIO",
            "LOGIN",
            "Usuario: " + usuario.getEmail() + " - Rol: " + usuario.getRol().name()
        );
        
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

    // ========== MÉTODOS PARA 2FA Y VERIFICACIÓN DE DISPOSITIVOS ==========
    
    public boolean isNewDevice(AuthController.DeviceCheckRequest request) {
        // En producción, verificar contra base de datos de dispositivos conocidos
        String userAgent = request.getDeviceInfo().getUserAgent();
        return userAgent != null && !userAgent.contains("Chrome");
    }

    public boolean requires2FAVerification(String provider) {
        // Siempre requerir 2FA para OAuth externo
        return true;
    }

    public String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void sendVerificationNotification(String provider, String code, AuthController.DeviceInfo deviceInfo) {
        // Guardar log en PostgreSQL
        DeviceLog deviceLog = new DeviceLog(
            provider,
            deviceInfo.getUserAgent(),
            deviceInfo.getPlatform(),
            deviceInfo.getLanguage()
        );
        deviceLog.setVerificationCode(code);
        deviceLog.setNotificationSentTo("ject2583@gmail.com");
        deviceLog.setTimestamp(LocalDateTime.now());
        
        deviceLogRepository.save(deviceLog);
        
        // Enviar email al superadmin
        String deviceInfoStr = String.format(
            "Platform: %s, UserAgent: %s, Language: %s",
            deviceInfo.getPlatform(),
            deviceInfo.getUserAgent(),
            deviceInfo.getLanguage()
        );
        
        emailService.sendNewDeviceNotification(provider, "N/A", deviceInfoStr, code);
        
        System.out.println("✅ DeviceLog guardado en PostgreSQL - ID: " + deviceLog.getId());
        System.out.println("✅ Notificación enviada a superadmin: ject2583@gmail.com");
    }

    public boolean verifyCode(String code, String provider) {
        Optional<DeviceLog> deviceLogOpt = deviceLogRepository
            .findByVerificationCodeAndProviderAndVerifiedFalse(code, provider);
        
        if (deviceLogOpt.isPresent()) {
            DeviceLog deviceLog = deviceLogOpt.get();
            
            // Verificar que no haya expirado (5 minutos)
            LocalDateTime expirationTime = deviceLog.getTimestamp().plusMinutes(5);
            
            if (LocalDateTime.now().isBefore(expirationTime)) {
                deviceLog.setVerified(true);
                deviceLog.setVerifiedAt(LocalDateTime.now());
                deviceLogRepository.save(deviceLog);
                
                // Notificar al superadmin de la verificación exitosa
                emailService.sendDataChangeNotification(
                    "DEVICE_LOG",
                    "VERIFIED",
                    "Provider: " + provider + ", Code: " + code + " - Verificado exitosamente"
                );
                
                System.out.println("✅ Código verificado y guardado en PostgreSQL");
                return true;
            } else {
                System.out.println("❌ Código expirado");
            }
        } else {
            System.out.println("❌ Código no encontrado");
        }
        
        return false;
    }
}
