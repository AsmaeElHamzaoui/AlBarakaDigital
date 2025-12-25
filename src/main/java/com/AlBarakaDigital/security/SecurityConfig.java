package com.AlBarakaDigital.security;

import com.AlBarakaDigital.security.jwt.JwtAuthenticationFilter;
import com.AlBarakaDigital.security.oauth2.OAuth2AuthenticationFilter;
import com.AlBarakaDigital.security.web.JwtAccessDeniedHandler;
import com.AlBarakaDigital.security.web.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration.jwtDecoder;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final OAuth2AuthenticationFilter oauth2Filter;

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

                        // Endpoint sécurisé par OAuth2
                        .requestMatchers("/api/agent/operations/pending")
                        .hasAuthority("SCOPE_operations.read")

                        .requestMatchers("/api/client/**")
                        .hasRole("CLIENT")

                        .requestMatchers("/api/agent/**")
                        .hasRole("AGENT_BANCAIRE")

                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                // OAuth2 Resource Server configuration
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder()))
                )

                .addFilterBefore(oauth2Filter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Spring Boot auto-configure ceci via application.properties
        return JwtDecoders.fromIssuerLocation(
                "http://localhost:8080/realms/albaraka-realm"
        );
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
