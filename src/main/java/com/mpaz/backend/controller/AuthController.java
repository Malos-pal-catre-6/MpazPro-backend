package com.mpaz.backend.controller;

import com.mpaz.backend.dto.LoginRequest;
import com.mpaz.backend.dto.LoginResponse;
import com.mpaz.backend.dto.RegistroRequest;
import com.mpaz.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<Void> registrar(@Valid @RequestBody RegistroRequest req) {
        authService.registrar(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}
