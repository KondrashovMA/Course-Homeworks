package ru.event.manager.users.models;

public record User(
        Long id,
        String login,
        String password,
        Integer age,
        String role
) {
}
