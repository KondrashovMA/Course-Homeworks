package ru.spring.core.services;

import org.springframework.stereotype.Service;
import ru.spring.core.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private List<User> createdUsers = new ArrayList<>(); // имитация БД с юзерами
    public long createUserByLoginAndGetId(String login) {
        User user = new User(login);
        long id = createdUsers.size() + 1;
        user.setId(id);
        createdUsers.add(user);
        return id;
    }
}
