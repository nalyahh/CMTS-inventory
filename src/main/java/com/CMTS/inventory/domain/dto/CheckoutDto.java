package com.CMTS.inventory.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CheckoutDto(
        Long id,
        Long itemId,
        String itemName,
        Long userId,
        Long productionId,
        String productionName,
        LocalDateTime checkedOutAt,
        LocalDate dueDate,
        LocalDateTime returnedAt) {
}
