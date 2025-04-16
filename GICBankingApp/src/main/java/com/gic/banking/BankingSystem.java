package com.gic.banking;

import com.gic.banking.handler.InterestRuleHandler;
import com.gic.banking.handler.StatementHandler;
import com.gic.banking.handler.TransactionHandler;
import com.gic.banking.repository.AccountRepository;
import com.gic.banking.repository.InterestRulesRepository;

import java.util.Scanner;

import static com.gic.banking.util.MessageConstants.*;

public class BankingSystem {
    private final TransactionHandler transactionHandler;
    private final InterestRuleHandler interestRuleHandler;
    private final StatementHandler statementPrinter;
    private final Scanner scanner = new Scanner(System.in); //Accepts user inputs into the application

    public BankingSystem(TransactionHandler transactionHandler,
                         InterestRuleHandler interestRuleHandler,
                         StatementHandler statementPrinter) {
        this.transactionHandler = transactionHandler;
        this.interestRuleHandler = interestRuleHandler;
        this.statementPrinter = statementPrinter;
    }

    /**
     * Main entry point for the BankingSystem application.
     * Initializes the BankingSystem and starts the main loop.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            AccountRepository accountRepository = new AccountRepository();
            InterestRulesRepository interestRulesRepository = new InterestRulesRepository();
            TransactionHandler transactionHandler = new TransactionHandler(accountRepository);
            InterestRuleHandler interestRuleHandler = new InterestRuleHandler(interestRulesRepository);
            StatementHandler statementPrinter = new StatementHandler(accountRepository, interestRulesRepository);

            BankingSystem bankingSystem = new BankingSystem(transactionHandler, interestRuleHandler, statementPrinter);
            bankingSystem.run();
        } catch (Exception exception) {
            System.out.println("An unexpected error occurred: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    /**
     * Main loop for the banking system. Displays a welcome prompt and processes user input.
     * Continues running until the user chooses to quit.
     */
    private void run() {
        while (true) {
            System.out.println(WELCOME_PROMPT);
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "T" -> transactionHandler.handle(); // Handle transaction input.
                case "I" -> interestRuleHandler.handle(); // Handle interest rule definition.
                case "P" -> statementPrinter.handle(); // Handle printing account statements.
                case "Q" -> {
                    System.out.println(THANK_YOU_MESSAGE); // Exit the application.
                    return;
                }
                default -> System.out.println(INVALID_CHOICE_ERROR); // Handle invalid choices.
            }
        }
    }

}