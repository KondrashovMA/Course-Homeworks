package ru.event.manager.users.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.event.manager.users.models.auth.JwtResponseDto;
import ru.event.manager.users.models.auth.JwtResponseModel;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface JwtResponseMapper {

    JwtResponseDto toDto(JwtResponseModel jwtResponseModel);

    JwtResponseModel toModel(JwtResponseDto jwtResponseDto);
}
