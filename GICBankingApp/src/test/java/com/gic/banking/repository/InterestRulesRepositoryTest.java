package com.gic.banking.repository;

import com.gic.banking.model.InterestRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InterestRulesRepositoryTest {

    private InterestRulesRepository repository;
    private InterestRule rule1;
    private InterestRule rule2;

    @BeforeEach
    void setUp() {
        repository = new InterestRulesRepository();
        rule1 = new InterestRule(LocalDate.of(2023, 1, 1), "RULE01", 1.5);
        rule2 = new InterestRule(LocalDate.of(2023, 2, 1), "RULE02", 2.0);
    }

    @Test
    void addInterestRule_shouldReturnTrueWhenAddingNewRule() {
        // Act
        boolean result = repository.addInterestRule(rule1);

        // Assert
        assertTrue(result);
    }

    @Test
    void addInterestRule_shouldReturnFalseWhenAddingDuplicateRule() {
        // Arrange
        repository.addInterestRule(rule1);

        // Act
        boolean result = repository.addInterestRule(rule1);

        // Assert
        assertFalse(result);
    }

    @Test
    void removeInterestRule_shouldReturnTrueWhenRuleExists() {
        // Arrange
        repository.addInterestRule(rule1);

        // Act
        boolean result = repository.removeInterestRule(rule1);

        // Assert
        assertTrue(result);
    }

    @Test
    void removeInterestRule_shouldReturnFalseWhenRuleDoesNotExist() {
        // Act
        boolean result = repository.removeInterestRule(rule1);

        // Assert
        assertFalse(result);
    }

    @Test
    void getInterestRules_shouldReturnEmptySetWhenNoRulesAdded() {
        // Act
        Set<InterestRule> result = repository.getInterestRules();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getInterestRules_shouldReturnAllAddedRules() {
        // Arrange
        repository.addInterestRule(rule1);
        repository.addInterestRule(rule2);

        // Act
        Set<InterestRule> result = repository.getInterestRules();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(rule1));
        assertTrue(result.contains(rule2));
    }

    @Test
    void getInterestRules_shouldReturnUnmodifiableSet() {
        // Arrange
        repository.addInterestRule(rule1);
        Set<InterestRule> result = repository.getInterestRules();

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(rule2));
    }

    @Test
    void multipleOperations_shouldMaintainCorrectState() {
        // Add two rules
        assertTrue(repository.addInterestRule(rule1));
        assertTrue(repository.addInterestRule(rule2));
        assertEquals(2, repository.getInterestRules().size());

        // Remove one rule
        assertTrue(repository.removeInterestRule(rule1));
        assertEquals(1, repository.getInterestRules().size());
        assertFalse(repository.getInterestRules().contains(rule1));
        assertTrue(repository.getInterestRules().contains(rule2));

        // Try to remove non-existent rule
        assertFalse(repository.removeInterestRule(rule1));
        assertEquals(1, repository.getInterestRules().size());
    }
}
