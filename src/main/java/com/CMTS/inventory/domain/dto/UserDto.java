package com.CMTS.inventory.domain.dto;

import com.CMTS.inventory.domain.entity.User;

import java.util.List;

public record UserDto(
        Long id,
        String name,
        String email,
        User.Role role,
        List<Long> productionIds) {
}
