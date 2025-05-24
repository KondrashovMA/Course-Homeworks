package ru.spring.core.services;

import org.apache.maven.surefire.shared.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.spring.core.model.Account;
import ru.spring.core.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Primary
public class UserService {

    private Map<Long, User> createdUsers = new HashMap<>();
    private static long currentUserId = 0;
    private final AccountService accountService;

    public UserService(AccountService accountService) {
        this.accountService = accountService;
    }

    public long createUserByLoginAndGetId(String login) {
        User user = new User(login);
        long id = currentUserId++;
        user.setId(id);
        user.setAccountList(new ArrayList<>());
        Account account = accountService.createAccountForUserByUserId(id);
        createdUsers.put(id, user);
        addAccountToUserByUserId(id, account);
        System.out.println(String.format("User with name %s created with id %d", login, id));
        return id;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(createdUsers.values());
    }

    public boolean isUniqueLogin(String login) {
        for(User user : this.getAllUsers()) {
            if(StringUtils.equals(login, user.getLogin())){
                return false;
            }
        }
        return true;
    }

    public boolean checkUserExistsById(long id) {
        return createdUsers.containsKey(id);
    }

    public void addAccountToUserByUserId(long userId, Account account) {
        createdUsers.get(userId).getAccountList().add(account);
    }

}
