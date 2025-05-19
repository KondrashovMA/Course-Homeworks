package ru.spring.core.console.handlers;

import org.apache.maven.surefire.shared.lang3.StringUtils;
import org.springframework.stereotype.Component;

public interface CommandHandler {

    void handle();

    String getHandlerName();
}
