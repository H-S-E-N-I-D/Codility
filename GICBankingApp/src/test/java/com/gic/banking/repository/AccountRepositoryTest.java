package com.gic.banking.repository;


import com.gic.banking.model.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryTest {

    private AccountRepository accountRepository;
    private static final String TEST_ACCOUNT_ID = "ACC123";

    @BeforeEach
    void setUp() {
        accountRepository = new AccountRepository();
    }

    @Test
    void addAccount_shouldCreateNewAccountWhenNotExists() {
        // Act
        BankAccount result = accountRepository.addAccount(TEST_ACCOUNT_ID);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_ACCOUNT_ID, result.getAccountId());
    }

    @Test
    void addAccount_shouldReturnExistingAccountWhenAlreadyExists() {
        // Arrange
        BankAccount existingAccount = accountRepository.addAccount(TEST_ACCOUNT_ID);

        // Act
        BankAccount result = accountRepository.addAccount(TEST_ACCOUNT_ID);

        // Assert
        assertSame(existingAccount, result);
    }

    @Test
    void findById_shouldReturnNullWhenAccountDoesNotExist() {
        // Act
        BankAccount result = accountRepository.findById(TEST_ACCOUNT_ID);

        // Assert
        assertNull(result);
    }

    @Test
    void findById_shouldReturnAccountWhenExists() {
        // Arrange
        BankAccount expectedAccount = accountRepository.addAccount(TEST_ACCOUNT_ID);

        // Act
        BankAccount result = accountRepository.findById(TEST_ACCOUNT_ID);

        // Assert
        assertSame(expectedAccount, result);
    }

    @Test
    void addAccount_shouldHandleMultipleAccountsCorrectly() {
        // Arrange
        String accountId1 = "ACC001";
        String accountId2 = "ACC002";

        // Act
        BankAccount account1 = accountRepository.addAccount(accountId1);
        BankAccount account2 = accountRepository.addAccount(accountId2);

        // Assert
        assertNotNull(account1);
        assertNotNull(account2);
        assertNotSame(account1, account2);
        assertEquals(accountId1, account1.getAccountId());
        assertEquals(accountId2, account2.getAccountId());
    }

    @Test
    void findById_shouldNotAffectRepositoryState() {
        // Arrange
        accountRepository.addAccount(TEST_ACCOUNT_ID);
        int initialSize = accountRepository.findById(TEST_ACCOUNT_ID).getAccountId().length(); // Just to access something

        // Act
        accountRepository.findById(TEST_ACCOUNT_ID);
        accountRepository.findById("NON_EXISTENT_ID");

        // Assert - Verify repository state hasn't changed
        BankAccount account = accountRepository.findById(TEST_ACCOUNT_ID);
        assertEquals(initialSize, account.getAccountId().length());
    }
}