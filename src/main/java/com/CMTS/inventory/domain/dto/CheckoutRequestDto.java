package com.CMTS.inventory.domain.dto;

import jakarta.validation.constraints.NotNull;

public record CheckoutRequestDto(
        @NotNull(message = "Item is required")
        Long itemId,
        @NotNull(message = "User is required")
        Long userId,
        @NotNull(message = "Production is required")
        Long productionId)
{

}
