package com.ecommerce.user.dto;

import com.ecommerce.user.model.UserRole;

public record UserResponse(String id,
        String firstName,
        String lastName,
        String email,
        String phone,
        UserRole role,
        AddressDTO address) {

}
