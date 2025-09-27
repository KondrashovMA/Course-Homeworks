package ru.event.manager.users.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.event.manager.users.models.User;
import ru.event.manager.users.models.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserAndUserEntityMapper {

    UserEntity toEntity(User user);

    User toModel(UserEntity userEntity);
}
