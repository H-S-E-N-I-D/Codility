package com.gic.banking.model;

import com.gic.banking.ops.InterestCalculator;
import com.gic.banking.ops.StatementPrinter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BankAccount implements StatementPrinter, InterestCalculator {
    private final String accountId; // Unique identifier for the bank account
    private double balance; // Current balance of the account
    private final List<Transaction> transactions; // List to store all transactions
    private final Map<String, Integer> transactionCountMap; // Map to track transaction counts per date
    private final DateTimeFormatter dateFormatter; // Formatter for parsing and formatting dates

    public BankAccount(String accountId) {
        this.accountId = accountId;
        this.balance = 0;
        this.transactions = new ArrayList<>();
        this.transactionCountMap = new HashMap<>();
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    }

    /**
     * Adds a transaction to the account.
     *
     * @param date   The date of the transaction in "yyyyMMdd" format.
     * @param type   The type of transaction ("D" for deposit, "W" for withdrawal).
     * @param amount The amount of the transaction.
     * @return true if the transaction is added successfully, false if there's insufficient balance for withdrawal.
     */
    public boolean addTransaction(String date, String type, double amount) {
        // Check for insufficient balance in case of withdrawal
        if (!checkInsufficientBalance(type, amount)) return false;

        // Increment transaction count for the given date
        int transactionCount = transactionCountMap.getOrDefault(date, 0) + 1;
        transactionCountMap.put(date, transactionCount);

        // Generate a unique transaction ID
        String transactionId = date + "-" + transactionCount;
        LocalDate transactionDate = LocalDate.parse(date, dateFormatter);

        // Update balance based on transaction type
        balance += (type.equals("D") ? amount : -amount);
        // Add the transaction to the list
        transactions.add(new Transaction(transactionId, transactionDate, type, amount, balance));

        return true;
    }

    /**
     * Prints a statement of all transactions in the account.
     */
    @Override
    public void printStatement() {
        System.out.println("Bank Account: " + accountId);
        System.out.println("| Date\t | Txn Id\t | Type\t | Amount\t | Balance\t |");
        // Print each transaction in a formatted manner
        transactions.forEach(txn -> System.out.printf("| %s\t | %s\t | %s\t | %6.2f\t | %6.2f\t |%n",
                txn.getDate().format(dateFormatter), txn.getId(), txn.getType(), txn.getAmount(), txn.getBalance()));
    }

    /**
     * Prints a monthly statement for a given period, including interest calculations.
     *
     * @param period        The period in "yyyyMM" format.
     * @param interestRules A set of interest rules applicable for the period.
     */
    @Override
    public void printMonthlyStatement(String period, Set<InterestRule> interestRules) {
        System.out.println("Account: " + accountId);
        System.out.println("| Date\t | Txn Id\t | Type\t | Amount\t | Balance\t |");

        // Parse start and end dates for the given period
        LocalDate startDate = parseDate(period + "01");
        LocalDate endDate = parseDate(period + "31");

        // Print transactions for the period and get the last transaction balance
        double lastTransactionBalance = printTransactionsForPeriod(period);
        // Calculate interest for the period
        double interest = calculateInterest(startDate, endDate, interestRules);
        // Calculate month-end balance including interest
        double monthEndBalance = lastTransactionBalance + interest;

        // Print the interest and month-end balance
        System.out.printf("| %s\t | %s\t | %s\t | %6.2f\t | %6.2f\t |%n",
                endDate.format(dateFormatter), "\t", "I", interest, monthEndBalance);
    }

    /**
     * Prints transactions for a specific period and returns the last transaction balance.
     *
     * @param period The period in "yyyyMM" format.
     * @return The balance after the last transaction in the period.
     */
    private double printTransactionsForPeriod(String period) {
        double lastBalance = 0;
        // Iterate through transactions and print those within the period
        for (Transaction txn : transactions) {
            if (txn.getDate().format(dateFormatter).startsWith(period)) {
                System.out.printf("| %s\t | %s\t | %s\t | %6.2f\t | %6.2f\t |%n",
                        txn.getDate().format(dateFormatter),
                        txn.getId(), txn.getType(), txn.getAmount(), txn.getBalance());
                lastBalance = txn.getBalance(); // Update last balance
            }
        }
        return lastBalance;
    }

    /**
     * Calculates the interest for a given period based on interest rules.
     *
     * @param startDate     The start date of the period.
     * @param endDate       The end date of the period.
     * @param interestRules A list of interest rules applicable for the period.
     * @return The calculated interest for the period.
     */
    @Override
    public double calculateInterest(LocalDate startDate, LocalDate endDate, Set<InterestRule> interestRules) {
        List<InterestRule> sortedInterestRules = new ArrayList<>(interestRules);
        // Merge transactions and interest rules into a single sorted list
        List<AccountActivity> mergedTransactions = getMergedTransactionsAndRules(startDate, endDate, sortedInterestRules);

        if (mergedTransactions.isEmpty()) return 0.00; // if no transactions, no interest

        double endOfDayBalance = 0.0;
        double totalInterest = 0.0;
        Iterator<AccountActivity> iterator = mergedTransactions.iterator();
        AccountActivity currentActivity = iterator.next();

        // Iterate through merged transactions and calculate daily interest
        while (iterator.hasNext()) {
            AccountActivity nextActivity = iterator.next();

            if (!"R".equals(currentActivity.getType())) {
                endOfDayBalance = currentActivity.getBalance(); // Update balance for non-rule transactions
            }

            LocalDate currentDate = currentActivity.getDate();
            LocalDate nextDate = nextActivity.getDate().minusDays(1);

            // Calculate days between current and next transaction
            int daysBetween = (iterator.hasNext())
                    ? (int) ChronoUnit.DAYS.between(currentDate, nextDate) + 1
                    : (int) ChronoUnit.DAYS.between(currentDate, nextActivity.getDate().plusDays(1));

            if (!iterator.hasNext()) {
                nextDate = nextActivity.getDate();
            }

            // Calculate daily interest and add to total interest
            double dailyInterest = endOfDayBalance * findEffectiveRate(sortedInterestRules, nextDate)
                    / 100 * daysBetween;
            totalInterest += dailyInterest;
            currentActivity = nextActivity;
        }

        // Round the total interest to 2 decimal places
        return Math.round(totalInterest / 365 * 100.0) / 100.0;
    }

    /**
     * Merges transactions and interest rules into a single AccountActivity list sorted by date .
     *
     * @param startDate     The start date of the period.
     * @param endDate       The end date of the period.
     * @param interestRules A list of interest rules applicable for the period.
     * @return A sorted list of merged transactions and rules.
     */
    private List<AccountActivity> getMergedTransactionsAndRules
    (LocalDate startDate, LocalDate endDate, List<InterestRule> interestRules) {
        // Filter transactions within the period
        List<AccountActivity> transactionsInPeriod = filterAndMapTransactionsToActivities(startDate, endDate);
        // Map interest rules to transactions
        List<AccountActivity> ruleDates = mapRulesToActivities(interestRules, startDate, endDate);

        // Add a dummy transaction for the end date
        transactionsInPeriod.add(new AccountActivity("", endDate, "R", 0.00, 0.00));

        // Merge and sort transactions and rules by date
        return Stream.concat(ruleDates.stream(), transactionsInPeriod.stream())
                .collect(Collectors.toMap(
                        AccountActivity::getDate,
                        accountActivity -> accountActivity,
                        (existing, replacement) -> replacement
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(AccountActivity::getDate))
                .collect(Collectors.toList());
    }

    /**
     * Filters transactions within a specified date range.
     *
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return A list of transactions within the range mapped as a list of AccountActivity.
     */
    private List<AccountActivity> filterAndMapTransactionsToActivities(LocalDate startDate, LocalDate endDate) {
        return transactions.stream()
                .filter(txn -> !txn.getDate().isBefore(startDate) && !txn.getDate().isAfter(endDate))
                .map(txn -> new AccountActivity(txn.getId(), txn.getDate(), txn.getType(), txn.getAmount(), txn.getBalance()))
                .collect(Collectors.toList());
    }

    /**
     * Maps interest rules to transactions for a specified date range.
     *
     * @param interestRules A list of interest rules.
     * @param startDate     The start date of the range.
     * @param endDate       The end date of the range.
     * @return A list of transactions representing interest rules.
     */
    private List<AccountActivity> mapRulesToActivities
    (List<InterestRule> interestRules, LocalDate startDate, LocalDate endDate) {
        return interestRules.stream()
                .filter(rule -> !rule.getDate().isBefore(startDate) && !rule.getDate().isAfter(endDate))
                .map(rule -> new AccountActivity(rule.getRuleId(), rule.getDate(), "R", rule.getRate(), 0.00))
                .collect(Collectors.toList());
    }

    /**
     * Finds the effective interest rate for a given date.
     *
     * @param interestRules A list of interest rules.
     * @param date          The date for which to find the effective rate.
     * @return The effective interest rate for the date.
     */
    private double findEffectiveRate(List<InterestRule> interestRules, LocalDate date) {
        return interestRules.stream()
                .filter(rule -> !rule.getDate().isAfter(date))
                .max(Comparator.comparing(InterestRule::getDate))
                .map(InterestRule::getRate)
                .orElse(0.0); // Default to 0.0 if no rule is found
    }

    /**
     * Parses a date string into a LocalDate object.
     *
     * @param dateStr The date string in "yyyyMMdd" format.
     * @return The parsed LocalDate object.
     * @throws IllegalArgumentException if the date string is invalid.
     */
    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
        }
    }

    /**
     * Validates the available balance is sufficient for withdrawal.
     *
     * @param type   The transaction type ("D" for deposit, "W" for withdrawal).
     * @param amount The withdrawal amount.
     * @return True if current account balance is sufficient for withdrawal.
     */
    private boolean checkInsufficientBalance(String type, double amount) {
        if (type.equals("W") && (balance - amount < 0)) {
            return false; // Insufficient balance for withdrawal
        }
        return true;
    }

    /**
     * Sets the transactions for the account (used for testing or initialization).
     *
     * @param transactions The list of transactions to set.
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions.clear();
        this.transactions.addAll(transactions);
    }

    /**
     * Returns the current balance of the account.
     *
     * @return The current balance.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Returns the list of transactions for the account.
     *
     * @return The list of transactions.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }


    /**
     * Returns the accountId of an account
     *
     * @return The accountId of an account
     */
    public String getAccountId() {
        return accountId;
    }
}