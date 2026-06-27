package dev.sorokin.eventmanager.user;

public record User (
        Long id,
        String login,
        Integer age,
        String password,
        UserRole role
) {
}
