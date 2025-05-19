package ru.spring.core.console.handlers.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.spring.core.console.handlers.CommandHandler;
import ru.spring.core.services.UserService;

import java.util.Scanner;

@Component
@Slf4j
public class UserCreateHandler implements CommandHandler {

    private final String COMMAND_NAME = "USER_CREATE";

    private final UserService userService;

    public UserCreateHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle() {
        System.out.println("Please enter login for user:");

        Scanner inputScanner = new Scanner(System.in);
        String inputResult = inputScanner.next();
        try {
            boolean isUserUnique = userService.isUniqueLogin(inputResult);
            if(isUserUnique) {
                long id = userService.createUserByLoginAndGetId(inputResult);
                System.out.println("User created with id: " + id + " and login: " + inputResult);
            } else {
                System.out.println("User with name " + inputResult + " exists. Please repeat command with new Name");
            }
        } catch (RuntimeException e){
            log.error("Error while creating user: " + e);
        }
    }

    @Override
    public String getHandlerName() {
        return this.COMMAND_NAME;
    }
}
