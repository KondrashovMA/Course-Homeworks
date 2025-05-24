package ru.spring.core.console;

import jakarta.annotation.PostConstruct;
import org.apache.maven.surefire.shared.lang3.StringUtils;
import org.springframework.stereotype.Component;

import ru.spring.core.console.handlers.CommandHandler;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OperationsConsoleListener {

    private final List<String> QUIT_COMMANDS = List.of("quit", "q", "exit");

    private final List<CommandHandler> commandHandlers;
    private final Scanner inputScanner;

    public OperationsConsoleListener(List<CommandHandler> commandHandlers, Scanner scanner) {
        this.commandHandlers = commandHandlers;
        this.inputScanner = scanner;
    }

    @PostConstruct
    public void startHandleConsole() {
        Map<String, CommandHandler> handlerMap = commandHandlers.stream().collect(Collectors.toMap(CommandHandler::getHandlerName, Function.identity()));

        while (true) {
            System.out.println("PleaseEnter one of operation type below. If you need to stop working with service enter EXIT (also QUIT or q)");
            System.out.println("Available commands: ");
            commandHandlers.forEach(commandHandler -> System.out.println(commandHandler.getHandlerName()));

            try {
                String inputResult = inputScanner.next();

                if (StringUtils.isBlank(inputResult)) {
                    System.out.println("Your input is blank, please repeat the input: ");
                } else if (QUIT_COMMANDS.contains(inputResult)) {
                    System.out.println("Work with service is over.");
                    break;
                }

                if (handlerMap.containsKey(inputResult)) {
                    handlerMap.get(inputResult).handle();
                } else {
                    System.out.println("You have entered an unknown command, please repeat.");
                }
            } catch (RuntimeException e) {
                System.out.println("Error while working: " + e);
                break;
            }
        }
    }
}
