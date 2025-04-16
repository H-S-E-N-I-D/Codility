package com.gic.banking.util;

public class MessageConstants {

    public static final String INVALID_TXN_DATE_FORMAT_ERROR = "Invalid Date format. Use 'YYYYMMdd' format instead.";
    public static final String INVALID_PERIOD_FORMAT_ERROR = "Invalid Date format. Use 'YYYYMM' format instead.";
    public static final String INVALID_TRANSACTION_TYPE_ERROR = "Invalid transaction type. Use 'D' for deposit or 'W' for withdrawal.";
    public static final String INVALID_AMOUNT_FORMAT_ERROR = "Invalid amount format.";
    public static final String INVALID_RATE_FORMAT_ERROR = "Invalid rate format.";
    public static final String INVALID_RATE_ERROR = "Rate should be between 0 and 100.";
    public static final String INVALID_INPUT_FORMAT_ERROR = "Invalid input format. Try again.";
    public static final String INVALID_CHOICE_ERROR = "Invalid choice. Please try again.";
    public static final String INSUFFICIENT_BALANCE_ERROR = "Transaction failed due to insufficient balance.";
    public static final String ACCOUNT_NOT_FOUND_ERROR = "Account not found.";
    public static final String INVALID_DATE_FORMAT_ERROR = "Invalid date format:";

    public static final String WELCOME_PROMPT = "Welcome to AwesomeGIC Bank! What would you like to do?\n" +
            "[T] Input transactions \n" +
            "[I] Define interest rules\n" +
            "[P] Print statement\n" +
            "[Q] Quit\n" +
            "> ";
    public static final String GO_BACK_PROMPT = "or enter blank to go back to main menu):";
    public static final String DEFINE_RULE_PROMPT = "Please enter interest rules details in <Date> <RuleId> <Rate in %> format";
    public static final String PRINT_STATEMENT_PROMPT = "Please enter account and month to generate the statement <Account> <Year><Month>";
    public static final String INPUT_TRANSACTIONS_PROMPT = "Please enter transaction details in <Date> <Account> <Type> <Amount> format";

    public static final String THANK_YOU_MESSAGE = "Thank you for banking with AwesomeGIC Bank.\nHave a nice day!";

}
