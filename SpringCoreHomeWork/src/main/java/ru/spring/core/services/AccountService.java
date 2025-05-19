package ru.spring.core.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.spring.core.model.Account;

@Service
public class AccountService {
    @Value("#{T(java.lang.Long).parseLong('${account.default-amount:500}')}")
    private Long defaultAccountMoneyAmount;

    public Account createAccountForUserById(long id) {
        Account account = new Account();
        account.setUserId(id);
        account.setMoneyAmount(defaultAccountMoneyAmount);
        return account;
    }
}
