package ru.spring.core.console.handlers.impl;

import org.springframework.stereotype.Component;
import ru.spring.core.console.handlers.CommandHandler;

@Component
public class AccountTransferHandler implements CommandHandler {

    private final String COMMAND_NAME = "ACCOUNT_TRANSFER";

    @Override
    public void handle() {

    }

    @Override
    public String getHandlerName() {
        return this.COMMAND_NAME;
    }
}
