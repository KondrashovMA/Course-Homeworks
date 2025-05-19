package ru.spring.core.console.handlers.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.spring.core.console.handlers.CommandHandler;
import ru.spring.core.model.Account;
import ru.spring.core.model.User;
import ru.spring.core.services.AccountService;
import ru.spring.core.services.UserService;

import java.util.Scanner;

@Component
@Slf4j
public class AccountCreateHandler implements CommandHandler {

    private final String COMMAND_NAME = "ACCOUNT_CREATE";

    private final UserService userService;
    private final AccountService accountService;

    public AccountCreateHandler(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public void handle() {
        System.out.println("Please enter user id");

        Scanner inputScanner = new Scanner(System.in);
        String inputResult = inputScanner.next();

        try {
            long id = Long.parseLong(inputResult);
            if(userService.checkUserExistsById(id)) {
                Account account = accountService.createAccountForUserById(id);
                userService.addAccountToUserByUserId(id, account);
                System.out.println("New account created for user with id " + id);
            } else{
                System.out.println("A non-existent id has been entered, please repeat the command and enter the correct id");
            }
        } catch (NumberFormatException e) {
            log.error("If you entered a wrong number, please repeat the command and enter the correct id (an integer from 0 or more)");
        }
    }

    @Override
    public String getHandlerName() {
        return this.COMMAND_NAME;
    }
}
