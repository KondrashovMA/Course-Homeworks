package ru.event.manager.users.models.auth;

public record UserAuthData(
        String login,
        String password
) {
}
