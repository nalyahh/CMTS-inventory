package com.CMTS.inventory.domain.dto;

public record AuthResponseDto(
        String token,
        long expiresIn) {
}
