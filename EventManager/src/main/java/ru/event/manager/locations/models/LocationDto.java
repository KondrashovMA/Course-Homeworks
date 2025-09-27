package ru.event.manager.locations.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LocationDto(
        Long id,

        @NotBlank
        String name,

        @NotBlank
        String address,

        @Min(0)
        @Max(500_000)
        Integer capacity,

        @NotBlank
        String description
) {}
