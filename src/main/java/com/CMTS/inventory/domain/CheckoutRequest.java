package com.CMTS.inventory.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CheckoutRequest(
        Long itemId,
        Long userId,
        Long productionId) {
}
