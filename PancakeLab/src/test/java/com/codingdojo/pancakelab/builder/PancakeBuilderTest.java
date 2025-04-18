package com.codingdojo.pancakelab.builder;

import com.codingdojo.pancakelab.model.Pancake;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PancakeBuilderTest {

    @Test
    void newInstance_ShouldReturnNewBuilderInstance() {
        PancakeBuilder builder = PancakeBuilder.newInstance();
        assertNotNull(builder);
    }

    @Test
    void withIngredient_ShouldAddIngredientAndReturnBuilder() {
        PancakeBuilder builder = PancakeBuilder.newInstance()
                .withName("Chocolate Chip")
                .withIngredient("chocolate chips");

        assertNotNull(builder);
        // Verify ingredient was added through build() validation
        assertDoesNotThrow(() -> builder.build());
    }

    @Test
    void build_ShouldCreatePancakeWithValidInput() {
        Pancake pancake = PancakeBuilder.newInstance()
                .withName("Banana Pancake")
                .withIngredient("banana")
                .withIngredient("walnuts")
                .build();

        assertNotNull(pancake);
        assertEquals("Banana Pancake", pancake.getName());
        assertEquals(2, pancake.getIngredients().size());
        assertTrue(pancake.getIngredients().contains("banana"));
        assertTrue(pancake.getIngredients().contains("walnuts"));
    }

    @Test
    void build_ShouldReturnUnmodifiableIngredientsList() {
        Pancake pancake = PancakeBuilder.newInstance()
                .withName("Test Pancake")
                .withIngredient("flour")
                .build();

        List<String> ingredients = pancake.getIngredients();
        assertThrows(UnsupportedOperationException.class,
                () -> ingredients.add("sugar"));
    }

    @Test
    void build_ShouldThrowExceptionWhenNameIsNull() {
        PancakeBuilder builder = PancakeBuilder.newInstance()
                .withIngredient("flour");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                builder::build);

        assertEquals("Pancake name cannot be empty", exception.getMessage());
    }

    @Test
    void build_ShouldThrowExceptionWhenNameIsEmpty() {
        PancakeBuilder builder = PancakeBuilder.newInstance()
                .withName("")
                .withIngredient("flour");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                builder::build);

        assertEquals("Pancake name cannot be empty", exception.getMessage());
    }

    @Test
    void build_ShouldThrowExceptionWhenNameIsBlank() {
        PancakeBuilder builder = PancakeBuilder.newInstance()
                .withName("   ")
                .withIngredient("flour");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                builder::build);

        assertEquals("Pancake name cannot be empty", exception.getMessage());
    }

    @Test
    void build_ShouldThrowExceptionWhenNoIngredients() {
        PancakeBuilder builder = PancakeBuilder.newInstance()
                .withName("Empty Pancake");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                builder::build);

        assertEquals("Pancake must have at least one ingredient", exception.getMessage());
    }

    @Test
    void builder_ShouldSupportMethodChaining() {
        Pancake pancake = PancakeBuilder.newInstance()
                .withName("Strawberry Pancake")
                .withIngredient("strawberries")
                .withIngredient("whipped cream")
                .build();

        assertNotNull(pancake);
        assertEquals(2, pancake.getIngredients().size());
    }

    @Test
    void builder_ShouldCreateIndependentInstances() {
        PancakeBuilder builder1 = PancakeBuilder.newInstance()
                .withName("Pancake 1")
                .withIngredient("ingredient 1");

        PancakeBuilder builder2 = PancakeBuilder.newInstance()
                .withName("Pancake 2")
                .withIngredient("ingredient 2");

        Pancake pancake1 = builder1.build();
        Pancake pancake2 = builder2.build();

        assertNotEquals(pancake1.getName(), pancake2.getName());
        assertNotEquals(pancake1.getIngredients(), pancake2.getIngredients());
    }
}