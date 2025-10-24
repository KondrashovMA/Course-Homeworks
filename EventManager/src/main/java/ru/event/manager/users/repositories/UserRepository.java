package ru.event.manager.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.event.manager.users.models.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByLoginIs(String login);

    Optional<UserEntity> findByLogin(String login);
}
