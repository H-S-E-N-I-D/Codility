package com.codingdojo.pancakelab.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PancakeLabClientFactoryTest {

    @Test
    void createClient_ShouldCreateNewPancakeLabClient() {
        // Act
        PancakeLabClient client = PancakeLabClientFactory.createClient();

        // Assert
        assertNotNull(client);
        assertTrue(client instanceof PancakeLabClientImpl);

    }

    @Test
    void createPancakeBuilderClient_ShouldCreateNewPancakeBuilderClient() {
        // Act
        PancakeBuilderClient builderClient = PancakeLabClientFactory.createPancakeBuilderClient();

        // Assert
        assertNotNull(builderClient);
        assertTrue(builderClient instanceof PancakeBuilderClientImpl);

    }

    @Test
    void factoryMethods_ShouldCreateNewInstancesEachTime() {
        // Act
        PancakeLabClient client1 = PancakeLabClientFactory.createClient();
        PancakeLabClient client2 = PancakeLabClientFactory.createClient();
        PancakeBuilderClient builder1 = PancakeLabClientFactory.createPancakeBuilderClient();
        PancakeBuilderClient builder2 = PancakeLabClientFactory.createPancakeBuilderClient();

        // Assert
        assertNotSame(client1, client2);
        assertNotSame(builder1, builder2);
    }

    @Test
    void constructor_ShouldBePrivate() {
        // Verify the factory class cannot be instantiated
        assertThrows(IllegalAccessException.class, () -> {
            PancakeLabClientFactory.class.getDeclaredConstructor().newInstance();
        });
    }
}