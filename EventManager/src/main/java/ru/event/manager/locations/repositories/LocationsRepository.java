package ru.event.manager.locations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.event.manager.locations.models.LocationEntity;

import java.util.List;

public interface LocationsRepository extends JpaRepository<LocationEntity, Long> {

    @Query(value = """
            select * from locations
            """, nativeQuery = true)
    List<LocationEntity> getLocations();

    Boolean existsByNameIs(String name);
}
