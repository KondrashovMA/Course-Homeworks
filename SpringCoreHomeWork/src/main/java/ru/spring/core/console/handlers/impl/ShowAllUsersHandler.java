package ru.spring.core.console.handlers.impl;

import org.springframework.stereotype.Component;
import ru.spring.core.console.handlers.CommandHandler;
import ru.spring.core.model.User;
import ru.spring.core.services.UserService;

import java.util.List;

@Component
public class ShowAllUsersHandler implements CommandHandler {

    private final String COMMAND_NAME = "SHOW_ALL_USERS";

    private final UserService userService;

    public ShowAllUsersHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle() {
        List<User> users = userService.getAllUsers();
        if(users.isEmpty()) {
            System.out.println("No users have been created yet");
            return;
        }
        System.out.println("All created users:");
        users.forEach(System.out::println);
    }

    @Override
    public String getHandlerName() {
        return this.COMMAND_NAME;
    }
}
