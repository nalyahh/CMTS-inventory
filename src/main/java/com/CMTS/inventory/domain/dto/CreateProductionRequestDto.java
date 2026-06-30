package com.CMTS.inventory.domain.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record CreateProductionRequestDto(
        @NotBlank (message = ERROR_MESSAGE_NAME)
        @Length(max = 255, message = ERROR_MESSAGE_NAME)
        String name,
        @FutureOrPresent(message = ERROR_MESSAGE_DATE)
        LocalDate startDate,
        @FutureOrPresent (message = ERROR_MESSAGE_DATE)
        LocalDate endDate) {

        private static final String ERROR_MESSAGE_NAME =
            "Name must be between 1 and 255 characters";
        private static final String ERROR_MESSAGE_DATE =
            "Date must be in the present or the future";
}
