package ru.event.manager.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.event.manager.users.models.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByLoginIs(String login);
}
