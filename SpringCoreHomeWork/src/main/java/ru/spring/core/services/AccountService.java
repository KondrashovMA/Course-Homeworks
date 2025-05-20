package ru.spring.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.spring.core.model.Account;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountService {

    @Value("#{T(java.lang.Long).parseLong('${account.default-amount:500}')}")
    private long defaultAccountMoneyAmount;

    @Value("#{T(java.lang.Long).parseLong('${account.transfer-commission:5}')}")
    private long commission;

    private Map<Long, Account> accountMap = new HashMap<>();

    public Account createAccountForUserByUserId(long id) {
        Account account = new Account();
        account.setUserId(id);
        account.setMoneyAmount(defaultAccountMoneyAmount);
        long accountId = accountMap.size() + 1;
        account.setId(accountId);
        accountMap.put(accountId, account);
        System.out.println(String.format("Account with id %d successfully created", accountId));
        return account;
    }

    public boolean checkAccountExistsById(long id) {
        return accountMap.containsKey(id);
    }

    public void addMoneyToAccountByAccountId(long id, long moneyAmount){
        if(moneyAmount < 0) {
            log.error("Amount can't be below zero");
            return;
        }
        Account account = accountMap.get(id);
        account.setMoneyAmount(account.getMoneyAmount() + moneyAmount);
        System.out.println(String.format("Successfully added %d money", moneyAmount));
    }

    public void withdrawMoneyToAccountByAccountId(long id, long moneyAmount){
        if(moneyAmount < 0) {
            log.error("Amount can't be below zero");
            return;
        }
        Account account = accountMap.get(id);
        if(moneyAmount > account.getMoneyAmount()){
            log.error("An attempt to withdraw more money than is available on the account");
            return;
        }
        account.setMoneyAmount(account.getMoneyAmount() - moneyAmount);
        System.out.println(String.format("Successfully withdrawn %d money", moneyAmount));
    }

    public void transferMoneyBetweenAccounts(long idFrom, long idTo, long moneyAmount) {
        if(moneyAmount < 0) {
            log.error("Amount can't be below zero");
            return;
        }
        Account accountFrom = accountMap.get(idFrom);
        long moneyAmountWithCommission = (long) moneyAmount * (commission + 100) / 100;
        if(accountFrom.getMoneyAmount() < moneyAmountWithCommission) {
            log.error("An attempt to transfer more money with commission than is available on the account");
        }
        Account accountTo = accountMap.get(idTo);
        accountFrom.setMoneyAmount(accountFrom.getMoneyAmount() - moneyAmountWithCommission);
        accountTo.setMoneyAmount(accountTo.getMoneyAmount() + moneyAmount);
        System.out.println(String.format("Money has been successfully transferred between accounts %d and %d", idFrom, idTo));
    }

    public void closeAccount(long id) {
        Account account = accountMap.get(id);
        List<Account> allUserAccounts = getAllAccountsForUserByUserId(account.getUserId());
        if(allUserAccounts.size() <= 1) {
            log.error("The user has 1 or fewer accounts");
        }
        long moneyAmountToTransfer = account.getMoneyAmount();
        Account accountForTransfer = allUserAccounts.stream().filter(acc -> acc.getId() != id).findFirst().get();
        this.addMoneyToAccountByAccountId(accountForTransfer.getId(), moneyAmountToTransfer);
        accountMap.remove(id);
        System.out.println("All money from account with id " + id + " was transfered to account with id "
                + accountForTransfer.getId() + ". Account closed");
    }

    private List<Account> getAllAccountsForUserByUserId(long id) {
        return accountMap.values()
                .stream()
                .filter(account -> account.getUserId() == id)
                .sorted(Comparator.comparingLong(Account::getId))
                .collect(Collectors.toList());
    }
}
