package com.mcommerce.auth_service.service;

import com.mcommerce.auth_service.dto.AuthResponse;
import com.mcommerce.auth_service.dto.LoginRequest;
import com.mcommerce.auth_service.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
