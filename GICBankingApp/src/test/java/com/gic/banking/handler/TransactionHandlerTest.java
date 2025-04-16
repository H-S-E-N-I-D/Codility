package com.gic.banking.handler;

import com.gic.banking.repository.AccountRepository;
import com.gic.banking.util.MessageConstants;
import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionHandlerTest {

    @Test
    void testInvalidTransactionInputFormat() throws Exception {
        String input = "20230505 AC001 D 100.00\n " +
                "20230601 AC001 D 150.00\n " +
                "20230626 AC001 W\n";

        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn(input).execute(() -> {
                            AccountRepository repository = new AccountRepository();
                            TransactionHandler handler = new TransactionHandler(repository);
                            handler.handle();
                        }
                )
        );
        assertTrue(output.contains(MessageConstants.INVALID_INPUT_FORMAT_ERROR), "Output Message is incorrect");
    }

    @Test
    void testInsufficientBalanceOnWithdrawal() throws Exception {
        String input = "20230505 AC001 W 100.00\n";

        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn(input).execute(() -> {
                            AccountRepository repository = new AccountRepository();
                            TransactionHandler handler = new TransactionHandler(repository);
                            handler.handle();
                        }
                )
        );
        assertTrue(output.contains(MessageConstants.INSUFFICIENT_BALANCE_ERROR), "Output Message is incorrect");
    }

    @Test
    void testInvalidTransactionDate() throws Exception {
        String input = "202305055524 AC001 D 100.00\n";

        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn(input).execute(() -> {
                            AccountRepository repository = new AccountRepository();
                            TransactionHandler handler = new TransactionHandler(repository);
                            handler.handle();
                        }
                )
        );
        assertTrue(output.contains(MessageConstants.INVALID_TXN_DATE_FORMAT_ERROR), "Output Message is incorrect");
    }

    @Test
    void testInvalidTransactionType() throws Exception {
        String input = "20230505 AC001 X 100.00\n";

        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn(input).execute(() -> {
                            AccountRepository repository = new AccountRepository();
                            TransactionHandler handler = new TransactionHandler(repository);
                            handler.handle();
                        }
                )
        );
        assertTrue(output.contains(MessageConstants.INVALID_TRANSACTION_TYPE_ERROR), "Output Message is incorrect");
    }

    @Test
    void testInvalidTransactionAmount() throws Exception {
        String input = "20230505 AC001 D 100.11651\n";

        String output = SystemLambda.tapSystemOutNormalized(() ->
                SystemLambda.withTextFromSystemIn(input).execute(() -> {
                            AccountRepository repository = new AccountRepository();
                            TransactionHandler handler = new TransactionHandler(repository);
                            handler.handle();
                        }
                )
        );
        assertTrue(output.contains(MessageConstants.INVALID_AMOUNT_FORMAT_ERROR), "Output Message is incorrect");
    }

}