package ru.spring.core.console.handlers.impl;

import org.springframework.stereotype.Component;
import ru.spring.core.console.handlers.CommandHandler;
import ru.spring.core.services.AccountService;
import ru.spring.core.services.UserService;

import java.util.Scanner;

@Component
public class AccountCreateHandler implements CommandHandler {

    private final String COMMAND_NAME = "ACCOUNT_CREATE";

    private final UserService userService;
    private final AccountService accountService;
    private final Scanner inputScanner;

    public AccountCreateHandler(UserService userService, AccountService accountService, Scanner inputScanner) {
        this.userService = userService;
        this.accountService = accountService;
        this.inputScanner = inputScanner;
    }

    @Override
    public void handle() {
        System.out.println("Please enter user id");
        String inputResult = inputScanner.next();

        try {
            long id = Long.parseLong(inputResult);
            if(userService.checkUserExistsById(id)) {
                accountService.createAccountForUserByUserId(id);
            } else{
                System.out.println("A non-existent id has been entered, please repeat the command and enter the correct id");
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
