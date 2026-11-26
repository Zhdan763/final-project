package dev.sorokin.eventmanager.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignInRequest(
        @NotBlank
        String login,
        @NotBlank
        String password

) {
}
