package ru.event.manager.locations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.event.manager.locations.models.LocationEntity;

public interface LocationsRepository extends JpaRepository<LocationEntity, Long> {

    Boolean existsByNameIs(String name);
}
