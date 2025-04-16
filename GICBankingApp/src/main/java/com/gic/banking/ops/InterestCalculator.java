package com.gic.banking.ops;

import com.gic.banking.model.InterestRule;

import java.time.LocalDate;
import java.util.Set;

public interface InterestCalculator {

    double calculateInterest(LocalDate startDate, LocalDate endDate, Set<InterestRule> interestRules);
}
