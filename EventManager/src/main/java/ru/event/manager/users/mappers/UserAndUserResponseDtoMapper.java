package ru.event.manager.users.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.event.manager.users.models.UserModel;
import ru.event.manager.users.models.UserResponseDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserAndUserResponseDtoMapper {

    UserResponseDto toDto(UserModel user);

    UserModel toModel(UserResponseDto userResponseDto);

}
