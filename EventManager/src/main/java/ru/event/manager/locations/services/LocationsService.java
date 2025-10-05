package ru.event.manager.locations.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.xml.bind.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.event.manager.locations.mappers.LocationAndLocationEntityMapper;
import ru.event.manager.locations.models.Location;
import ru.event.manager.locations.models.LocationEntity;
import ru.event.manager.locations.repositories.LocationsRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class LocationsService {

    private final Logger log = LoggerFactory.getLogger(LocationsService.class);
    private final LocationAndLocationEntityMapper locationAndLocationEntityMapper;
    private final LocationsRepository locationsRepository;

    public LocationsService(LocationAndLocationEntityMapper locationAndLocationEntityMapper, LocationsRepository locationsRepository) {
        this.locationAndLocationEntityMapper = locationAndLocationEntityMapper;
        this.locationsRepository = locationsRepository;
    }

    public List<Location> getAllLocations() {
        List<LocationEntity> locationEntities = locationsRepository.findAll();

        log.info("Request for all getting All location");
        return locationEntities.stream().map(locationAndLocationEntityMapper::toModel).collect(Collectors.toList());
    }

    public Location createLocation(Location location) throws ValidationException {
        if(locationsRepository.existsByNameIs(location.name())) {
            throw new ValidationException("Location with name %s already exists".formatted(location.name()));
        }
        LocationEntity createdLocationEntity = locationsRepository.save(locationAndLocationEntityMapper.toEntity(location));
        Location createdLocation = locationAndLocationEntityMapper.toModel(createdLocationEntity);
        log.info("Created location: %s".formatted(createdLocation));
        return createdLocation;
    }

    public void deleteLocationById(Long id) {
        //todo в следующих итерациях реализовать логику проверки наличия событий на этой локации

        if(!locationsRepository.existsById(id)) {
            throw new NoSuchElementException("Not found location with id=%s".formatted(id));
        }
        locationsRepository.deleteById(id);
        log.info("Deleted location with id: %s".formatted(id));
    }

    public Location getLocationById(Long id) {
        LocationEntity location = locationsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "No found location by id=%s".formatted(id)
                )
        );
        return locationAndLocationEntityMapper.toModel(location);
    }

    public Location updateLocationById(Long id, Location locationToUpdate) {
        if(!locationsRepository.existsById(id)) {
            throw new NoSuchElementException("Not found location with id=%s".formatted(id));
        }

        Location existingLocation = getLocationById(id);

        if(existingLocation.capacity() > locationToUpdate.capacity()) {
            throw new RuntimeException("Cannot change location capacity to value less than current value");
        }

        LocationEntity locationToUpdateEntity = locationAndLocationEntityMapper.toEntity(locationToUpdate);
        locationToUpdateEntity.setId(id);
        LocationEntity updatedLocationEntity = locationsRepository.save(locationToUpdateEntity);

        Location updatedLocation = locationAndLocationEntityMapper.toModel(updatedLocationEntity);
        log.info("Updated location: %s".formatted(updatedLocation));
        return updatedLocation;
    }
}
