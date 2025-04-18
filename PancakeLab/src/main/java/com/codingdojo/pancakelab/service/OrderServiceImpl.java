package com.codingdojo.pancakelab.service;

import com.codingdojo.pancakelab.model.Order;
import com.codingdojo.pancakelab.model.Pancake;
import com.codingdojo.pancakelab.model.Room;
import com.codingdojo.pancakelab.repository.OrderRepository;
import com.codingdojo.pancakelab.security.SecurityManager;
import com.codingdojo.pancakelab.security.UserContext;
import com.codingdojo.pancakelab.security.UserRole;
import com.codingdojo.pancakelab.util.Validator;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = Logger.getLogger(OrderServiceImpl.class.getName());

    private final OrderRepository orderRepository;
    private final SecurityManager securityManager;

    public OrderServiceImpl(SecurityManager securityManager, OrderRepository orderRepository) {
        this.securityManager = Objects.requireNonNull(securityManager, "SecurityManager cannot be null");
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository cannot be null");
        LOGGER.log(Level.INFO, "OrderService initialized with SecurityManager and OrderRepository");
    }

    @Override
    public synchronized Order createOrder(UserContext user, Room room) {
        LOGGER.log(Level.INFO, "Creating new order for user: {0} in room: {1}",
                new Object[]{user.getUsername(), room.getNumber()});

        validateUserPermission(user, UserRole.DISCIPLE);
        Validator.validateRoom(room);

        Order order = new Order(room);
        Order createdOrder = orderRepository.save(order);

        LOGGER.log(Level.INFO, "Successfully created order {0} for user: {1}",
                new Object[]{createdOrder.getId(), user.getUsername()});

        return createdOrder;
    }

    @Override
    public synchronized Order addPancakeToOrder(UserContext user, String orderId, Pancake pancake) {
        LOGGER.log(Level.INFO, "Adding pancake to order {0} by user: {1}",
                new Object[]{orderId, user.getUsername()});

        validateUserPermission(user, UserRole.DISCIPLE);
        Validator.validatePancake(pancake);

        Order order = getValidOrder(orderId);
        validateOrderStatus(order, Order.OrderStatus.CREATED, "add pancake");

        order.addPancake(pancake);
        Order updatedOrder = orderRepository.save(order);

        LOGGER.log(Level.INFO, "Successfully added pancake to order {0} by user: {1}",
                new Object[]{orderId, user.getUsername()});

        return updatedOrder;
    }

    @Override
    public synchronized Order removePancakeFromOrder(UserContext user, String orderId, Pancake pancake) {
        LOGGER.log(Level.INFO, "Removing pancake from order {0} by user: {1}",
                new Object[]{orderId, user.getUsername()});

        validateUserPermission(user, UserRole.DISCIPLE);

        Order order = getValidOrder(orderId);
        validateOrderStatus(order, Order.OrderStatus.CREATED, "remove pancake");

        order.removePancake(pancake);
        Order updatedOrder = orderRepository.save(order);

        LOGGER.log(Level.INFO, "Successfully removed pancake from order {0} by user: {1}",
                new Object[]{orderId, user.getUsername()});

        return updatedOrder;
    }

    @Override
    public synchronized Order completeOrder(UserContext user, String orderId) {
        LOGGER.log(Level.INFO, "Completing order {0} by user: {1}",
                new Object[]{orderId, user.getUsername()});

        validateUserPermission(user, UserRole.DISCIPLE);

        Order order = getValidOrder(orderId);
        validateOrderStatus(order, Order.OrderStatus.CREATED, "complete");
        validateOrderHasPancakes(order);

        order.completeOrder();
        Order completedOrder = orderRepository.save(order);

        LOGGER.log(Level.INFO, "Successfully completed order {0} by user: {1}",
                new Object[]{orderId, user.getUsername()});

        return completedOrder;
    }

    @Override
    public synchronized void cancelOrder(UserContext user, String orderId) {
        LOGGER.log(Level.INFO, "Cancelling order {0} by user: {1}",
                new Object[]{orderId, user.getUsername()});

        validateUserPermission(user, UserRole.DISCIPLE);

        Order order = getValidOrder(orderId);
        validateOrderStatus(order, Order.OrderStatus.CREATED, "cancel");

        order.cancelOrder();
        orderRepository.delete(order);

        LOGGER.log(Level.INFO, "Successfully cancelled order {0} by user: {1}",
                new Object[]{orderId, user.getUsername()});
    }

    @Override
    public Order getOrder(UserContext user, String orderId) {
        LOGGER.log(Level.INFO, "Retrieving order {0} by user: {1}",
                new Object[]{orderId, user.getUsername()});

        Order order = getValidOrder(orderId);
        LOGGER.log(Level.INFO, "Successfully retrieved order {0} by user: {1}",
                new Object[]{orderId, user.getUsername()});

        return order;
    }

    private Order getValidOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    String errorMsg = String.format("Order not found with id: %s", orderId);
                    LOGGER.log(Level.SEVERE, errorMsg);
                    return new IllegalArgumentException(errorMsg);
                });
    }

    private void validateUserPermission(UserContext user, UserRole requiredRole) {
        securityManager.checkPermission(user, requiredRole);
    }

    private void validateOrderStatus(Order order, Order.OrderStatus expectedStatus, String operation) {
        if (order.getStatus() != expectedStatus) {
            String errorMsg = String.format("Cannot %s for order in status: %s", operation, order.getStatus());
            LOGGER.log(Level.WARNING, errorMsg);
            throw new IllegalStateException(errorMsg);
        }
    }

    private void validateOrderHasPancakes(Order order) {
        if (order.getPancakes().isEmpty()) {
            String errorMsg = "Cannot complete order with no pancakes";
            LOGGER.log(Level.WARNING, errorMsg);
            throw new IllegalStateException(errorMsg);
        }
    }
}