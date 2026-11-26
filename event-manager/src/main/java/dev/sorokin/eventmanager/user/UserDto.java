package dev.sorokin.eventmanager.user;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
        Long id,
        String login,
        Integer age,
        String password,
        UserRole role
) {
}
