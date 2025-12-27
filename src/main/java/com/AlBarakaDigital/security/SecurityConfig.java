package com.AlBarakaDigital.security;

import com.AlBarakaDigital.security.jwt.JwtAuthenticationFilter;
import com.AlBarakaDigital.security.web.JwtAccessDeniedHandler;
import com.AlBarakaDigital.security.web.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.*;

import static org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration.jwtDecoder;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        .accessDeniedHandler(new JwtAccessDeniedHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/agent/operations/**")
                        .hasAnyAuthority("ROLE_app_AGENT_BANCAIRE", "ROLE_AGENT_BANCAIRE")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Set<GrantedAuthority> authorities = new HashSet<>();

            // Extraire les scopes
            Map<String, Object> claims = jwt.getClaims();
            if (claims.containsKey("scope")) {
                String scope = (String) claims.get("scope");
                Arrays.stream(scope.split(" "))
                        .map(scopeStr -> new SimpleGrantedAuthority("SCOPE_" + scopeStr))
                        .forEach(authorities::add);
            }

            // Extraire les rôles de Keycloak
            if (claims.containsKey("realm_access")) {
                Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
                if (realmAccess.containsKey("roles")) {
                    List<String> roles = (List<String>) realmAccess.get("roles");
                    roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .forEach(authorities::add);
                }
            }

            return authorities;
        });
        return converter;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}