package ru.spring.core.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class User {

    private long id;

    private String login;

    private List<Account> accountList;
}
