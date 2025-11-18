package com.saumya.auth.controller;

import com.saumya.auth.dto.LoginRequest;
import com.saumya.auth.dto.RegisterRequest;
import com.saumya.auth.dto.EmailRequest;
import com.saumya.auth.dto.ResetPasswordRequest;
import com.saumya.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String base = "http://localhost:8080/api/auth/verify?token=";
        authService.register(request, base);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token) {
        boolean ok = authService.verifyAccount(token);
        if (!ok) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        if (token == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(token);
    }

    @PostMapping("/request-reset")
    public ResponseEntity<?> requestReset(@RequestBody EmailRequest request) {
        String base = "http://localhost:8080/api/auth/reset?token=";
        authService.requestPasswordReset(request.getEmail(), base);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody ResetPasswordRequest request) {
        boolean ok = authService.resetPassword(request);
        if (!ok) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }
}
