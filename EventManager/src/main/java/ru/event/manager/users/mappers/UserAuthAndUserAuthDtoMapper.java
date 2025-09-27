package ru.event.manager.users.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.event.manager.users.models.auth.UserAuthData;
import ru.event.manager.users.models.auth.UserAuthDataDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserAuthAndUserAuthDtoMapper {

    UserAuthDataDto toDto(UserAuthData authData);

    UserAuthData toModel(UserAuthDataDto userDto);
}
