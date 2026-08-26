package com.CMTS.inventory.domain.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record UpdateProductionRequestDto(
        @NotBlank(message = ERROR_MESSAGE_NAME)
        @Length(max = 255, message = ERROR_MESSAGE_NAME)
        String name,
        @NotNull(message = "Start date is required")
        LocalDate startDate,
        @NotNull(message = "End date is required")
        LocalDate endDate) {

    private static final String ERROR_MESSAGE_NAME =
            "Name must be between 1 and 255 characters";

    @AssertTrue(message = "End date must be on or after start date")
    public boolean isEndDateValid() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }
}
