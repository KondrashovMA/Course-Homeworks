package ru.event.manager.users.models;

import ru.event.manager.users.models.auth.UserRoles;

public record UserModel(
        Long id,
        String login,
        String password,
        Integer age,
        UserRoles role
) {
}
