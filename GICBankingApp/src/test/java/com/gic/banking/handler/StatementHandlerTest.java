package com.gic.banking.handler;

import com.gic.banking.repository.AccountRepository;
import com.gic.banking.repository.InterestRulesRepository;
import com.gic.banking.util.MessageConstants;
import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StatementHandlerTest {

    @Test
    void testInvalidPrintStatementInputFormats() throws Exception {
        String input = "AC001\n";

        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn(input).execute(() -> {
                            InterestRulesRepository interestRulesRepository = new InterestRulesRepository();
                            AccountRepository accountRepository = new AccountRepository();
                            StatementHandler handler = new StatementHandler(accountRepository, interestRulesRepository);
                            handler.handle();
                        }
                )
        );
        assertTrue(output.contains(MessageConstants.INVALID_INPUT_FORMAT_ERROR), "Output Message is incorrect");
    }

    @Test
    void testInvalidPrintStatementInputAccount() throws Exception {
        String input = "AC001 202306\n";

        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn(input).execute(() -> {
                            InterestRulesRepository interestRulesRepository = new InterestRulesRepository();
                            AccountRepository accountRepository = new AccountRepository();
                            StatementHandler handler = new StatementHandler(accountRepository, interestRulesRepository);
                            handler.handle();
                        }
                )
        );
        assertTrue(output.contains(MessageConstants.ACCOUNT_NOT_FOUND_ERROR), "Output Message is incorrect");
    }

    @Test
    void testInvalidPrintStatementInputPeriod() throws Exception {
        String input = "AC001 202306125\n";

        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn(input).execute(() -> {
                            InterestRulesRepository interestRulesRepository = new InterestRulesRepository();
                            AccountRepository accountRepository = new AccountRepository();
                            StatementHandler handler = new StatementHandler(accountRepository, interestRulesRepository);
                            handler.handle();
                        }
                )
        );
        assertTrue(output.contains(MessageConstants.INVALID_PERIOD_FORMAT_ERROR), "Output Message is incorrect");
    }

    @Test
    void testInvalidPrintStatementInputPeriodFormat() throws Exception {
        String input = "AC001 4654654654654\n";

        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn(input).execute(() -> {
                            InterestRulesRepository interestRulesRepository = new InterestRulesRepository();
                            AccountRepository accountRepository = new AccountRepository();
                            StatementHandler handler = new StatementHandler(accountRepository, interestRulesRepository);
                            handler.handle();
                        }
                )
        );
        assertTrue(output.contains(MessageConstants.INVALID_PERIOD_FORMAT_ERROR), "Output Message is incorrect");
    }

}