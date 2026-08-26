package com.CMTS.inventory.domain;

import java.time.LocalDate;

public record UpdateProductionRequest(
        String name,
        LocalDate startDate,
        LocalDate endDate) {
}
