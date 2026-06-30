package com.CMTS.inventory.domain.dto;

import com.CMTS.inventory.domain.entity.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateUserRequestDto(
        @NotBlank(message = ERROR_MESSAGE_NAME)
        @Length(max = 255, message = ERROR_MESSAGE_NAME)
        String name,
        @NotBlank(message = ERROR_MESSAGE_EMAIL)
        @Email(message = ERROR_MESSAGE_EMAIL)
        String email,
        @NotBlank (message = ERROR_MESSAGE_PASSWORD)
        @Length(min = 4, max = 255, message = ERROR_MESSAGE_PASSWORD)
        String password,
        @NotNull
        User.Role role,
        @Nullable
        List<Long> productionIds) {

        private static final String ERROR_MESSAGE_NAME =
            "Name must be between 1 and 255 characters";

        private static final String ERROR_MESSAGE_EMAIL =
            "Email cannot be blank and must be in email format";

        private static final String ERROR_MESSAGE_PASSWORD =
            "Password must be between 4 and 255 characters";

        @AssertTrue(message = "Crew members must belong to at least one production")
        public boolean isProductionValid() {
                return role == User.Role.ADMIN || (productionIds != null && !productionIds.isEmpty());
        }
}
