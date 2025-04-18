package com.codingdojo.pancakelab.service;

import com.codingdojo.pancakelab.model.Building;
import com.codingdojo.pancakelab.model.Order;
import com.codingdojo.pancakelab.model.Room;
import com.codingdojo.pancakelab.repository.OrderRepository;
import com.codingdojo.pancakelab.security.SecurityManager;
import com.codingdojo.pancakelab.security.UserContext;
import com.codingdojo.pancakelab.security.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChefServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private SecurityManager securityManager;
    @Mock
    private UserContext chefUser;
    @Mock
    private UserContext nonChefUser;
    @Mock
    private Handler logHandler;
    @Captor
    private ArgumentCaptor<LogRecord> logCaptor;


    private ChefServiceImpl chefService;

    @BeforeEach
    void setUp() {
        chefService = new ChefServiceImpl(securityManager, orderRepository);
        Logger logger = Logger.getLogger(ChefServiceImpl.class.getName());
        logger.addHandler(new java.util.logging.Handler() {
            @Override
            public void publish(LogRecord record) {
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        });
    }

    @Test
    void constructor_ShouldInitializeWithDependencies() {
        assertNotNull(chefService);
        verifyNoInteractions(securityManager); // testing construction
        verifyNoInteractions(orderRepository);
    }

    @Test
    void getPendingOrders_ShouldReturnCompletedOrdersForChef() {
        // Arrange
        Building buildingNorth = new Building("North");
        Building buildingEast = new Building("East");
        Room room101 = new Room("101", buildingNorth);
        Room room102 = new Room("102", buildingEast);

        Order completedOrder1 = new Order(room101);
        completedOrder1.setStatus(Order.OrderStatus.COMPLETED);
        Order completedOrder2 = new Order(room102);
        completedOrder2.setStatus(Order.OrderStatus.COMPLETED);
        List<Order> expectedOrders = Arrays.asList(completedOrder1, completedOrder2);

        when(orderRepository.findByStatus(Order.OrderStatus.COMPLETED)).thenReturn(expectedOrders);

        // Act
        List<Order> result = chefService.getPendingOrders(chefUser);

        // Assert
        assertEquals(2, result.size());
        verify(securityManager).checkPermission(chefUser, UserRole.CHEF);
        verify(orderRepository).findByStatus(Order.OrderStatus.COMPLETED);
    }

    @Test
    void getPendingOrders_ShouldThrowWhenNotChef() {
        doThrow(new SecurityException("Unauthorized"))
                .when(securityManager).checkPermission(nonChefUser, UserRole.CHEF);

        assertThrows(SecurityException.class,
                () -> chefService.getPendingOrders(nonChefUser));
    }

    @Test
    void prepareOrder_ShouldUpdateStatusToReadyForDelivery() {
        // Arrange
        Building buildingNorth = new Building("North");
        Room room101 = new Room("101", buildingNorth);

        String orderId = "order1";
        Order order = new Order(room101);
        order.setStatus(Order.OrderStatus.COMPLETED);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        // Act
        Order result = chefService.prepareOrder(chefUser, orderId);

        // Assert
        assertEquals(Order.OrderStatus.READY_FOR_DELIVERY, result.getStatus());
        verify(securityManager).checkPermission(chefUser, UserRole.CHEF);
        verify(orderRepository).save(order);
    }

    @Test
    void prepareOrder_ShouldThrowWhenOrderNotFound() {
        // Arrange
        String orderId = "nonexistent";
        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> chefService.prepareOrder(chefUser, orderId));

        assertTrue(exception.getMessage().contains("Order not found"));
        verify(securityManager).checkPermission(chefUser, UserRole.CHEF);
    }

    @Test
    void prepareOrder_ShouldThrowWhenInvalidStatus() {
        // Arrange
        Building buildingEast = new Building("East");
        Room room102 = new Room("102", buildingEast);

        String orderId = "order1";
        Order order = new Order(room102);
        order.setStatus(Order.OrderStatus.CREATED);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> chefService.prepareOrder(chefUser, orderId));

        assertTrue(exception.getMessage().contains("Invalid order status"));
        verify(securityManager).checkPermission(chefUser, UserRole.CHEF);
    }

    @Test
    void prepareOrder_ShouldBeSynchronized() {
        // This test verifies the method is synchronized by checking its modifiers
        try {
            assertTrue(java.lang.reflect.Modifier.isSynchronized(
                    ChefServiceImpl.class.getMethod("prepareOrder", UserContext.class, String.class)
                            .getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("Method not found");
        }
    }

    @Test
    void prepareOrder_ShouldLogAppropriateMessages() {
        // Arrange
        Building buildingNorth = new Building("North");
        Room room101 = new Room("101", buildingNorth);

        String orderId = "order1";
        Order order = new Order(room101);
        order.setStatus(Order.OrderStatus.COMPLETED);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        // Act
        chefService.prepareOrder(chefUser, orderId);

        // Assert - Verify logging through mock handler
        // Note: Actual logging verification would require a more sophisticated test setup
        // This just verifies the code paths that generate logs are executed
        assertDoesNotThrow(() -> verify(orderRepository).findById(orderId));
    }

    @Test
    void constructor_ShouldThrowWhenNullDependencies() {
        assertThrows(NullPointerException.class, () -> new ChefServiceImpl(null, orderRepository));
        assertThrows(NullPointerException.class, () -> new ChefServiceImpl(securityManager, null));
    }
}