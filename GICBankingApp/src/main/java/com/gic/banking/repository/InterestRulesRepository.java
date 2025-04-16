package com.gic.banking.repository;

import com.gic.banking.model.InterestRule;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class InterestRulesRepository {
    //list of all InterestRules
    private final Set<InterestRule> interestRules = new LinkedHashSet<>();

    public boolean addInterestRule(InterestRule rule) {
        return interestRules.add(rule); // Add the new rule.

    }

    public boolean removeInterestRule(InterestRule rule) {
        return interestRules.remove(rule); // Add the new rule.

    }

    public Set<InterestRule> getInterestRules() {
        return Collections.unmodifiableSet(this.interestRules);

    }


}
