package com.gic.banking.model;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void testAddTransaction() {
        BankAccount account = new BankAccount("A1");

        assertTrue(account.addTransaction("20231001", "D", 100.0));
        assertEquals(100.0, account.getBalance());

        assertFalse(account.addTransaction("20231001", "W", 150.0)); // Insufficient balance
        assertEquals(100.0, account.getBalance());

        assertTrue(account.addTransaction("20231001", "W", 50.0));
        assertEquals(50.0, account.getBalance());
    }

    @Test
    void testPrintStatement() throws Exception {
        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn("").execute(() -> {
                            BankAccount account = new BankAccount("A1");
                            account.addTransaction("20231001", "D", 100.0);
                            account.addTransaction("20231002", "W", 50.0);
                            account.printStatement();
                        }
                )
        );
        assertTrue(output.contains("Bank Account: A1"), "Output Message is incorrect");

    }

    @Test
    void testPrintMonthlyStatement() throws Exception {

        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn("").execute(() -> {
                            BankAccount account = new BankAccount("A1");
                            account.addTransaction("20231001", "D", 100.0);
                            account.addTransaction("20231015", "W", 50.0);

                            Set<InterestRule> interestRules = new HashSet<>();

                            account.printMonthlyStatement("202310", interestRules);
                        }
                )
        );
        assertTrue(output.contains("| 20231031\t | \t\t | I\t |   0.00\t |  50.00\t |"), "Output Message is incorrect");


    }

    @Test
    void testStatementForInvalidMonth() {
        BankAccount account = new BankAccount("A1");
        account.addTransaction("20231001", "D", 100.0);
        account.addTransaction("20231015", "W", 50.0);

        Set<InterestRule> interestRules = new HashSet<>();

        // checks whether an IllegalArgumentException is thrown for invalid date format
        assertThrows(IllegalArgumentException.class, () -> {
            account.printMonthlyStatement("20231010", interestRules);
        });
    }


    @Test
    void testSetTransactions() {
        BankAccount account = new BankAccount("A1");
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("T1", LocalDate.now(), "D", 100.0, 100.0));

        account.setTransactions(transactions);
        assertEquals(1, account.getTransactions().size());
    }
}