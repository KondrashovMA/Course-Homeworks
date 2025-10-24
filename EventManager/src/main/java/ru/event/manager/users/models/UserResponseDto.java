package ru.event.manager.users.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserResponseDto (
    Long id,

    @NotBlank
    String login,

    @Min(0)
    @Max(150)
    Integer age
){
}
