package com.AlBarakaDigital.controller;

import com.AlBarakaDigital.dto.UserRequestDTO;
import com.AlBarakaDigital.dto.UserResponseDTO;
import com.AlBarakaDigital.dto.auth.*;
import com.AlBarakaDigital.entity.User;
import com.AlBarakaDigital.repository.UserRepository;
import com.AlBarakaDigital.security.jwt.JwtUtil;
import com.AlBarakaDigital.service.KeycloakAuthService;
import com.AlBarakaDigital.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserService userService;
    private final KeycloakAuthService keycloakAuthService;

    // Login classique avec JWT custom (pour les autres endpoints)
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new LoginResponse(token);
    }

    // Nouveau login OAuth2 avec Keycloak (pour l'endpoint pending operations)
    @PostMapping("/login/oauth2")
    public ResponseEntity<?> loginOAuth2(@RequestBody LoginRequest request) {
        try {
            // Authentifier via Keycloak et récupérer le token OAuth2
            String accessToken = keycloakAuthService.authenticateAndGetToken(
                    request.getEmail(),
                    request.getPassword()
            );

            return ResponseEntity.ok(new OAuth2LoginResponse(accessToken));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody UserRequestDTO requestDTO) {

        UserResponseDTO createdUser = userService.createUser(requestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}