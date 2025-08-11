package ru.spring.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class User {

    public User(String login) {
        this.login = login;
    }

    private long id;

    private String login;

    private List<Account> accountList;

    @Override
    public String toString() {
        return "User id = " +
                id +
                "; Login = " +
                login +
                "; accounts: " + Arrays.toString(accountList.toArray());
    }
}
