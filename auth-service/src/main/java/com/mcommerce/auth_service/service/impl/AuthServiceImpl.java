package com.mcommerce.auth_service.service.impl;

import com.mcommerce.auth_service.dto.AuthResponse;
import com.mcommerce.auth_service.dto.LoginRequest;
import com.mcommerce.auth_service.dto.RegisterRequest;
import com.mcommerce.auth_service.entity.User;
import com.mcommerce.auth_service.enums.Role;
import com.mcommerce.auth_service.repository.UserRepository;
import com.mcommerce.auth_service.security.JwtService;
import com.mcommerce.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username déjà utilisé");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .message("Utilisateur créé avec succès")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .message("Connexion réussie")
                .token(token)
                .build();
    }

}
