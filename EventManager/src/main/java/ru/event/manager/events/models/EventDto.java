package ru.event.manager.events.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventDto(

        @NotNull
        LocalDateTime date,

        @NotNull
        @Min(0)
        @Max(50000)
        Integer duration,

        @NotNull
        @Min(0)
        @Max(10000)
        Integer cost,

        @NotNull
        @Min(0)
        @Max(100)
        Integer maxPlaces,

        @NotNull
        @Min(0)
        Long locationId,

        @NotBlank
        String name
){}
