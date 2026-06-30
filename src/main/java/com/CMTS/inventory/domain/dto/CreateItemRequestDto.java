package com.CMTS.inventory.domain.dto;

import com.CMTS.inventory.domain.entity.Item;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateItemRequestDto(
        @NotBlank(message = ERROR_MESSAGE_NAME)
        @Length(max = 255, message = ERROR_MESSAGE_NAME)
        String name,
        @NotBlank(message = ERROR_MESSAGE_LOCATION)
        @Length(max = 255, message = ERROR_MESSAGE_LOCATION)
        String location,
        @NotNull
        Item.Categories category,
        @Min(1)
        int quantity,
        @Nullable
        @Length(max = 1000, message = ERROR_MESSAGE_NOTES)
        String notes,
        @Nullable
        String photoURL,
        @Nullable
        Long productionId) {

        private static final String ERROR_MESSAGE_NAME =
                "Name must be between 1 and 255 characters";

        private static final String ERROR_MESSAGE_LOCATION =
                "Location must be between 1 and 255 characters";

        private static final String ERROR_MESSAGE_NOTES =
                "Notes have a max of 1000 characters";
}
