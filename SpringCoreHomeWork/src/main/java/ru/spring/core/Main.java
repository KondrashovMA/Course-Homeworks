package ru.spring.core;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.spring.core.console.OperationsConsoleListener;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("ru.spring.core");

        OperationsConsoleListener operationsConsoleListener = context.getBean(OperationsConsoleListener.class);
        operationsConsoleListener.startHandleConsole();
    }
}
