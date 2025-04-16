package com.gic.banking.handler;

import com.gic.banking.model.InterestRule;
import com.gic.banking.repository.InterestRulesRepository;
import com.gic.banking.util.InputValidations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static com.gic.banking.util.MessageConstants.*;

public class InterestRuleHandler implements SystemHandler {

    private final InterestRulesRepository interestRulesRepository;
    private final Scanner scanner = new Scanner(System.in);
    // Formatter for parsing and formatting dates
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");


    public InterestRuleHandler(InterestRulesRepository interestRulesRepository) {
        this.interestRulesRepository = interestRulesRepository;
    }

    /**
     * Handles the definition of interest rules. Prompts the user for rule details and processes them.
     * Allows the user to go back to the main menu by entering an empty input.
     */
    @Override
    public void handle() {
        System.out.println(DEFINE_RULE_PROMPT);
        System.out.println(GO_BACK_PROMPT);
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return; // Go back to the main menu if input is empty.

            String[] inputParts = input.split(" ");

            if (!validateInterestRuleInputFormat(inputParts)) continue; // Validate input format.

            String date = inputParts[0];
            String ruleId = inputParts[1];
            String rateStr = inputParts[2];

            if (!validateDate(date) || !validateInterestRuleRateFormat(rateStr))
                continue; // Validate date and rate format.

            double rate = Double.parseDouble(rateStr);
            if (!validateInterestRuleRate(rate)) continue; // Validate rate value.

            InterestRule rule = new InterestRule(LocalDate.parse(date, dateFormatter), ruleId, rate);
            interestRulesRepository.removeInterestRule(rule); // Remove existing rule with the same date and ID.
            interestRulesRepository.addInterestRule(rule); // Add the new rule.
            printInterestRules(); // Print the updated list of interest rules.
        }
    }

    /**
     * Validates the input format for interest rules.
     *
     * @param inputParts The input parts to validate.
     * @return True if the input format is valid, otherwise false.
     */
    public static boolean validateInterestRuleInputFormat(String[] inputParts) {
        if (inputParts.length != 3) {
            System.out.println(INVALID_INPUT_FORMAT_ERROR);
            return false;
        }
        return true;
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
     * Validates the interest rate format.
     *
     * @param rate The rate to validate.
     * @return True if the rate format is valid, otherwise false.
     */
    private boolean validateInterestRuleRateFormat(String rate) {
        try {
            Double.parseDouble(rate);
        } catch (NumberFormatException e) {
            System.out.println(INVALID_RATE_FORMAT_ERROR);
            return false;
        }
        return true;
    }

    /**
     * Validates the interest rate value (must be between 0 and 100).
     *
     * @param rate The rate to validate.
     * @return True if the rate is valid, otherwise false.
     */
    public static boolean validateInterestRuleRate(double rate) {
        if (rate <= 0 || rate >= 100) {
            System.out.println(INVALID_RATE_ERROR);
            return false;
        }
        return true;
    }


    /**
     * Prints the current list of interest rules in a tabular format.
     */
    private void printInterestRules() {
        System.out.println("Interest rules:");
        System.out.println("| Date\t | RuleId\t | Rate (%)\t |");
        interestRulesRepository.getInterestRules().forEach(rule -> System.out.printf("| %s\t | %s\t | %6.2f\t |%n",
                rule.getDate().format(dateFormatter), rule.getRuleId(), rule.getRate()));
    }


}
