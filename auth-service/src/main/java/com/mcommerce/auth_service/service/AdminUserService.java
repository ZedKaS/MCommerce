package com.mcommerce.auth_service.service;

import com.mcommerce.auth_service.dto.CreateUserRequest;
import com.mcommerce.auth_service.dto.UpdateUserRequest;
import com.mcommerce.auth_service.dto.UserResponse;

import java.util.List;

public interface AdminUserService {
    List<UserResponse> getAllUsers();
    UserResponse createUser(CreateUserRequest request);
    UserResponse updateUser(Long id, UpdateUserRequest request);
    void deleteUser(Long id);
}
