package com.codingdojo.pancakelab.service;

import com.codingdojo.pancakelab.model.Order;
import com.codingdojo.pancakelab.repository.OrderRepository;
import com.codingdojo.pancakelab.security.SecurityManager;
import com.codingdojo.pancakelab.security.UserContext;
import com.codingdojo.pancakelab.security.UserRole;
import com.codingdojo.pancakelab.util.Validator;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeliveryServiceImpl implements DeliveryService {
    private static final Logger LOGGER = Logger.getLogger(DeliveryServiceImpl.class.getName());

    private final OrderRepository orderRepository;
    private final SecurityManager securityManager;

    public DeliveryServiceImpl(SecurityManager securityManager, OrderRepository orderRepository) {
        this.securityManager = Objects.requireNonNull(securityManager, "SecurityManager cannot be null");
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository cannot be null");
        LOGGER.log(Level.INFO, "DeliveryService initialized with SecurityManager and OrderRepository");
    }

    @Override
    public List<Order> getOrdersReadyForDelivery(UserContext user) {
        LOGGER.log(Level.INFO, "Fetching orders ready for delivery for user: {0}", user.getUsername());
        securityManager.checkPermission(user, UserRole.DELIVERY);

        List<Order> orders = orderRepository.findByStatus(Order.OrderStatus.READY_FOR_DELIVERY);
        LOGGER.log(Level.INFO, "Found {0} orders ready for delivery for user: {1}",
                new Object[]{orders.size(), user.getUsername()});

        return orders;
    }

    @Override
    public synchronized void deliverOrder(UserContext user, String orderId) {
        LOGGER.log(Level.INFO, "Attempting to deliver order {0} by user: {1}",
                new Object[]{orderId, user.getUsername()});

        validateDeliveryRequest(user, orderId);

        Order order = retrieveValidOrder(orderId);
        validateOrderStatus(order);
        Validator.validateRoom(order.getRoom());

        updateOrderStatusToDelivered(order);
        LOGGER.log(Level.INFO, "Order {0} successfully delivered by user: {1}",
                new Object[]{orderId, user.getUsername()});
    }

    private void validateDeliveryRequest(UserContext user, String orderId) {
        Objects.requireNonNull(user, "User context cannot be null");
        Objects.requireNonNull(orderId, "Order ID cannot be null");
        securityManager.checkPermission(user, UserRole.DELIVERY);
    }

    private Order retrieveValidOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    String errorMsg = String.format("Order not found with id: %s", orderId);
                    LOGGER.log(Level.SEVERE, errorMsg);
                    return new IllegalArgumentException(errorMsg);
                });
    }

    private void validateOrderStatus(Order order) {
        if (order.getStatus() != Order.OrderStatus.READY_FOR_DELIVERY) {
            String errorMsg = String.format("Invalid order status: %s for order: %s. Expected: READY_FOR_DELIVERY",
                    order.getStatus(), order.getId());
            LOGGER.log(Level.WARNING, errorMsg);
            throw new IllegalStateException(errorMsg);
        }
    }

    private void updateOrderStatusToDelivered(Order order) {
        order.setStatus(Order.OrderStatus.DELIVERED);
        orderRepository.delete(order);
    }
}