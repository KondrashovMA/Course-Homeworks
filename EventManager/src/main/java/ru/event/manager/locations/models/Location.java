package ru.event.manager.locations.models;

import lombok.Builder;

@Builder
public record Location(
        Long id,
        String name,
        String address,
        Integer capacity,
        String description
) {}
