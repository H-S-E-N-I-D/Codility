package com.codingdojo.pancakelab.repository;

import com.codingdojo.pancakelab.model.Building;
import com.codingdojo.pancakelab.model.Order;
import com.codingdojo.pancakelab.model.Order.OrderStatus;
import com.codingdojo.pancakelab.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryImplTest {
    private OrderRepositoryImpl repository;
    private Order testOrder1;
    private Order testOrder2;
    private Order testOrder3;

    @BeforeEach
    void setUp() {
        Building buildingNorth = new Building("North");
        Building buildingEast = new Building("East");
        Building buildingWest = new Building("West");
        Room room101 = new Room("101", buildingNorth);
        Room room102 = new Room("102", buildingEast);
        Room room103 = new Room("103", buildingWest);
        repository = new OrderRepositoryImpl();
        testOrder1 = new Order(room101);
        testOrder1.setStatus(OrderStatus.CREATED);

        testOrder2 = new Order(room102);
        testOrder2.setStatus(OrderStatus.COMPLETED);

        testOrder3 = new Order(room103);
        testOrder3.setStatus(OrderStatus.CREATED);
    }

    @Test
    void save_ShouldStoreOrderAndReturnIt() {
        Order savedOrder = repository.save(testOrder1);

        assertNotNull(savedOrder);
        assertEquals(testOrder1, savedOrder);
        assertEquals(testOrder1, repository.findById(testOrder1.getId()).get());
    }

    @Test
    void save_ShouldUpdateExistingOrder() {
        repository.save(testOrder1);
        testOrder1.setStatus(OrderStatus.COMPLETED);

        Order updatedOrder = repository.save(testOrder1);

        assertEquals(OrderStatus.COMPLETED, updatedOrder.getStatus());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void findById_ShouldReturnEmptyForNonExistentOrder() {
        Optional<Order> result = repository.findById("nonexistent");

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_ShouldReturnExistingOrder() {
        repository.save(testOrder1);
        Optional<Order> result = repository.findById(testOrder1.getId());

        assertTrue(result.isPresent());
        assertEquals(testOrder1, result.get());
    }

    @Test
    void findAll_ShouldReturnEmptyListForEmptyRepository() {
        List<Order> result = repository.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllOrders() {
        repository.save(testOrder1);
        repository.save(testOrder2);
        List<Order> result = repository.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(testOrder1));
        assertTrue(result.contains(testOrder2));
    }

    @Test
    void delete_ShouldRemoveOrder() {
        repository.save(testOrder1);
        repository.delete(testOrder1);

        assertTrue(repository.findById(testOrder1.getId()).isEmpty());
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void delete_ShouldDoNothingForNonExistentOrder() {
        repository.save(testOrder1);
        repository.delete(testOrder2);

        assertEquals(1, repository.findAll().size());
        assertTrue(repository.findById(testOrder1.getId()).isPresent());
    }

    @Test
    void findByStatus_ShouldReturnEmptyListForNoMatches() {
        repository.save(testOrder1); // CREATED status
        List<Order> result = repository.findByStatus(OrderStatus.COMPLETED);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByStatus_ShouldReturnMatchingOrders() {
        repository.save(testOrder1); // CREATED
        repository.save(testOrder2); // COMPLETED
        repository.save(testOrder3); // CREATED

        List<Order> createdOrders = repository.findByStatus(OrderStatus.CREATED);
        List<Order> completedOrders = repository.findByStatus(OrderStatus.COMPLETED);

        assertEquals(2, createdOrders.size());
        assertTrue(createdOrders.contains(testOrder1));
        assertTrue(createdOrders.contains(testOrder3));

        assertEquals(1, completedOrders.size());
        assertTrue(completedOrders.contains(testOrder2));
    }

    @Test
    void findByStatus_ShouldHandleNullOrders() {
        // Simulate a null value somehow getting into the map
        repository.save(testOrder1);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            repository.save(null);
        });
        assertEquals("Order cannot be null", exception.getMessage());

        List<Order> result = repository.findByStatus(OrderStatus.CREATED);

        assertEquals(1, result.size());
        assertEquals(testOrder1, result.get(0));
    }

    @Test
    void concurrentAccess_ShouldMaintainConsistency() throws InterruptedException {
        // Test thread safety
        repository.save(testOrder1);

        Thread t1 = new Thread(() -> {
            repository.save(testOrder2);
            repository.delete(testOrder1);
        });

        Thread t2 = new Thread(() -> {
            Optional<Order> order = repository.findById(testOrder1.getId());
            if (order.isPresent()) {
                repository.save(testOrder3);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // Verify final state is consistent
        List<Order> allOrders = repository.findAll();
        assertTrue(allOrders.size() >= 1 && allOrders.size() <= 3);
    }
}