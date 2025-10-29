package com.nfdtech.clinic_admin_api.controllers;

import com.nfdtech.clinic_admin_api.dto.auth.LoginRequestDTO;
import com.nfdtech.clinic_admin_api.dto.auth.LoginResponseDTO;
import com.nfdtech.clinic_admin_api.dto.auth.RefreshTokenRequestDTO;
import com.nfdtech.clinic_admin_api.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshRequest) {
        LoginResponseDTO response = authService.refreshToken(refreshRequest);
        return ResponseEntity.ok(response);
    }
}
