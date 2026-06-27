package dev.sorokin.eventmanager.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpRequest(
        @NotBlank
        String login,
        @NotBlank
        String password,
        @NotNull
        @Min(value = 0, message = "Age must be greater or equal than 0")
        Integer age
) {
}
