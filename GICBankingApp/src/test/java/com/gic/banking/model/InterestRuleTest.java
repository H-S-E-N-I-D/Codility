package com.gic.banking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InterestRuleTest {

    @Test
    void testInterestRuleConstructorAndGetters() {
        LocalDate date = LocalDate.now();
        InterestRule rule = new InterestRule(date, "R1", 5.0);

        assertEquals(date, rule.getDate());
        assertEquals("R1", rule.getRuleId());
        assertEquals(5.0, rule.getRate());
    }

    @Test
    void testSetters() {
        LocalDate date = LocalDate.now();
        InterestRule rule = new InterestRule(date, "R1", 5.0);

        rule.setDate(date.plusDays(1));
        rule.setRuleId("R2");
        rule.setRate(10.0);

        assertEquals(date.plusDays(1), rule.getDate());
        assertEquals("R2", rule.getRuleId());
        assertEquals(10.0, rule.getRate());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDate date = LocalDate.now();
        InterestRule rule1 = new InterestRule(date, "R1", 5.0);
        InterestRule rule2 = new InterestRule(date, "R1", 5.0);
        InterestRule rule3 = new InterestRule(date.plusDays(1), "R2", 10.0);

        assertEquals(rule1, rule2);
        assertNotEquals(rule1, rule3);
        assertEquals(rule1.hashCode(), rule2.hashCode());
        assertNotEquals(rule1.hashCode(), rule3.hashCode());
    }
}