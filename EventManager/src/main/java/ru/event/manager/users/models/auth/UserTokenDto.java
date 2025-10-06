package ru.event.manager.users.models.auth;

import jakarta.validation.constraints.NotBlank;

public record UserTokenDto(
        @NotBlank
        String jwtToken
)
{}
