package com.gic.banking.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InputValidations {

    // Reuse SimpleDateFormat objects to avoid repeated instantiation
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat PERIOD_FORMAT = new SimpleDateFormat("yyyyMM");

    static {
        DATE_FORMAT.setLenient(false); // Strict parsing for dates
        PERIOD_FORMAT.setLenient(false); // Strict parsing for periods
    }

    // Reuse DateTimeFormatter for modern date validation
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    /**
     * Validates if the input string is a valid date in the format "yyyyMMdd".
     *
     * @param dateStr The date string to validate.
     * @return true if the date is valid, false otherwise.
     */
    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.length() != 8) {
            return false; // Ensure it is exactly 8 digits
        }
        try {
            DATE_FORMAT.parse(dateStr); // Attempt to parse the date
            return true;
        } catch (ParseException e) {
            return false; // Invalid date format
        }
    }

    /**
     * Validates if the input string is a valid period in the format "yyyyMM".
     *
     * @param periodStr The period string to validate.
     * @return true if the period is valid, false otherwise.
     */
    public static boolean isValidPeriod(String periodStr) {
        if (periodStr == null || periodStr.length() != 6) {
            return false; // Ensure it is exactly 6 digits
        }
        try {
            YearMonth.parse(periodStr, YEAR_MONTH_FORMATTER); // Use java.time for validation
            return true;
        } catch (DateTimeParseException e) {
            return false; // Invalid period format
        }
    }

    /**
     * Validates if the input string is a valid positive amount with up to 2 decimal places.
     *
     * @param amountStr The amount string to validate.
     * @return true if the amount is valid, false otherwise.
     */
    public static boolean isValidAmount(String amountStr) {
        if (amountStr == null || !amountStr.matches("^\\d+(\\.\\d{1,2})?$")) {
            return false; // Ensure numeric format with up to 2 decimal places
        }
        BigDecimal amount = new BigDecimal(amountStr);
        return amount.compareTo(BigDecimal.ZERO) > 0; // Ensure amount > 0

    }
}