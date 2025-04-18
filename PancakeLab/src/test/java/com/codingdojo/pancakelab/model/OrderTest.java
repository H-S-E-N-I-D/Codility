package com.codingdojo.pancakelab.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    private Room testRoom;
    private Order order;
    private Pancake testPancake;

    @BeforeEach
    void setUp() {
        Building testBuilding = new Building("Test Building");
        testRoom = new Room("101", testBuilding);
        order = new Order(testRoom);
        testPancake = new Pancake("DarkChocolateWhippedCream", List.of("dark-chocolate", "whipped-cream"));

    }

    @Test
    void testConstructorInitialization() {
        assertNotNull(order.getId());
        assertEquals(testRoom, order.getRoom());
        assertTrue(order.getPancakes().isEmpty());
        assertEquals(Order.OrderStatus.CREATED, order.getStatus());
    }

    @Test
    void testGetId() {
        assertNotNull(order.getId());
        // UUID format validation
        assertDoesNotThrow(() -> UUID.fromString(order.getId()));
    }

    @Test
    void testGetRoom() {
        assertEquals(testRoom, order.getRoom());
    }

    @Test
    void testGetPancakesReturnsCopy() {
        order.addPancake(testPancake);
        List<Pancake> pancakes = order.getPancakes();
        pancakes.clear();
        assertFalse(order.getPancakes().isEmpty());
    }

    @Test
    void testGetStatus() {
        assertEquals(Order.OrderStatus.CREATED, order.getStatus());
    }

    @Test
    void testSetStatus() {
        order.setStatus(Order.OrderStatus.PREPARING);
        assertEquals(Order.OrderStatus.PREPARING, order.getStatus());
    }

    @Test
    void testAddPancake() {
        order.addPancake(testPancake);
        assertEquals(1, order.getPancakes().size());
        assertEquals(testPancake, order.getPancakes().get(0));
    }

    @Test
    void testRemovePancake() {
        order.addPancake(testPancake);
        order.removePancake(testPancake);
        assertTrue(order.getPancakes().isEmpty());
    }

    @Test
    void testRemoveNonExistentPancake() {
        Pancake anotherPancake = new Pancake("DarkChocolate", List.of("dark-chocolate"));
        order.addPancake(testPancake);
        order.removePancake(anotherPancake);
        assertEquals(1, order.getPancakes().size());
    }

    @Test
    void testCompleteOrder() {
        order.completeOrder();
        assertEquals(Order.OrderStatus.COMPLETED, order.getStatus());
    }

    @Test
    void testCancelOrder() {
        order.cancelOrder();
        assertEquals(Order.OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void testToString() {
        order.addPancake(testPancake);
        String result = order.toString();

        assertTrue(result.contains("Id = '" + order.getId() + "'"));
        assertTrue(result.contains("Room = '101'"));
        assertTrue(result.contains("Building = 'Test Building'"));
        assertTrue(result.contains("pancakes = [" + testPancake + "]"));
        assertTrue(result.contains("status = CREATED"));
    }

    @Test
    void testOrderStatusEnum() {
        // Verify all enum values are present
        Order.OrderStatus[] statuses = Order.OrderStatus.values();
        assertEquals(6, statuses.length);
        assertEquals(Order.OrderStatus.CREATED, Order.OrderStatus.valueOf("CREATED"));
        assertEquals(Order.OrderStatus.COMPLETED, Order.OrderStatus.valueOf("COMPLETED"));
        assertEquals(Order.OrderStatus.PREPARING, Order.OrderStatus.valueOf("PREPARING"));
        assertEquals(Order.OrderStatus.READY_FOR_DELIVERY, Order.OrderStatus.valueOf("READY_FOR_DELIVERY"));
        assertEquals(Order.OrderStatus.DELIVERED, Order.OrderStatus.valueOf("DELIVERED"));
        assertEquals(Order.OrderStatus.CANCELLED, Order.OrderStatus.valueOf("CANCELLED"));
    }
}