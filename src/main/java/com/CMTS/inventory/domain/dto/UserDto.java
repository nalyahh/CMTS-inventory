package com.CMTS.inventory.domain.dto;

import com.CMTS.inventory.domain.entity.User;

public record UserDto(
        Long id,
        String name,
        String email,
        User.Role role,
        Long productionId) {
}
