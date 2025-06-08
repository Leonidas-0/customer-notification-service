package com.levanz.customer.controller;

import com.levanz.customer.dto.AuthResponseDto;
import com.levanz.customer.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;

    @Operation(summary = "Authenticate and receive JWT")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
        @Valid @RequestBody LoginRequest body
    ) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                body.username(), body.password()
            )
        );
        String token = jwtUtils.generateToken(auth);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }

    public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
    ) {}
}
