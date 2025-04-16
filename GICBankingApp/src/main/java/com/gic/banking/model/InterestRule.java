package com.gic.banking.model;

import java.time.LocalDate;
import java.util.Objects;

public class InterestRule {
    private LocalDate date;
    private String ruleId;
    private double rate;

    public InterestRule(LocalDate date, String ruleId, double rate) {
        this.date = date;
        this.ruleId = ruleId;
        this.rate = rate;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InterestRule)) return false;
        InterestRule that = (InterestRule) o;
        return Double.compare(that.rate, rate) == 0 &&
                Objects.equals(date, that.date) &&
                Objects.equals(ruleId, that.ruleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, ruleId, rate);
    }
}
