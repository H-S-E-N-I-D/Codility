package com.gic.banking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTest {

    @Test
    void testTransactionConstructorAndGetters() {
        LocalDate date = LocalDate.now();
        Transaction transaction = new Transaction("T1", date, "D", 100.0, 200.0);

        assertEquals("T1", transaction.getId());
        assertEquals(date, transaction.getDate());
        assertEquals("D", transaction.getType());
        assertEquals(100.0, transaction.getAmount());
        assertEquals(200.0, transaction.getBalance());
    }

    @Test
    void testSetters() {
        LocalDate date = LocalDate.now();
        Transaction transaction = new Transaction("T1", date, "D", 100.0, 200.0);

        transaction.setId("T2");
        transaction.setDate(date.plusDays(1));
        transaction.setType("W");
        transaction.setAmount(50.0);
        transaction.setBalance(150.0);

        assertEquals("T2", transaction.getId());
        assertEquals(date.plusDays(1), transaction.getDate());
        assertEquals("W", transaction.getType());
        assertEquals(50.0, transaction.getAmount());
        assertEquals(150.0, transaction.getBalance());
    }

}