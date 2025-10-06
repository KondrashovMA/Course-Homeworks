package ru.event.manager.users.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.event.manager.users.models.auth.UserCredentialsDto;
import ru.event.manager.users.models.auth.UserCredentialsModel;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserCredentialsModelAndUserCredentialsDtoMapper {

    UserCredentialsDto toDto(UserCredentialsModel userCredentialsModel);

    UserCredentialsModel toModel(UserCredentialsDto userCredentialsDto);
}
