package ru.event.manager.locations.controllers.filters;

public record LocationsFilter(
    String name,
    Integer capacity,
    Integer pageNumber,
    Integer pageSize
){}
