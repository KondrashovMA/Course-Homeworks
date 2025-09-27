package ru.event.manager.locations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.event.manager.locations.models.LocationEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationsRepository extends JpaRepository<LocationEntity, Long> {

    @Query(value = """
            select * from locations
            """, nativeQuery = true)
    List<LocationEntity> getLocations();

    @Query(value = """
            select * from locations
            where (:name is null or locations.name = :name)
            and (:capacity is null or capacity >= :capacity)
            """, nativeQuery = true)
    List<LocationEntity> getLocations(String name, Integer capacity, Pageable pageable);
}
