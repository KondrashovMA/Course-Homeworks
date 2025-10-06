package ru.event.manager.users.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.event.manager.users.models.auth.UserRoles;
import ru.event.manager.users.mappers.UserAndUserEntityMapper;
import ru.event.manager.users.models.UserModel;
import ru.event.manager.users.models.UserEntity;
import ru.event.manager.users.repositories.UserRepository;

import java.util.Objects;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserAndUserEntityMapper userAndUserEntityMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserAndUserEntityMapper userAndUserEntityMapper, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userAndUserEntityMapper = userAndUserEntityMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserModel registerUser(UserModel userToCreate) {
        if(isUserExistsByLogin(userToCreate.login())){
            throw new RuntimeException("Error while create User: %s because duplicate name".formatted(userToCreate.toString()));
        }

        String hashedPassword = passwordEncoder.encode(userToCreate.password());

        UserEntity userEntityToCreate = userAndUserEntityMapper.toEntity(userToCreate);
        userEntityToCreate.setPassword(hashedPassword);
        if (Objects.isNull(userToCreate.role())) {
            log.info("Set role to user for user: %s".formatted(userToCreate.login()));
            userEntityToCreate.setRole(UserRoles.USER);
        }
        UserEntity createdUser = userRepository.save(userEntityToCreate);
        return userAndUserEntityMapper.toModel(createdUser);
    }

    public UserModel getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No found user by id=%s".formatted(id)));
        log.info("Found user by id: %s".formatted(userEntity.toString()));
        return userAndUserEntityMapper.toModel(userEntity);
    }

    public UserModel getUserByLogin(String login) {
        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow(() -> new EntityNotFoundException("Not found user with login %s".formatted(login)));
        return userAndUserEntityMapper.toModel(userEntity);
    }

    public boolean isUserExistsByLogin(String login) {
        return userRepository.existsByLoginIs(login);
    }
}
