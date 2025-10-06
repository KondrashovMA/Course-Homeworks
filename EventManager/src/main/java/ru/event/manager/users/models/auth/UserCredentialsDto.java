package ru.event.manager.users.models.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UserCredentialsDto(
        @NotBlank
        String login,

        @NotBlank
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password
) {
}
