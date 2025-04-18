package com.codingdojo.pancakelab.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PancakeTest {

    @Test
    void testConstructorAndGetters() {
        List<String> ingredients = Arrays.asList("flour", "eggs", "milk");
        Pancake pancake = new Pancake("Classic", ingredients);

        assertEquals("Classic", pancake.getName());
        assertEquals(ingredients, pancake.getIngredients());
    }

    @Test
    void testGetIngredientsReturnsUnmodifiableList() {
        List<String> ingredients = Arrays.asList("flour", "sugar", "butter");
        Pancake pancake = new Pancake("Sweet", ingredients);
        List<String> retrievedIngredients = pancake.getIngredients();

        assertThrows(UnsupportedOperationException.class, () -> {
            retrievedIngredients.add("syrup");
        });
    }

    @Test
    void testToStringWithIngredients() {
        List<String> ingredients = Arrays.asList("flour", "blueberries");
        Pancake pancake = new Pancake("Blueberry", ingredients);

        String expected = "Pancake{name='Blueberry', ingredients=[flour, blueberries]}";
        assertEquals(expected, pancake.toString());
    }

    @Test
    void testToStringWithEmptyIngredients() {
        Pancake pancake = new Pancake("Plain", Collections.emptyList());

        String expected = "Pancake{name='Plain', ingredients=[]}";
        assertEquals(expected, pancake.toString());
    }


    @Test
    void testNotEqualsDifferentName() {
        List<String> ingredients = Arrays.asList("flour", "eggs");
        Pancake pancake1 = new Pancake("Basic", ingredients);
        Pancake pancake2 = new Pancake("Deluxe", ingredients);

        assertNotEquals(pancake1, pancake2);
    }

    @Test
    void testNotEqualsDifferentIngredients() {
        Pancake pancake1 = new Pancake("Basic", Arrays.asList("flour", "eggs"));
        Pancake pancake2 = new Pancake("Basic", Arrays.asList("flour", "milk"));

        assertNotEquals(pancake1, pancake2);
    }

    @Test
    void testNotEqualsNull() {
        Pancake pancake = new Pancake("Basic", Arrays.asList("flour"));
        assertNotEquals(null, pancake);
    }

    @Test
    void testNotEqualsDifferentClass() {
        Pancake pancake = new Pancake("Basic", Arrays.asList("flour"));
        assertNotEquals("Basic pancake", pancake);
    }
}