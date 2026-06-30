package com.CMTS.inventory.domain;

import com.CMTS.inventory.domain.entity.User;

public record CreateUserRequest(
        String name,
        String email,
        String password,
        User.Role role,
        Long productionId) {
}
