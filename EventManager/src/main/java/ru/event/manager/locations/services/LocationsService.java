package ru.event.manager.locations.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.xml.bind.ValidationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.event.manager.locations.mappers.LocationAndLocationEntityMapper;
import ru.event.manager.locations.models.Location;
import ru.event.manager.locations.models.LocationEntity;
import ru.event.manager.locations.repositories.LocationsRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationsService {
    private final LocationAndLocationEntityMapper locationAndLocationEntityMapper;
    private final LocationsRepository locationsRepository;

    public LocationsService(LocationAndLocationEntityMapper locationAndLocationEntityMapper, LocationsRepository locationsRepository) {
        this.locationAndLocationEntityMapper = locationAndLocationEntityMapper;
        this.locationsRepository = locationsRepository;
    }

    public List<Location> getAllLocations(String name, Integer capacity, Integer pageNumber, Integer pageSize) {
        Pageable pageable = Pageable
                .ofSize(Optional.ofNullable(pageSize).orElse(100))
                .withPage(Optional.ofNullable(pageNumber).orElse(0));
        List<LocationEntity> locationEntities = locationsRepository.getLocations(name, capacity, pageable);

        return Optional.ofNullable(locationEntities)
                .map(entities -> entities.stream().map(locationAndLocationEntityMapper::toModel).collect(Collectors.toList()))
                .orElse(List.of());
    }

    public Location createLocation(Location location) throws ValidationException {
        if(locationsRepository.existsByNameIs(location.name())) {
            throw new ValidationException("Location with name %s already exists".formatted(location.name()));
        }
        LocationEntity createdLocationEntity = locationsRepository.save(locationAndLocationEntityMapper.toEntity(location));
        return locationAndLocationEntityMapper.toModel(createdLocationEntity);
    }

    public void deleteLocationById(Long id) {
        //todo в следующих итерациях реализовать логику проверки наличия событий на этой локации

        if(!locationsRepository.existsById(id)) {
            throw new NoSuchElementException("Not found location with id=%s".formatted(id));
        }
        locationsRepository.deleteById(id);
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
        LocationEntity locationToUpdateEntity = locationAndLocationEntityMapper.toEntity(locationToUpdate);
        LocationEntity locationEntity = locationsRepository.findById(id).get();
        if(locationEntity.getCapacity() > locationToUpdateEntity.getCapacity()) {
            throw new RuntimeException("Cannot change location capacity to value less than current value");
        }
        locationToUpdateEntity.setId(id);
        LocationEntity updatedLocationEntity = locationsRepository.save(locationToUpdateEntity);
        return locationAndLocationEntityMapper.toModel(updatedLocationEntity);
    }
}
