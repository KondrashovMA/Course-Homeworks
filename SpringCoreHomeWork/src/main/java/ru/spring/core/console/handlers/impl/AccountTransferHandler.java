package ru.spring.core.console.handlers.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.spring.core.console.handlers.CommandHandler;
import ru.spring.core.services.AccountService;

import java.util.Scanner;

@Component
@Slf4j
public class AccountTransferHandler implements CommandHandler {

    private final String COMMAND_NAME = "ACCOUNT_TRANSFER";

    private final AccountService accountService;
    public AccountTransferHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void handle() {
        try {
            Scanner inputScanner = new Scanner(System.in);
            System.out.println("Enter the ID of the account that the money is being transferred from: ");

            String inputResult = inputScanner.next();
            long idFrom = Long.parseLong(inputResult);

            System.out.println("Enter the ID of the account that the money is being transferred to: ");
            inputResult = inputScanner.next();
            long idTo = Long.parseLong(inputResult);


            if(accountService.checkAccountExistsById(idFrom) && accountService.checkAccountExistsById(idTo)) {
                System.out.println("Enter the amount of money is being transferred: ");
                inputResult = inputScanner.next();
                long money = Long.parseLong(inputResult);

                accountService.transferMoneyBetweenAccounts(idFrom, idTo, money);
            } else{
                System.out.println("A non-existent ids for accounts has been entered, please repeat the command and enter the correct ids");
            }
        } catch (NumberFormatException e) {
            log.error("If you entered a wrong number, please repeat the command and enter the correct id (an integer from 0 or more)");
        }
    }

    @Override
    public String getHandlerName() {
        return this.COMMAND_NAME;
    }
}
