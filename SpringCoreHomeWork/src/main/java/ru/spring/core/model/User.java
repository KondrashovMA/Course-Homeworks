package ru.spring.core.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "bank_users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    public User(String login) {
        this.login = login;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login")
    private String login;

    @OneToMany (mappedBy="user", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Account> accountList = new ArrayList<>();

    @Override
    public String toString() {
        return "User id = " +
                id +
                "; Login = " +
                login +
                "; accounts: " + Arrays.toString(accountList.toArray());
    }
}
