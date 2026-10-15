package com.example.taskmanagement.Controller;
import com.example.taskmanagement.Dto.AuthResponse;
import com.example.taskmanagement.Dto.LoginRequest;
import com.example.taskmanagement.Dto.RegisterRequest;


import com.example.taskmanagement.Services.securityService.AuthService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

//    private final AuthService authService;
private final com.example.taskmanagement.Services.securityService.AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register  a new user
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    // Login and receive JWT Using TOKEN
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}
