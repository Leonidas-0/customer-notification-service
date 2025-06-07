package com.levanz.customer.controller;

import com.levanz.customer.security.JwtUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwt;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest body) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.username(), body.password()));

        String token = jwt.generateToken(authentication.getName());

        return ResponseEntity.ok(Map.of("token", token));
    }

    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {}
}
