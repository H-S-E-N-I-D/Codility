package com.gic.banking.handler;

import com.gic.banking.repository.InterestRulesRepository;
import com.gic.banking.util.MessageConstants;
import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InterestRuleHandlerTest {

    @Test
    void testValidInterestRuleInputFormat() throws Exception {
        String input = "20230101 RULE01 1.95\n" +
                "20230520 RULE02 1.90\n" +
                "20230615 RULE03 2.20\n\n";
        String tableHeader = "| Date\t | RuleId\t | Rate (%)\t |";
        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn(input).execute(() -> {
                            InterestRulesRepository repository = new InterestRulesRepository();
                            InterestRuleHandler handler = new InterestRuleHandler(repository);
                            handler.handle();
                        }
                )
        );
        assertTrue(output.contains(tableHeader), "Output Message is incorrect");
    }

    @Test
    void testInvalidInterestRuleInputFormat() throws Exception {
        String input = "20230101 RULE01\n";
        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn(input).execute(() -> {
                            InterestRulesRepository repository = new InterestRulesRepository();
                            InterestRuleHandler handler = new InterestRuleHandler(repository);
                            handler.handle();
                        }
                )
        );
        assertTrue(output.contains(MessageConstants.INVALID_INPUT_FORMAT_ERROR), "Output Message is incorrect");
    }

    @Test
    void testInvalidInterestRuleRate() throws Exception {
        String input = "20230101 RULE01 200.00\n";

        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn(input).execute(() -> {
                            InterestRulesRepository repository = new InterestRulesRepository();
                            InterestRuleHandler handler = new InterestRuleHandler(repository);
                            handler.handle();
                        }
                )
        );
        assertTrue(output.contains(MessageConstants.INVALID_RATE_ERROR), "Output Message is incorrect");
    }

    @Test
    void testInvalidInterestRuleRateFormat() throws Exception {
        String input = "20230101 RULE01 hello\n";

        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn(input).execute(() -> {
                            InterestRulesRepository repository = new InterestRulesRepository();
                            InterestRuleHandler handler = new InterestRuleHandler(repository);
                            handler.handle();
                        }
                )
        );
        assertTrue(output.contains(MessageConstants.INVALID_RATE_FORMAT_ERROR), "Output Message is incorrect");
    }

    @Test
    void testInvalidInterestRuleDateFormat() throws Exception {
        String input = "2023010101 RULE01 1.95\n"; // Invalid date input

        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn(input).execute(() -> {
                            InterestRulesRepository repository = new InterestRulesRepository();
                            InterestRuleHandler handler = new InterestRuleHandler(repository);
                            handler.handle();
                        }
                )
        );
        assertTrue(output.contains(MessageConstants.INVALID_TXN_DATE_FORMAT_ERROR), "Output Message is incorrect");
    }
}