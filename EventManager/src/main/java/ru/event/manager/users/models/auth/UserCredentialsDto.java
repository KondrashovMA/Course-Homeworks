package ru.event.manager.users.models.auth;

import jakarta.validation.constraints.NotBlank;

public record UserCredentialsDto(
        @NotBlank
        String login,

        @NotBlank
        String password
) {
}
