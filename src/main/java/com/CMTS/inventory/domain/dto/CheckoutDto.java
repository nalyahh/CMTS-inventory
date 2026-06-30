package com.CMTS.inventory.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CheckoutDto(
        Long id,
        Long itemId,
        Long userId,
        Long productionId,
        LocalDateTime checkedOutAt,
        LocalDate dueDate,
        LocalDateTime returnedAt) {
}
