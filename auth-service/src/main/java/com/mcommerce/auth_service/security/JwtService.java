package com.mcommerce.auth_service.security;

import com.mcommerce.auth_service.entity.User;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String generateToken(User user);
    Claims extractAllClaims(String token);

    boolean isTokenValid(String token);
}
