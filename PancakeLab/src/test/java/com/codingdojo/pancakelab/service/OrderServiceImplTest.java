package com.codingdojo.pancakelab.service;

import com.codingdojo.pancakelab.model.Building;
import com.codingdojo.pancakelab.model.Order;
import com.codingdojo.pancakelab.model.Pancake;
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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private SecurityManager securityManager;
    @Mock
    private UserContext discipleUser;
    @Mock
    private UserContext nonDiscipleUser;
    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    private OrderServiceImpl orderService;
    private Order testOrder;
    private Pancake testPancake;
    private Room testRoom;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(securityManager, orderRepository);

        Building buildingNorth = new Building("North");
        testRoom = new Room("101", buildingNorth);
        testPancake = new Pancake("DarkChocolate", Collections.singletonList("dark-chocolate"));
        testOrder = new Order(testRoom);
    }

    @Test
    void constructor_ShouldInitializeWithDependencies() {
        assertNotNull(orderService);
        verifyNoInteractions(securityManager, orderRepository);
    }

    @Test
    void constructor_ShouldThrowWhenNullDependencies() {
        assertThrows(NullPointerException.class, () -> new OrderServiceImpl(null, orderRepository));
        assertThrows(NullPointerException.class, () -> new OrderServiceImpl(securityManager, null));
    }

    @Test
    void createOrder_ShouldCreateNewOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.createOrder(discipleUser, testRoom);

        assertNotNull(result);
        verify(securityManager).checkPermission(discipleUser, UserRole.DISCIPLE);
        verify(orderRepository).save(orderCaptor.capture());
        assertEquals(testRoom, orderCaptor.getValue().getRoom());
    }

    @Test
    void createOrder_ShouldThrowWhenNotDisciple() {
        doThrow(new SecurityException("Unauthorized"))
                .when(securityManager).checkPermission(nonDiscipleUser, UserRole.DISCIPLE);

        assertThrows(SecurityException.class,
                () -> orderService.createOrder(nonDiscipleUser, testRoom));
    }

    @Test
    void addPancakeToOrder_ShouldAddPancake() {
        when(orderRepository.findById("order1")).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.addPancakeToOrder(discipleUser, "order1", testPancake);

        assertNotNull(result);
        verify(securityManager).checkPermission(discipleUser, UserRole.DISCIPLE);
        verify(orderRepository).save(testOrder);
        assertTrue(testOrder.getPancakes().contains(testPancake));
    }

    @Test
    void addPancakeToOrder_ShouldThrowWhenInvalidStatus() {
        testOrder.completeOrder();
        when(orderRepository.findById("order1")).thenReturn(Optional.of(testOrder));

        assertThrows(IllegalStateException.class,
                () -> orderService.addPancakeToOrder(discipleUser, "order1", testPancake));
    }

    @Test
    void removePancakeFromOrder_ShouldRemovePancake() {
        testOrder.addPancake(testPancake);
        when(orderRepository.findById("order1")).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.removePancakeFromOrder(discipleUser, "order1", testPancake);

        assertNotNull(result);
        verify(securityManager).checkPermission(discipleUser, UserRole.DISCIPLE);
        verify(orderRepository).save(testOrder);
        assertFalse(testOrder.getPancakes().contains(testPancake));
    }

    @Test
    void completeOrder_ShouldCompleteOrder() {
        testOrder.addPancake(testPancake);
        when(orderRepository.findById("order1")).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.completeOrder(discipleUser, "order1");

        assertNotNull(result);
        assertEquals(Order.OrderStatus.COMPLETED, result.getStatus());
    }

    @Test
    void completeOrder_ShouldThrowWhenNoPancakes() {
        when(orderRepository.findById("order1")).thenReturn(Optional.of(testOrder));

        assertThrows(IllegalStateException.class,
                () -> orderService.completeOrder(discipleUser, "order1"));
    }

    @Test
    void cancelOrder_ShouldCancelOrder() {
        when(orderRepository.findById("order1")).thenReturn(Optional.of(testOrder));

        orderService.cancelOrder(discipleUser, "order1");

        verify(orderRepository).delete(testOrder);
        assertEquals(Order.OrderStatus.CANCELLED, testOrder.getStatus());
    }

    @Test
    void getOrder_ShouldReturnOrder() {
        when(orderRepository.findById("order1")).thenReturn(Optional.of(testOrder));

        Order result = orderService.getOrder(discipleUser, "order1");

        assertNotNull(result);
        assertEquals(testOrder, result);
    }

    @Test
    void getOrder_ShouldThrowWhenOrderNotFound() {
        when(orderRepository.findById("order1")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> orderService.getOrder(discipleUser, "order1"));
    }

    @Test
    void allMethods_ShouldBeSynchronized() {
        assertSynchronized("createOrder", UserContext.class, Room.class);
        assertSynchronized("addPancakeToOrder", UserContext.class, String.class, Pancake.class);
        assertSynchronized("removePancakeFromOrder", UserContext.class, String.class, Pancake.class);
        assertSynchronized("completeOrder", UserContext.class, String.class);
        assertSynchronized("cancelOrder", UserContext.class, String.class);
    }

    private void assertSynchronized(String methodName, Class<?>... parameterTypes) {
        try {
            assertTrue(java.lang.reflect.Modifier.isSynchronized(
                    OrderServiceImpl.class.getMethod(methodName, parameterTypes)
                            .getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("Method " + methodName + " not found");
        }
    }

}