package com.mcommerce.auth_service.dto;

import com.mcommerce.auth_service.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    private Role role;
    private Boolean enabled;

    private String password;
}
