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
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private SecurityManager securityManager;
    @Mock
    private UserContext deliveryUser;
    @Mock
    private UserContext nonDeliveryUser;
    @Captor
    private ArgumentCaptor<LogRecord> logCaptor;

    private DeliveryServiceImpl deliveryService;

    @BeforeEach
    void setUp() {
        deliveryService = new DeliveryServiceImpl(securityManager, orderRepository);

        // Setup logger capturing
        Logger logger = Logger.getLogger(DeliveryServiceImpl.class.getName());
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
        assertNotNull(deliveryService);
        verifyNoInteractions(securityManager); // testing construction
    }

    @Test
    void constructor_ShouldThrowWhenNullDependencies() {
        assertThrows(NullPointerException.class, () -> new DeliveryServiceImpl(null, orderRepository));
        assertThrows(NullPointerException.class, () -> new DeliveryServiceImpl(securityManager, null));
    }

    @Test
    void getOrdersReadyForDelivery_ShouldReturnReadyOrders() {
        // Arrange

        Building buildingNorth = new Building("North");
        Building buildingEast = new Building("East");
        Room room101 = new Room("101", buildingNorth);
        Room room102 = new Room("102", buildingEast);

        Order readyOrder1 = new Order(room101);
        readyOrder1.setStatus(Order.OrderStatus.READY_FOR_DELIVERY);
        Order readyOrder2 = new Order(room102);
        readyOrder2.setStatus(Order.OrderStatus.READY_FOR_DELIVERY);
        List<Order> expectedOrders = Arrays.asList(readyOrder1, readyOrder2);

        when(orderRepository.findByStatus(Order.OrderStatus.READY_FOR_DELIVERY)).thenReturn(expectedOrders);

        // Act
        List<Order> result = deliveryService.getOrdersReadyForDelivery(deliveryUser);

        // Assert
        assertEquals(2, result.size());
        verify(securityManager).checkPermission(deliveryUser, UserRole.DELIVERY);
        verify(orderRepository).findByStatus(Order.OrderStatus.READY_FOR_DELIVERY);
    }

    @Test
    void getOrdersReadyForDelivery_ShouldThrowWhenNotDeliveryUser() {
        doThrow(new SecurityException("Unauthorized"))
                .when(securityManager).checkPermission(nonDeliveryUser, UserRole.DELIVERY);

        assertThrows(SecurityException.class,
                () -> deliveryService.getOrdersReadyForDelivery(nonDeliveryUser));
    }

    @Test
    void deliverOrder_ShouldMarkOrderAsDelivered() {
        // Arrange
        Building buildingNorth = new Building("North");
        Room room101 = new Room("101", buildingNorth);

        String orderId = "order1";
        Order order = new Order(room101);
        order.setStatus(Order.OrderStatus.READY_FOR_DELIVERY);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

        // Act
        deliveryService.deliverOrder(deliveryUser, orderId);

        // Assert
        assertEquals(Order.OrderStatus.DELIVERED, order.getStatus());
        verify(securityManager).checkPermission(deliveryUser, UserRole.DELIVERY);
        verify(orderRepository).delete(order);
    }

    @Test
    void deliverOrder_ShouldThrowWhenOrderNotFound() {
        // Arrange
        String orderId = "nonexistent";
        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> deliveryService.deliverOrder(deliveryUser, orderId));

        assertTrue(exception.getMessage().contains("Order not found"));
        verify(securityManager).checkPermission(deliveryUser, UserRole.DELIVERY);
    }

    @Test
    void deliverOrder_ShouldThrowWhenInvalidStatus() {
        // Arrange
        Building buildingNorth = new Building("North");
        Room room101 = new Room("101", buildingNorth);

        String orderId = "order1";
        Order order = new Order(room101);
        order.setStatus(Order.OrderStatus.CREATED);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> deliveryService.deliverOrder(deliveryUser, orderId));

        assertTrue(exception.getMessage().contains("Invalid order status"));
        verify(securityManager).checkPermission(deliveryUser, UserRole.DELIVERY);
    }

    @Test
    void deliverOrder_ShouldThrowWhenNullUser() {
        assertThrows(NullPointerException.class,
                () -> deliveryService.deliverOrder(null, "order1"));
    }

    @Test
    void deliverOrder_ShouldThrowWhenNullOrderId() {
        assertThrows(NullPointerException.class,
                () -> deliveryService.deliverOrder(deliveryUser, null));
    }

    @Test
    void deliverOrder_ShouldValidateRoom() {
        // Arrange
        Building buildingNorth = new Building("North");
        Room invalidRoom = new Room(null, buildingNorth);// Invalid room

        String orderId = "order1";
        Order order = new Order(invalidRoom);
        order.setStatus(Order.OrderStatus.READY_FOR_DELIVERY);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> deliveryService.deliverOrder(deliveryUser, orderId));
    }

    @Test
    void deliverOrder_ShouldBeSynchronized() {
        // Verify method is synchronized
        try {
            assertTrue(java.lang.reflect.Modifier.isSynchronized(
                    DeliveryServiceImpl.class.getMethod("deliverOrder", UserContext.class, String.class)
                            .getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("Method not found");
        }
    }
}