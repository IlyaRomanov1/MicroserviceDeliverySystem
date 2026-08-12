package com.ilyaromanov.authservice.controllers;

import com.ilyaromanov.authservice.models.dtos.UserDTO;
import com.ilyaromanov.authservice.models.dtos.requests.AuthRequestDTO;
import com.ilyaromanov.authservice.models.dtos.responses.AuthResponseDTO;
import com.ilyaromanov.authservice.security.JWTUtil;
import com.ilyaromanov.authservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody UserDTO dto) {
        authService.register(dto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthRequestDTO dto){
        AuthResponseDTO claims = authService.login(dto);
        String token = jwtUtil.generateToken(claims);
        return Map.of("jwt-token", token);
    }
}
