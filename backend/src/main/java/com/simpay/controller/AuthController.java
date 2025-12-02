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
@CrossOrigin(origins = "*", maxAge = 3600)
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