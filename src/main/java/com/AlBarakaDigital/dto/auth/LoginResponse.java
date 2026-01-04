package com.AlBarakaDigital.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String accessToken;      // Le token principal (JWT custom OU Keycloak)
    private String tokenType;        // "Bearer"
    private String authenticationType; // "CUSTOM_JWT" ou "KEYCLOAK_OAUTH2"
    private Long expiresIn;          // Durée de validité en secondes
    private String role;             // Rôle de l'utilisateur

    // Constructeur pour compatibilité avec l'ancien code
    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
        this.authenticationType = "CUSTOM_JWT";
    }
}