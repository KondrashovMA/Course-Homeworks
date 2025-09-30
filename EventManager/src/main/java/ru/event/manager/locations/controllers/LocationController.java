package ru.event.manager.locations.controllers;

import jakarta.validation.Valid;
import jakarta.xml.bind.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.event.manager.locations.mappers.LocationAndLocationDtoMapper;
import ru.event.manager.locations.models.Location;
import ru.event.manager.locations.models.LocationDto;
import ru.event.manager.locations.services.LocationsService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationsService locationsService;
    private final LocationAndLocationDtoMapper locationAndLocationDtoMapper;

    public LocationController(LocationsService locationsService, LocationAndLocationDtoMapper locationAndLocationDtoMapper) {
        this.locationsService = locationsService;
        this.locationAndLocationDtoMapper = locationAndLocationDtoMapper;
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(locationsService.getAllLocations()
                        .stream()
                        .map(locationAndLocationDtoMapper::toDto)
                        .collect(Collectors.toList())
                );
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@RequestBody @Valid LocationDto locationDtoToCreate) throws ValidationException {
        Location createdLocation = locationsService.createLocation(locationAndLocationDtoMapper.toModel(locationDtoToCreate));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(locationAndLocationDtoMapper.toDto(createdLocation));
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocationById(@PathVariable("locationId") Long locationId,
                                                          @RequestBody @Valid LocationDto locationDtoToUpdate) {
        Location updatedLocation = locationsService.updateLocationById(locationId, locationAndLocationDtoMapper.toModel(locationDtoToUpdate));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationAndLocationDtoMapper.toDto(updatedLocation));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> deleteLocationById(@PathVariable("locationId") Long locationId) {
        locationsService.deleteLocationById(locationId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocationById(@PathVariable("locationId") Long locationId) {
        Location location = locationsService.getLocationById(locationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationAndLocationDtoMapper.toDto(location));
    }
}
