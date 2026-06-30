package com.CMTS.inventory.domain;

import com.CMTS.inventory.domain.entity.User;

import java.util.List;

public record CreateUserRequest(
        String name,
        String email,
        String password,
        User.Role role,
        List<Long> productionIds) {
}
