package ru.event.manager.users.models.auth;

public record UserCredentialsModel(
        String login,
        String password
) {
}
