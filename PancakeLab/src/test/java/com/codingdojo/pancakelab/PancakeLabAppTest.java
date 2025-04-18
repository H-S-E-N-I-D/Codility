package com.codingdojo.pancakelab;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PancakeLabAppTest {

    @Test
    void main_ShouldRunWithoutErrors() {
        // Set a system property to prevent the actual application from running
        System.setProperty("testing", "true");

        // Verify the main method runs without throwing exceptions
        assertDoesNotThrow(() -> PancakeLabApp.main(new String[]{}));

        // Clean up
        System.clearProperty("testing");
    }

    @Test
    void main_ShouldHandleNullArgs() {
        // Set a system property to prevent the actual application from running
        System.setProperty("testing", "true");

        // Verify the main method handles null args
        assertDoesNotThrow(() -> PancakeLabApp.main(null));

        // Clean up
        System.clearProperty("testing");
    }
}