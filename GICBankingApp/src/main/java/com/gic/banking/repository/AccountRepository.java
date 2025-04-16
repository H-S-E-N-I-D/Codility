package com.gic.banking.repository;

import com.gic.banking.model.BankAccount;

import java.util.HashMap;
import java.util.Map;

public class AccountRepository {
    //Stores each bank account against unique account-id
    private final Map<String, BankAccount> accounts = new HashMap<>();

    public BankAccount addAccount(String accountId) {
        BankAccount account = accounts.computeIfAbsent(accountId, BankAccount::new);
        return account;
    }

    public BankAccount findById(String accountId) {
        return accounts.get(accountId);
    }


}
