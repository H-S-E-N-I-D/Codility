package com.gic.banking.utils;

import com.gic.banking.util.InputValidations;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputValidationsTest {

    @Test
    void testIsValidDate() {
        // Test valid date
        assertTrue(InputValidations.isValidDate("20231015"));

        // Test invalid date (wrong format)
        assertFalse(InputValidations.isValidDate("2023-10-15"));

        // Test invalid date (wrong length)
        assertFalse(InputValidations.isValidDate("2023101"));

        // Test invalid date (non-existent date)
        assertFalse(InputValidations.isValidDate("20230230"));

        // Test null input
        assertFalse(InputValidations.isValidDate(null));
    }

    @Test
    void testIsValidPeriod() {
        // Test valid period
        assertTrue(InputValidations.isValidPeriod("202310"));

        // Test invalid period (wrong format)
        assertFalse(InputValidations.isValidPeriod("2023-10"));

        // Test invalid period (wrong length)
        assertFalse(InputValidations.isValidPeriod("20231"));

        // Test invalid period (non-existent period)
        assertFalse(InputValidations.isValidPeriod("202313"));

        // Test null input
        assertFalse(InputValidations.isValidPeriod(null));
    }

    @Test
    void testIsValidAmount() {
        // Test valid amount (integer)
        assertTrue(InputValidations.isValidAmount("100"));

        // Test valid amount (with decimal)
        assertTrue(InputValidations.isValidAmount("100.50"));

        // Test valid amount (with one decimal)
        assertTrue(InputValidations.isValidAmount("100.5"));

        // Test invalid amount (negative)
        assertFalse(InputValidations.isValidAmount("-100"));

        // Test invalid amount (more than 2 decimal places)
        assertFalse(InputValidations.isValidAmount("100.555"));

        // Test invalid amount (non-numeric)
        assertFalse(InputValidations.isValidAmount("abc"));

        // Test invalid amount (empty string)
        assertFalse(InputValidations.isValidAmount(""));

        // Test null input
        assertFalse(InputValidations.isValidAmount(null));
        assertFalse(InputValidations.isValidAmount("-155465421"));
    }

    @Test
    void testIsValidAmount2() {
        assertFalse(InputValidations.isValidAmount(".01654"));
    }
}