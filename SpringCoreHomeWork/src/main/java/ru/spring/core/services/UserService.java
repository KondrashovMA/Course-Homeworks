package ru.spring.core.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.spring.core.repository.UserRepository;
import ru.spring.core.model.User;

import java.util.List;
import java.util.Objects;

@Service
@Primary
public class UserService {

    private final AccountService accountService;
    private final TransactionsService transactionsService;
    private final UserRepository userRepository;

    public UserService(AccountService accountService, TransactionsService transactionsService, UserRepository userRepository) {
        this.accountService = accountService;
        this.transactionsService = transactionsService;
        this.userRepository = userRepository;
    }

    public long createUserByLoginAndGetId(String login) {
        User user = new User(login);
        user = userRepository.createUserByLogin(user);
        System.out.println(String.format("User with name %s created with id %d", login, user.getId()));
        return user.getId();
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public boolean isUniqueLogin(String login) {
        Objects.requireNonNull(login, "Login cannot be null.");
        return userRepository.isUserUniqueLogin(login);
    }

    public boolean checkUserExistsById(long id) {
        return userRepository.checkUserExistsById(id);
    }

    public User getUserById(long id) {
        return userRepository.getUserById(id);
    }
}
