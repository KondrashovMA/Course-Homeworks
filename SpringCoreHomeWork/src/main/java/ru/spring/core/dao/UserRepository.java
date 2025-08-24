package ru.spring.core.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import ru.spring.core.model.Account;
import ru.spring.core.model.User;
import ru.spring.core.services.AccountService;
import ru.spring.core.services.TransactionsService;

import java.util.List;

@Component
public class UserRepository {

    private final SessionFactory sessionFactory;
    private final TransactionsService transactionsService;
    private final AccountService accountService;

    public UserRepository(SessionFactory sessionFactory, TransactionsService transactionsService, AccountService accountService) {
        this.sessionFactory = sessionFactory;
        this.transactionsService = transactionsService;
        this.accountService = accountService;
    }

    public User getUserById(long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, userId);
        }
    }

    public User createUserByLogin(User createdUser) {
        return transactionsService.executeTransaction(session -> {
            Account account = accountService.createEmptyAccount();
            account.setUser(createdUser);
            createdUser.getAccountList().add(account);

            session.persist(createdUser);
            return createdUser;
        });
    }

    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select s from User s", User.class).list();
        }
    }

    public boolean checkUserExistsById(long userId) {
        try (Session session = sessionFactory.openSession()) {
            String hql =  "SELECT COUNT(u) > 0 FROM User u WHERE u.id = :id";

            return session.createQuery(hql, Boolean.class)
                    .setParameter("id", userId)
                    .getSingleResult();
        }
    }

    public boolean isUserUniqueLogin(String login) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(u) > 0 " +
                    "FROM User u " +
                    "WHERE u.login = :login";
            return session.createQuery(hql, Boolean.class)
                    .setParameter("login", login)
                    .getSingleResult();
        }
    }
}
