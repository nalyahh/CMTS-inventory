package com.CMTS.inventory.domain;

import java.time.LocalDate;

public record CreateProductionRequest(
        String name,
        LocalDate startDate,
        LocalDate endDate) {
}
