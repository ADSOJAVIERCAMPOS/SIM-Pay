package com.simpay.controller;

import com.simpay.dto.JwtResponse;
import com.simpay.dto.LoginRequest;
import com.simpay.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.login(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(new MessageResponse("Sesión cerrada exitosamente"));
    }

    @PostMapping("/check-device")
    public ResponseEntity<?> checkDevice(@RequestBody DeviceCheckRequest request) {
        try {
            // Verificar si es un nuevo dispositivo basado en userAgent y otros datos
            boolean isNewDevice = authService.isNewDevice(request);
            boolean requires2FA = authService.requires2FAVerification(request.getProvider());
            
            if (isNewDevice || requires2FA) {
                // Generar y enviar código de verificación
                String code = authService.generateVerificationCode();
                authService.sendVerificationNotification(request.getProvider(), code, request.getDeviceInfo());
                
                return ResponseEntity.ok(new DeviceCheckResponse(isNewDevice, requires2FA, 
                    "Código de verificación enviado"));
            }
            
            return ResponseEntity.ok(new DeviceCheckResponse(false, false, "Dispositivo reconocido"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<?> verify2FA(@RequestBody VerificationRequest request) {
        try {
            boolean verified = authService.verifyCode(request.getCode(), request.getProvider());
            
            if (verified) {
                return ResponseEntity.ok(new VerificationResponse(true, "Código verificado correctamente"));
            } else {
                return ResponseEntity.badRequest()
                    .body(new VerificationResponse(false, "Código inválido o expirado"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/send-verification")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody VerificationEmailRequest request) {
        try {
            authService.sendNewUserVerificationEmail(request.getEmail(), request.getNombre());
            return ResponseEntity.ok(new MessageResponse("Correo de verificación enviado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error al enviar correo: " + e.getMessage()));
        }
    }

    // Clases internas para requests y responses
    public static class DeviceCheckRequest {
        private String provider;
        private DeviceInfo deviceInfo;

        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        public DeviceInfo getDeviceInfo() { return deviceInfo; }
        public void setDeviceInfo(DeviceInfo deviceInfo) { this.deviceInfo = deviceInfo; }
    }

    public static class DeviceInfo {
        private String userAgent;
        private String platform;
        private String language;
        private String timestamp;

        public String getUserAgent() { return userAgent; }
        public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
        public String getPlatform() { return platform; }
        public void setPlatform(String platform) { this.platform = platform; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    }

    public static class DeviceCheckResponse {
        private boolean isNewDevice;
        private boolean requires2FA;
        private String message;

        public DeviceCheckResponse(boolean isNewDevice, boolean requires2FA, String message) {
            this.isNewDevice = isNewDevice;
            this.requires2FA = requires2FA;
            this.message = message;
        }

        public boolean isNewDevice() { return isNewDevice; }
        public void setNewDevice(boolean newDevice) { isNewDevice = newDevice; }
        public boolean isRequires2FA() { return requires2FA; }
        public void setRequires2FA(boolean requires2FA) { this.requires2FA = requires2FA; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class VerificationRequest {
        private String code;
        private String provider;

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
    }

    public static class VerificationResponse {
        private boolean verified;
        private String message;

        public VerificationResponse(boolean verified, String message) {
            this.verified = verified;
            this.message = message;
        }

        public boolean isVerified() { return verified; }
        public void setVerified(boolean verified) { this.verified = verified; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class VerificationEmailRequest {
        private String email;
        private String nombre;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    // Clase interna para respuestas de mensaje
    public static class MessageResponse {
        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}