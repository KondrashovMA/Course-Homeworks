package ru.spring.core.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.spring.core.repository.AccountRepository;
import ru.spring.core.model.Account;
import ru.spring.core.model.User;

import java.util.List;

@Service
public class AccountService {

    @Value("${account.default-amount}")
    private long defaultAccountMoneyAmount;

    @Value("${account.transfer-commission}")
    private long defaultCommission;

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createEmptyAccount() {
        Account account = new Account();
        account.setMoneyAmount(defaultAccountMoneyAmount);
        return account;
    }

    public Account createAccountForUser(User user) {
        Account account = new Account();
        account.setUser(user);
        account.setMoneyAmount(defaultAccountMoneyAmount);

        Account createdAccount = accountRepository.saveAccount(account);

        System.out.println(String.format("Account with id %d successfully created", createdAccount.getId()));
        return createdAccount;
    }

    public boolean checkAccountExistsById(long id) {
        return accountRepository.checkAccountExistsById(id);
    }

    public void addMoneyToAccountByAccountId(long accountId, long moneyAmount){
        if(moneyAmount < 0) {
            System.out.println("Amount can't be below zero");
            return;
        }
        accountRepository.addMoneyToAccountByAccountId(accountId, moneyAmount);
        System.out.println(String.format("Successfully added %d money to account %d", moneyAmount, accountId));
    }

    public void withdrawMoneyToAccountByAccountId(long id, long moneyAmount){
        if(moneyAmount < 0) {
            System.out.println("Amount can't be below zero");
            return;
        }
        Account account = accountRepository.getAccountById(id);
        if(moneyAmount > account.getMoneyAmount()){
            System.out.println("An attempt to withdraw more money than is available on the account");
            return;
        }
        accountRepository.withdrawMoneyToAccountByAccountId(id, moneyAmount);
        System.out.println(String.format("Successfully withdrawn %d money", moneyAmount));
    }

    public void transferMoneyBetweenAccounts(long idFrom, long idTo, long moneyAmount) {
        if(moneyAmount < 0) {
            System.out.println("Amount can't be below zero");
            return;
        }
        long commission = defaultCommission;
        if(accountRepository.checkDoBelongTwoAccountToOneUser(idFrom, idTo)) {
            commission = 0;
        }
        Account accountFrom =  accountRepository.getAccountById(idFrom);
        long moneyAmountWithCommission = moneyAmount * (commission + 100) / 100;
        if(accountFrom.getMoneyAmount() < moneyAmountWithCommission) {
            System.out.println("An attempt to transfer more money with commission than is available on the account");
            return;
        }
        accountRepository.transferMoneyBetweenAccounts(idFrom, idTo, moneyAmountWithCommission, moneyAmount);
        System.out.println(String.format("Money has been successfully transferred between accounts %d and %d", idFrom, idTo));
    }

    public void closeAccount(long accountId) {
        Account accountToClose = accountRepository.getAccountById(accountId);
        List<Account> allUserAccounts = accountRepository.getAllAccountsForUserByUserId(accountToClose.getUser().getId());
        if(allUserAccounts.size() <= 1) {
            System.out.println("The user has 1 or fewer accounts");
            return;
        }
        accountRepository.closeAccount(accountToClose, allUserAccounts);
        System.out.println("All money from account with id " + accountId + ". Account closed");
    }
}
