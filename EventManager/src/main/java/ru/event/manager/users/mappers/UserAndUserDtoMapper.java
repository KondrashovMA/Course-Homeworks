package ru.event.manager.users.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.event.manager.users.models.UserModel;
import ru.event.manager.users.models.UserDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserAndUserDtoMapper {

    UserDto toDto(UserModel user);

    UserModel toModel(UserDto userDto);
}
