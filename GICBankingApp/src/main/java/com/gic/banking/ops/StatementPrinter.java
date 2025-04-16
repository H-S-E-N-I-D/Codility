package com.gic.banking.ops;

import com.gic.banking.model.InterestRule;

import java.util.Set;

public interface StatementPrinter {

    void printStatement();

    void printMonthlyStatement(String period, Set<InterestRule> interestRules);

}
