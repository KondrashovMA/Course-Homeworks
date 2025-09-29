package ru.event.manager.locations.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.event.manager.locations.models.Location;
import ru.event.manager.locations.models.LocationDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface LocationAndLocationDtoMapper {

    LocationDto toDto(Location location);

    Location toModel(LocationDto locationDto);
}
