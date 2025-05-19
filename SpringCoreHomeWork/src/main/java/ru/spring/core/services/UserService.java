package ru.spring.core.services;

import org.apache.maven.surefire.shared.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.spring.core.model.Account;
import ru.spring.core.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    
    private Map<Long, User> createdUsers = new HashMap<>();

    public long createUserByLoginAndGetId(String login) {
        User user = new User(login);
        long id = createdUsers.size() + 1;
        user.setId(id);
        user.setAccountList(new ArrayList<>());
        createdUsers.put(id, user);
        return id;
    }

    public List<User> getAllUsers() {
        return (List<User>) createdUsers.values();
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

    public void addAccountToUserByUserId(long id, Account account) {
        for(User user : this.getAllUsers()) {
            if(user.getId() == id){
                user.getAccountList().add(account);
            }
        }
    }

}
