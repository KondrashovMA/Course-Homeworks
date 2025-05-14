package ru.spring.core.model;

import lombok.Data;

@Data
public class Account {

    private long id;

    private long userId;

    private long moneyAmount;
}
