package ru.spring.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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
                "; " +
                "Login = " +
                login;
    }
}
