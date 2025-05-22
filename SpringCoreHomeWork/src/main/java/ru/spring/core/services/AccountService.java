package ru.spring.core.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.spring.core.model.Account;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Value("${account.default-amount}")
    private long defaultAccountMoneyAmount;

    @Value("${account.transfer-commission}")
    private long defaultCommission;

    private Map<Long, Account> accountMap = new HashMap<>();
    private static long currentAccountId = 0;
    private final UserService userService;
    public AccountService(UserService userService) {
        this.userService = userService;
    }

    public void createAccountForUserByUserId(long userId) {
        Account account = new Account();
        account.setUserId(userId);
        account.setMoneyAmount(defaultAccountMoneyAmount);
        long accountId = currentAccountId++;
        account.setId(accountId);
        accountMap.put(accountId, account);
        userService.addAccountToUserByUserId(userId, account);

        System.out.println(String.format("Account with id %d successfully created", accountId));
    }

    public boolean checkAccountExistsById(long id) {
        return accountMap.containsKey(id);
    }

    public void addMoneyToAccountByAccountId(long accountId, long moneyAmount){
        if(moneyAmount < 0) {
            System.out.println("Amount can't be below zero");
            return;
        }
        Account account = accountMap.get(accountId);
        account.setMoneyAmount(account.getMoneyAmount() + moneyAmount);
        System.out.println(String.format("Successfully added %d money to account %d", moneyAmount, accountId));
    }

    public void withdrawMoneyToAccountByAccountId(long id, long moneyAmount){
        if(moneyAmount < 0) {
            System.out.println("Amount can't be below zero");
            return;
        }
        Account account = accountMap.get(id);
        if(moneyAmount > account.getMoneyAmount()){
            System.out.println("An attempt to withdraw more money than is available on the account");
            return;
        }
        account.setMoneyAmount(account.getMoneyAmount() - moneyAmount);
        System.out.println(String.format("Successfully withdrawn %d money", moneyAmount));
    }

    public void transferMoneyBetweenAccounts(long idFrom, long idTo, long moneyAmount) {
        if(moneyAmount < 0) {
            System.out.println("Amount can't be below zero");
            return;
        }
        long commission = defaultCommission;
        if(checkDoBelongTwoAccountToOneUser(idFrom, idTo)) {
            commission = 0;
        }
        Account accountFrom = accountMap.get(idFrom);
        long moneyAmountWithCommission = (long) moneyAmount * (commission + 100) / 100;
        if(accountFrom.getMoneyAmount() < moneyAmountWithCommission) {
            System.out.println("An attempt to transfer more money with commission than is available on the account");
            return;
        }
        this.withdrawMoneyToAccountByAccountId(idFrom, moneyAmountWithCommission);
        this.addMoneyToAccountByAccountId(idTo, moneyAmount);
        System.out.println(String.format("Money has been successfully transferred between accounts %d and %d", idFrom, idTo));
    }

    public void closeAccount(long accountId) {
        Account accountToClose = accountMap.get(accountId);
        List<Account> allUserAccounts = getAllAccountsForUserByUserId(accountToClose.getUserId());
        if(allUserAccounts.size() <= 1) {
            System.out.println("The user has 1 or fewer accounts");
            return;
        }
        long moneyAmountToTransfer = accountToClose.getMoneyAmount();
        Account accountForTransfer = allUserAccounts.stream().filter(acc -> acc.getId() != accountId).findFirst().get();
        this.addMoneyToAccountByAccountId(accountForTransfer.getId(), moneyAmountToTransfer);
        accountMap.remove(accountId);
        userService.deleteAccountFromUserByUserId(accountToClose.getUserId(), accountToClose);
        System.out.println("All money from account with id " + accountId + " was transfered to account with id "
                + accountForTransfer.getId() + ". Account closed");
    }

    private List<Account> getAllAccountsForUserByUserId(long id) {
        return accountMap.values()
                .stream()
                .filter(account -> account.getUserId() == id)
                .sorted(Comparator.comparingLong(Account::getId))
                .collect(Collectors.toList());
    }

    private boolean checkDoBelongTwoAccountToOneUser(long firstAccountId, long secondAccountId) {
        return accountMap.get(firstAccountId).getUserId() == accountMap.get(secondAccountId).getUserId();
    }
}
