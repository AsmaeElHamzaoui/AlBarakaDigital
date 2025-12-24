package com.AlBarakaDigital.controller;

import com.AlBarakaDigital.dto.UserRequestDTO;
import com.AlBarakaDigital.dto.UserResponseDTO;
import com.AlBarakaDigital.dto.auth.*;
import com.AlBarakaDigital.entity.User;
import com.AlBarakaDigital.repository.UserRepository;
import com.AlBarakaDigital.security.jwt.JwtUtil;
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

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody UserRequestDTO requestDTO) {

        UserResponseDTO createdUser = userService.createUser(requestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
