package ru.spring.core.console.handlers.impl;

import org.springframework.stereotype.Component;
import ru.spring.core.console.handlers.CommandHandler;
import ru.spring.core.services.AccountService;

import java.util.Scanner;

@Component
public class AccountCloseHandler implements CommandHandler {

    private final String COMMAND_NAME = "ACCOUNT_CLOSE";

    private final AccountService accountService;
    private final Scanner inputScanner;

    public AccountCloseHandler(AccountService accountService, Scanner inputScanner) {
        this.accountService = accountService;
        this.inputScanner = inputScanner;
    }
    @Override
    public void handle() {
        System.out.println("Enter id of account for closing: ");
        String inputResult = inputScanner.next();
        try {
            long id = Long.parseLong(inputResult);
            if(accountService.checkAccountExistsById(id)) {
                accountService.closeAccount(id);
            } else{
                System.out.println("A non-existent id for account has been entered, please repeat the command and enter the correct id");
            }
        } catch (NumberFormatException e) {
            System.out.println("If you entered a wrong number, please repeat the command and enter the correct id (an integer from 0 or more)");
        }
    }

    @Override
    public String getHandlerName() {
        return this.COMMAND_NAME;
    }
}
