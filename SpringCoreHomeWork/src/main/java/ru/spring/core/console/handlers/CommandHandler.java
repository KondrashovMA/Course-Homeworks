package ru.spring.core.console.handlers;

public interface CommandHandler {

    void handle();

    String getHandlerName();
}
