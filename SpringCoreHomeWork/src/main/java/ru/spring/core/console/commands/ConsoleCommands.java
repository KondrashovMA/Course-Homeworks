package ru.spring.core.console.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConsoleCommands {

    USER_CREATE("USER_CREATE"),
    ACCOUNT_CREATE("ACCOUNT_CREATE"),
    ACCOUNT_CLOSE("ACCOUNT_CLOSE"),
    ACCOUNT_DEPOSIT("ACCOUNT_DEPOSIT"),
    ACCOUNT_TRANSFER("ACCOUNT_TRANSFER"),
    ACCOUNT_WITHDRAW("ACCOUNT_WITHDRAW"),
    SHOW_ALL_USERS("SHOW_ALL_USERS");

    private final String commandZName;
}
