package com.CMTS.inventory.domain.dto;

import java.time.LocalDate;

public record ProductionDto(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        boolean archived) {
}
