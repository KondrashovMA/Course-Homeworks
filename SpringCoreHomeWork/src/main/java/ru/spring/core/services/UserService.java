package ru.spring.core.services;

import org.apache.maven.surefire.shared.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.spring.core.dao.UserDao;
import ru.spring.core.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Primary
public class UserService {

    private final AccountService accountService;
    private final TransactionsService transactionsService;
    private final UserDao userDao;

    public UserService(AccountService accountService, TransactionsService transactionsService, UserDao userDao) {
        this.accountService = accountService;
        this.transactionsService = transactionsService;
        this.userDao = userDao;
    }

    public long createUserByLoginAndGetId(String login) {
        User user = new User(login);
        user = userDao.createUserByLogin(user);
        System.out.println(String.format("User with name %s created with id %d", login, user.getId()));
        return user.getId();
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    //todo заменить на запрос
    public boolean isUniqueLogin(String login) {
        for(User user : this.getAllUsers()) {
            if(StringUtils.equals(login, user.getLogin())){
                return false;
            }
        }
        return true;
    }

    public boolean checkUserExistsById(long id) {
        return userDao.checkUserExistsById(id);
    }

    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

}
