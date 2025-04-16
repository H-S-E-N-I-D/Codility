package com.gic.banking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountActivityTest {

    @Test
    void testAccountActivityConstructorAndGetters() {
        LocalDate date = LocalDate.now();
        AccountActivity accountActivity = new AccountActivity("T1", date, "D", 100.0, 200.0);

        assertEquals("T1", accountActivity.getId());
        assertEquals(date, accountActivity.getDate());
        assertEquals("D", accountActivity.getType());
        assertEquals(100.0, accountActivity.getRateOrAmount());
        assertEquals(200.0, accountActivity.getBalance());
    }

    @Test
    void testSetters() {
        LocalDate date = LocalDate.now();
        AccountActivity accountActivity = new AccountActivity("T1", date, "D", 100.0, 200.0);

        accountActivity.setId("T2");
        accountActivity.setDate(date.plusDays(1));
        accountActivity.setType("W");
        accountActivity.setRateOrAmount(50.0);
        accountActivity.setBalance(150.0);

        assertEquals("T2", accountActivity.getId());
        assertEquals(date.plusDays(1), accountActivity.getDate());
        assertEquals("W", accountActivity.getType());
        assertEquals(50.0, accountActivity.getRateOrAmount());
        assertEquals(150.0, accountActivity.getBalance());
    }

}
