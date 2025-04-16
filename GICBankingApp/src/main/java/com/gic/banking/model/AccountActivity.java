package com.gic.banking.model;

import java.time.LocalDate;

public class AccountActivity {

    private String id;
    private LocalDate date;
    private String type;
    private double rateOrAmount;
    private double balance;

    public AccountActivity(String id, LocalDate date,  String type, double rateOrAmount, double balance) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.rateOrAmount = rateOrAmount;
        this.balance = balance;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRateOrAmount() {
        return rateOrAmount;
    }

    public void setRateOrAmount(double rateOrAmount) {
        this.rateOrAmount = rateOrAmount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
