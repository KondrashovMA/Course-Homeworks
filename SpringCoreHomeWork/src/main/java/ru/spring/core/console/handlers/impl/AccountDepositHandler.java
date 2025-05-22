package ru.spring.core.console.handlers.impl;

import org.springframework.stereotype.Component;
import ru.spring.core.console.handlers.CommandHandler;
import ru.spring.core.services.AccountService;

import java.util.Scanner;

@Component
public class AccountDepositHandler implements CommandHandler {

    private final String COMMAND_NAME = "ACCOUNT_DEPOSIT";

    private final AccountService accountService;

    public AccountDepositHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void handle() {
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Enter id of account: ");
        String inputResult = inputScanner.next();
        try {
            long id = Long.parseLong(inputResult);
            if(accountService.checkAccountExistsById(id)) {
                System.out.println("Please enter amount of money for account: ");
                inputResult = inputScanner.next();

                long money = Long.parseLong(inputResult);
                accountService.addMoneyToAccountByAccountId(id, money);
            } else{
                System.out.println("A non-existent id for account has been entered, please repeat the command and enter the correct id");
            }
        } catch (NumberFormatException e) {
            System.out.println("If you entered a wrong number, please repeat the command and enter the correct id or money amount (an integer from 0 or more)");
        }
    }

    @Override
    public String getHandlerName() {
        return this.COMMAND_NAME;
    }
}
