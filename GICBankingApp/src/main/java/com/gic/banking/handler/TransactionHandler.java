package com.gic.banking.handler;

import com.gic.banking.model.BankAccount;
import com.gic.banking.repository.AccountRepository;
import com.gic.banking.util.InputValidations;

import java.util.Scanner;

import static com.gic.banking.util.MessageConstants.*;

public class TransactionHandler implements SystemHandler {

    private final AccountRepository accountRepository;
    private final Scanner scanner = new Scanner(System.in);


    public TransactionHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Handles the input of transactions. Prompts the user for transaction details and processes them.
     * Allows the user to go back to the main menu by entering an empty input.
     */
    @Override
    public void handle() {
        System.out.println(INPUT_TRANSACTIONS_PROMPT);
        System.out.println(GO_BACK_PROMPT);
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return; // Go back to the main menu if input is empty.

            String[] inputParts = input.split(" ");

            if (!validateTransactionInputFormat(inputParts)) continue; // Validate input format.

            String date = inputParts[0];
            String accountId = inputParts[1];
            String type = inputParts[2];
            String amountStr = inputParts[3];

            if (!validateInput(date, type, amountStr)) continue; // Validate date, type, and amount.

            double amount = Double.parseDouble(amountStr);
            // Add new Account.
            BankAccount account = accountRepository.addAccount(accountId);

            if (!account.addTransaction(date, type, amount)) {
                System.out.println(INSUFFICIENT_BALANCE_ERROR); // Handle insufficient balance.
            } else {
                account.printStatement(); // Print the updated account statement.
            }
        }
    }

    /**
     * Validates the input format for transactions.
     *
     * @param inputParts The input parts to validate.
     * @return True if the input format is valid, otherwise false.
     */
    public static boolean validateTransactionInputFormat(String[] inputParts) {
        if (inputParts.length != 4) {
            System.out.println(INVALID_INPUT_FORMAT_ERROR);
            return false;
        }
        return true;
    }

    /**
     * Validates the input for a transaction, including date, type, and amount.
     *
     * @param date      The transaction date.
     * @param type      The transaction type ("D" for deposit, "W" for withdrawal).
     * @param amountStr The transaction amount as a string.
     * @return True if the input is valid, otherwise false.
     */
    private boolean validateInput(String date, String type, String amountStr) {
        return validateDate(date) && validateTransactionType(type) && validateAmount(amountStr);
    }

    /**
     * Validates the date format for transactions and interest rules.
     *
     * @param date The date to validate.
     * @return True if the date is valid, otherwise false.
     */
    private boolean validateDate(String date) {
        if (!InputValidations.isValidDate(date)) {
            System.out.println(INVALID_TXN_DATE_FORMAT_ERROR);
            return false;
        }
        return true;
    }

    /**
     * Validates the transaction type ("D" for deposit, "W" for withdrawal).
     *
     * @param type The transaction type to validate.
     * @return True if the type is valid, otherwise false.
     */
    private boolean validateTransactionType(String type) {
        if (!type.equals("D") && !type.equals("W")) {
            System.out.println(INVALID_TRANSACTION_TYPE_ERROR);
            return false;
        }
        return true;
    }

    /**
     * Validates the transaction amount format.
     *
     * @param amount The amount to validate.
     * @return True if the amount is valid, otherwise false.
     */
    private boolean validateAmount(String amount) {
        if (!InputValidations.isValidAmount(amount)) {
            System.out.println(INVALID_AMOUNT_FORMAT_ERROR);
            return false;
        }
        return true;
    }
}

