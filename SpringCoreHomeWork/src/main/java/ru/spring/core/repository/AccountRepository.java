package ru.spring.core.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import ru.spring.core.model.Account;
import ru.spring.core.services.TransactionsService;

import java.util.List;
import java.util.Objects;

@Component
public class AccountRepository {

    private final SessionFactory sessionFactory;
    private final TransactionsService transactionsService;

    public AccountRepository(SessionFactory sessionFactory, TransactionsService transactionsService) {
        this.sessionFactory = sessionFactory;
        this.transactionsService = transactionsService;
    }

    public Account getAccountById(long accountId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Account.class, accountId);
        }
    }

    public Account saveAccount(Account createdAccount) {
        return transactionsService.executeTransaction(
                session -> {
                    session.persist(createdAccount);
                    return createdAccount;
                }
        );
    }

    public void removeAccount(long accountId) {
        transactionsService.executeTransaction(session -> {
            Account account = getAccountById(accountId);
            if(Objects.nonNull(account)) session.remove(account);
        });
    }

    public boolean checkAccountExistsById(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.byId(Account.class)
                    .loadOptional(id)
                    .isPresent();
        }
    }

    public List<Account> getAllAccountsForUserByUserId(long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select account from Account account where account.user.id = :id", Account.class)
                    .setParameter("id", userId)
                    .list();
        }
    }

    public void addMoneyToAccountByAccountId(long accountId, long moneyAmount){
        transactionsService.executeTransaction(
                session -> {
                    Account account = session.get(Account.class, accountId);
                    account.setMoneyAmount(account.getMoneyAmount() + moneyAmount);
                    session.persist(account);
                }
        );
    }

    public void withdrawMoneyToAccountByAccountId(long accountId, long moneyAmount) {
        transactionsService.executeTransaction(
                session -> {
                    Account account = session.get(Account.class, accountId);
                    account.setMoneyAmount(account.getMoneyAmount() - moneyAmount);
                    session.persist(account);
                }
        );
    }

    public void transferMoneyBetweenAccounts(long idFrom, long idTo, long moneyAmountWithCommission, long moneyAmount) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            transactionsService.executeTransaction(
                    session1 -> {
                        withdrawMoneyToAccountByAccountId(idFrom, moneyAmountWithCommission);
                        addMoneyToAccountByAccountId(idTo, moneyAmount);
                    }, transaction
            );

            session.getTransaction().commit();
        }
    }

    public void closeAccount(Account accountToClose, List<Account> allUserAccounts) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            transactionsService.executeTransaction(
                    session1 -> {
                        long moneyAmountToTransfer = accountToClose.getMoneyAmount();
                        Account accountForTransfer = allUserAccounts.stream().filter(acc -> !acc.getId().equals(accountToClose.getId())).findFirst().get();
                        if(accountForTransfer.getMoneyAmount() < 0) {
                            System.out.println("Amount can't be below zero");
                            return;
                        }
                        addMoneyToAccountByAccountId(accountForTransfer.getId(), moneyAmountToTransfer);
                        removeAccount(accountToClose.getId());
                    }, transaction);

            session.getTransaction().commit();
        }
    }


    public boolean checkDoBelongTwoAccountToOneUser(long firstAccountId, long secondAccountId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = """
                            SELECT CASE WHEN COUNT(a1) > 0 THEN true ELSE false END
                                FROM Account a1
                                JOIN Account a2 ON a1.user.id = a2.user.id
                                WHERE a1.id = :accountId1
                                  AND a2.id = :accountId2
                         """;

            return session.createQuery(hql, Boolean.class)
                    .setParameter("accountId1", firstAccountId)
                    .setParameter("accountId2", secondAccountId)
                    .getSingleResult();
        }
    }

}
