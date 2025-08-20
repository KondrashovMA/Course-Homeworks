package ru.spring.core.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class TransactionsService {

    private final SessionFactory sessionFactory;

    public TransactionsService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void executeTransaction(Consumer<Session> action) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            action.accept(session);

            session.getTransaction().commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void executeTransaction(Consumer<Session> action, Transaction transaction) {
        try (Session session = sessionFactory.openSession()) {
            if (Objects.nonNull(transaction) && transaction.isActive()) {
                action.accept(session);
            }
        }
    }

    public <T> T executeTransaction(Function<Session, T> action) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            var result = action.apply(session);

            session.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public <T> T executeTransaction(Function<Session, T> action, Transaction transaction) {
        try (Session session = sessionFactory.openSession()) {
            if (Objects.nonNull(transaction) && transaction.isActive()) {
                var result = action.apply(session);

                session.getTransaction().commit();
                return result;
            }
        }
        return null;
    }

}
