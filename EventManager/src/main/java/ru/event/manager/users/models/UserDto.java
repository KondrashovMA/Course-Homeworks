package ru.event.manager.users.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
        Long id,

        @NotBlank
        String login,

        @NotBlank
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,

        @Min(0)
        @Max(150)
        Integer age,

        String role
) {
}