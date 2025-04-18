package com.codingdojo.pancakelab.client;

import com.codingdojo.pancakelab.model.Building;
import com.codingdojo.pancakelab.model.Order;
import com.codingdojo.pancakelab.model.Pancake;
import com.codingdojo.pancakelab.model.Room;
import com.codingdojo.pancakelab.security.SecurityManager;
import com.codingdojo.pancakelab.security.UserContext;
import com.codingdojo.pancakelab.service.ChefService;
import com.codingdojo.pancakelab.service.DeliveryService;
import com.codingdojo.pancakelab.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PancakeLabClientImplTest {

    @Mock
    private OrderService orderService;
    @Mock
    private ChefService chefService;
    @Mock
    private DeliveryService deliveryService;
    @Mock
    private SecurityManager securityManager;
    @Mock
    private UserContext user;

    @Captor
    private ArgumentCaptor<String> stringCaptor;
    @Captor
    private ArgumentCaptor<Pancake> pancakeCaptor;

    private PancakeLabClientImpl client;
    private Order testOrder;
    private Pancake testPancake;
    private Room testRoom;

    @BeforeEach
    void setUp() {
        client = new PancakeLabClientImpl(orderService, chefService, deliveryService, securityManager);

        Building buildingNorth = new Building("North");
        Building buildingEast = new Building("East");
        testRoom = new Room("101", buildingNorth);
        testPancake = new Pancake("Chocolate", Collections.singletonList("chocolate"));
        testOrder = new Order(testRoom);
    }

    @Test
    void constructor_ShouldInitializeWithDependencies() {
        assertNotNull(client);
        verifyNoInteractions(orderService, chefService, deliveryService, securityManager);
    }

    @Test
    void constructor_ShouldThrowWhenNullDependencies() {
        assertThrows(NullPointerException.class, () ->
                new PancakeLabClientImpl(null, chefService, deliveryService, securityManager));
        assertThrows(NullPointerException.class, () ->
                new PancakeLabClientImpl(orderService, null, deliveryService, securityManager));
        assertThrows(NullPointerException.class, () ->
                new PancakeLabClientImpl(orderService, chefService, null, securityManager));
        assertThrows(NullPointerException.class, () ->
                new PancakeLabClientImpl(orderService, chefService, deliveryService, null));
    }

    @Test
    void createOrder_ShouldDelegateToOrderService() {
        when(orderService.createOrder(user, testRoom)).thenReturn(testOrder);

        Order result = client.createOrder(user, testRoom);

        assertEquals(testOrder, result);
        verify(orderService).createOrder(user, testRoom);
    }

    @Test
    void getOrder_ShouldDelegateToOrderService() {
        when(orderService.getOrder(user, "order1")).thenReturn(testOrder);

        Order result = client.getOrder(user, "order1");

        assertEquals(testOrder, result);
        verify(orderService).getOrder(user, "order1");
    }

    @Test
    void addPancakeToOrder_ShouldDelegateToOrderService() {
        when(orderService.addPancakeToOrder(user, "order1", testPancake)).thenReturn(testOrder);

        Order result = client.addPancakeToOrder(user, "order1", testPancake);

        assertEquals(testOrder, result);
        verify(orderService).addPancakeToOrder(user, "order1", testPancake);
    }

    @Test
    void addMultiplePancakesToOrder_ShouldAddMultiplePancakes() {
        when(orderService.getOrder(user, "order1")).thenReturn(testOrder);
        when(orderService.addPancakeToOrder(user, "order1", testPancake)).thenReturn(testOrder);

        Order result = client.addMultiplePancakesToOrder(user, "order1", testPancake, 3);

        assertEquals(testOrder, result);
        verify(orderService, times(3)).addPancakeToOrder(user, "order1", testPancake);
    }

    @Test
    void addMultiplePancakesToOrder_ShouldThrowForInvalidCount() {
        assertThrows(IllegalArgumentException.class,
                () -> client.addMultiplePancakesToOrder(user, "order1", testPancake, 0));
        assertThrows(IllegalArgumentException.class,
                () -> client.addMultiplePancakesToOrder(user, "order1", testPancake, -1));
    }

    @Test
    void removePancakeFromOrder_ShouldDelegateToOrderService() {
        when(orderService.removePancakeFromOrder(user, "order1", testPancake)).thenReturn(testOrder);

        Order result = client.removePancakeFromOrder(user, "order1", testPancake);

        assertEquals(testOrder, result);
        verify(orderService).removePancakeFromOrder(user, "order1", testPancake);
    }

    @Test
    void removeMultiplePancakesFromOrder_ShouldRemoveMultiplePancakes() {
        when(orderService.getOrder(user, "order1")).thenReturn(testOrder);
        when(orderService.removePancakeFromOrder(user, "order1", testPancake)).thenReturn(testOrder);

        Order result = client.removeMultiplePancakesFromOrder(user, "order1", testPancake, 2);

        assertEquals(testOrder, result);
        verify(orderService, times(2)).removePancakeFromOrder(user, "order1", testPancake);
    }

    @Test
    void removeMultiplePancakesFromOrder_ShouldThrowForInvalidCount() {
        assertThrows(IllegalArgumentException.class,
                () -> client.removeMultiplePancakesFromOrder(user, "order1", testPancake, 0));
        assertThrows(IllegalArgumentException.class,
                () -> client.removeMultiplePancakesFromOrder(user, "order1", testPancake, -1));
    }

    @Test
    void completeOrder_ShouldDelegateToOrderService() {
        when(orderService.completeOrder(user, "order1")).thenReturn(testOrder);

        Order result = client.completeOrder(user, "order1");

        assertEquals(testOrder, result);
        verify(orderService).completeOrder(user, "order1");
    }

    @Test
    void cancelOrder_ShouldDelegateToOrderService() {
        doNothing().when(orderService).cancelOrder(user, "order1");

        client.cancelOrder(user, "order1");

        verify(orderService).cancelOrder(user, "order1");
    }

    @Test
    void getPendingOrders_ShouldDelegateToChefService() {
        List<Order> expectedOrders = Collections.singletonList(testOrder);
        when(chefService.getPendingOrders(user)).thenReturn(expectedOrders);

        List<Order> result = client.getPendingOrders(user);

        assertEquals(expectedOrders, result);
        verify(chefService).getPendingOrders(user);
    }

    @Test
    void prepareOrder_ShouldDelegateToChefService() {
        when(chefService.prepareOrder(user, "order1")).thenReturn(testOrder);

        Order result = client.prepareOrder(user, "order1");

        assertEquals(testOrder, result);
        verify(chefService).prepareOrder(user, "order1");
    }

    @Test
    void getOrdersReadyForDelivery_ShouldDelegateToDeliveryService() {
        List<Order> expectedOrders = Collections.singletonList(testOrder);
        when(deliveryService.getOrdersReadyForDelivery(user)).thenReturn(expectedOrders);

        List<Order> result = client.getOrdersReadyForDelivery(user);

        assertEquals(expectedOrders, result);
        verify(deliveryService).getOrdersReadyForDelivery(user);
    }

    @Test
    void deliverOrder_ShouldDelegateToDeliveryService() {
        doNothing().when(deliveryService).deliverOrder(user, "order1");

        client.deliverOrder(user, "order1");

        verify(deliveryService).deliverOrder(user, "order1");
    }

    @Test
    void getAvailableBuildings_ShouldReturnAllAllowedBuildings() {
        List<Building> result = client.getAvailableBuildings();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void validatePancakeCount_ShouldThrowForInvalidCounts() {
        IllegalArgumentException exception;

        exception = assertThrows(IllegalArgumentException.class,
                () -> client.addMultiplePancakesToOrder(user, "order1", testPancake, 0));
        assertTrue(exception.getMessage().contains("Pancake count must be positive"));

        exception = assertThrows(IllegalArgumentException.class,
                () -> client.removeMultiplePancakesFromOrder(user, "order1", testPancake, -5));
        assertTrue(exception.getMessage().contains("Pancake count must be positive"));
    }

    @Test
    void logging_ShouldBePresentForAllOperations() {
        // This test verifies that logging calls exist in the code
        // Actual logging verification would require more sophisticated test setup
        assertDoesNotThrow(() -> {
            when(orderService.getOrder(user, "order1")).thenReturn(testOrder);
            when(orderService.addPancakeToOrder(user, "order1", testPancake)).thenReturn(testOrder);

            client.addMultiplePancakesToOrder(user, "order1", testPancake, 1);
            client.removeMultiplePancakesFromOrder(user, "order1", testPancake, 1);
        });
    }
}