package ru.spring.core.console.handlers.impl;

import org.springframework.stereotype.Component;
import ru.spring.core.console.handlers.CommandHandler;
import ru.spring.core.services.UserService;

import java.util.Scanner;

@Component
public class UserCreateHandler implements CommandHandler {

    private final String COMMAND_NAME = "USER_CREATE";

    private final UserService userService;
    private final Scanner inputScanner;

    public UserCreateHandler(UserService userService, Scanner inputScanner) {
        this.userService = userService;
        this.inputScanner = inputScanner;
    }

    @Override
    public void handle() {
        System.out.println("Please enter login for user:");
        String inputResult = inputScanner.next();
        try {
            if(userService.isUniqueLogin(inputResult)) {
                long id = userService.createUserByLoginAndGetId(inputResult);
                System.out.println("User created with id: " + id + " and login: " + inputResult);
            } else {
                System.out.println("User with name " + inputResult + " exists. Please repeat command with new Name");
            }
        } catch (RuntimeException e){
            System.out.println("Error while creating user: " + e);
        }
    }

    @Override
    public String getHandlerName() {
        return this.COMMAND_NAME;
    }
}
