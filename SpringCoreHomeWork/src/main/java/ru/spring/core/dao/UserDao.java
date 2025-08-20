package ru.spring.core.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import ru.spring.core.model.Account;
import ru.spring.core.model.User;
import ru.spring.core.services.AccountService;
import ru.spring.core.services.TransactionsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserDao {

    private final SessionFactory sessionFactory;
    private final TransactionsService transactionsService;
    private final AccountService accountService;

    public UserDao(SessionFactory sessionFactory, TransactionsService transactionsService, AccountService accountService) {
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
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            Account account = accountService.createAccountWithoutSaving();
            account.setUser(createdUser);
            createdUser.setAccountList(new ArrayList<>());
            createdUser.getAccountList().add(account);

            session.persist(createdUser);
            session.getTransaction().commit();
            return createdUser;
        }
    }

    public void addAccountToUserByUserId(long userId, Account account) {
        transactionsService.executeTransaction(session -> {
            User user = getUserById(userId);
            user.getAccountList().add(account);
        });
    }

    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select s from User s", User.class).list();
        }
    }

    //todo Заменить на запрос
    public boolean checkUserExistsById(long userId) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, userId);
            return Objects.nonNull(user);
        }
    }


}
