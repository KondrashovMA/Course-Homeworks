package ru.spring.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Scanner;

@Configuration
@PropertySource("classpath:application.properties")
public class SpringConfig {

    @Bean
    public Scanner createScanner() {
        return new Scanner(System.in);
    }
}
