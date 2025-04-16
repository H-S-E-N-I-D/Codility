package com.gic.banking.handler;

import com.gic.banking.model.BankAccount;
import com.gic.banking.repository.AccountRepository;
import com.gic.banking.repository.InterestRulesRepository;
import com.gic.banking.util.InputValidations;

import java.util.Scanner;

import static com.gic.banking.util.MessageConstants.*;

public class StatementHandler implements SystemHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final AccountRepository accountRepository;
    private final InterestRulesRepository interestRulesRepository;

    public StatementHandler(AccountRepository accountRepository, InterestRulesRepository interestRulesRepository) {
        this.accountRepository = accountRepository;
        this.interestRulesRepository = interestRulesRepository;
    }

    /**
     * Handles the printing of account statements. Prompts the user for account details and the period.
     * Allows the user to go back to the main menu by entering an empty input.
     */
    public void handle() {
        System.out.println(PRINT_STATEMENT_PROMPT);
        System.out.println(GO_BACK_PROMPT);
        System.out.print("> ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return; // Go back to the main menu if input is empty.

        String[] inputParts = input.split(" ");
        if (!validatePrintStatementPeriodInputFormat(inputParts)) return; // Validate input format.

        String accountId = inputParts[0];
        String period = inputParts[1];

        if (!validatePeriod(period)) return; // Validate the period format.

        BankAccount account = accountRepository.findById(accountId);
        if (account == null) {
            System.out.println(ACCOUNT_NOT_FOUND_ERROR); // Handle account not found.
            return;
        }
        // Print the monthly statement.
        account.printMonthlyStatement(period, interestRulesRepository.getInterestRules());
    }

    /**
     * Validates the input format for printing statements.
     *
     * @param inputParts The input parts to validate.
     * @return True if the input format is valid, otherwise false.
     */
    public static boolean validatePrintStatementPeriodInputFormat(String[] inputParts) {
        if (inputParts.length != 2) {
            System.out.println(INVALID_INPUT_FORMAT_ERROR);
            return false;
        }
        return true;
    }

    /**
     * Validates the period format for printing statements.
     *
     * @param date The period to validate.
     * @return True if the period is valid, otherwise false.
     */
    private boolean validatePeriod(String date) {
        if (!InputValidations.isValidPeriod(date)) {
            System.out.println(INVALID_PERIOD_FORMAT_ERROR);
            return false;
        }
        return true;
    }

}
