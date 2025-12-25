package com.AlBarakaDigital.security.oauth2;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class OAuth2AuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public OAuth2AuthenticationFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
        this.jwtAuthenticationConverter = new JwtAuthenticationConverter();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // Vérifier si c'est un token OAuth2 (Bearer token)
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                // Décoder et valider le JWT OAuth2
                Jwt jwt = jwtDecoder.decode(token);

                // Convertir en Authentication
                Authentication authentication =
                        jwtAuthenticationConverter.convert(jwt);

                // Placer dans le contexte de sécurité
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

            } catch (JwtException e) {
                // Token invalide - on laisse passer,
                // la sécurité bloquera si nécessaire
                logger.warn("Token OAuth2 invalide: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }


}