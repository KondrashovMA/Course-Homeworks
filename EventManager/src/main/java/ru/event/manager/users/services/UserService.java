package ru.event.manager.users.services;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.event.manager.users.UserRoles;
import ru.event.manager.users.mappers.UserAndUserEntityMapper;
import ru.event.manager.users.models.User;
import ru.event.manager.users.models.auth.UserAuthData;
import ru.event.manager.users.models.UserEntity;
import ru.event.manager.users.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserAndUserEntityMapper userAndUserEntityMapper;
    private final UserRepository userRepository;

    public UserService(UserAndUserEntityMapper userAndUserEntityMapper, UserRepository userRepository) {
        this.userAndUserEntityMapper = userAndUserEntityMapper;
        this.userRepository = userRepository;
    }

    public User createUser(User userToCreate) {
        if(userRepository.existsByLoginIs(userToCreate.login())){
            throw new RuntimeException("Error while create User: %s because duplicate name".formatted(userToCreate.toString()));
        }

        UserEntity userEntityToCreate = userAndUserEntityMapper.toEntity(userToCreate);
        if (!isUserRoleIsUserRole(userToCreate.role())) {
            log.info("Change role to user for user: %s".formatted(userToCreate.login()));
            userEntityToCreate.setRole(UserRoles.USER.getRoleName());
        }
        UserEntity createdUser = userRepository.save(userEntityToCreate);
        return userAndUserEntityMapper.toModel(createdUser);
    }

    //todo пока доступно по любому запросу
    public User getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No found user by id=%s".formatted(id)));
        log.info("Found user by id: %s".formatted(userEntity.toString()));
        return userAndUserEntityMapper.toModel(userEntity);
    }

    //todo видимо реализуется в рамках следующей итерации со spring-security
    public String  getUserAuth(UserAuthData userAuthData) {
        return "";
    }

    private boolean isUserRoleIsUserRole(String userRole) {
        return Optional.ofNullable(userRole)
                .map(String::trim)
                .map(String::toLowerCase)
                .map(str -> str.equals(UserRoles.USER.getRoleName()))
                .orElse(false);
    }
}
