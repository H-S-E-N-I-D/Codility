package com.codingdojo.pancakelab.service;

import com.codingdojo.pancakelab.model.Order;
import com.codingdojo.pancakelab.repository.OrderRepository;
import com.codingdojo.pancakelab.security.SecurityManager;
import com.codingdojo.pancakelab.security.UserContext;
import com.codingdojo.pancakelab.security.UserRole;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChefServiceImpl implements ChefService {
    private static final Logger LOGGER = Logger.getLogger(ChefServiceImpl.class.getName());

    private final OrderRepository orderRepository;
    private final SecurityManager securityManager;

    public ChefServiceImpl(SecurityManager securityManager, OrderRepository orderRepository) {
        this.securityManager = Objects.requireNonNull(securityManager, "SecurityManager cannot be null");
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository cannot be null");
        LOGGER.log(Level.INFO, "ChefService initialized with SecurityManager and OrderRepository");
    }

    @Override
    public List<Order> getPendingOrders(UserContext user) {
        LOGGER.log(Level.INFO, "Attempting to get pending orders for user: {0}", user.getUsername());
        securityManager.checkPermission(user, UserRole.CHEF);

        List<Order> orders = orderRepository.findByStatus(Order.OrderStatus.COMPLETED);
        LOGGER.log(Level.INFO, "Retrieved {0} pending orders for chef: {1}",
                new Object[]{orders.size(), user.getUsername()});

        return orders;
    }

    @Override
    public synchronized Order prepareOrder(UserContext user, String orderId) {
        LOGGER.log(Level.INFO, "Attempting to prepare order {0} for user: {1}",
                new Object[]{orderId, user.getUsername()});
        securityManager.checkPermission(user, UserRole.CHEF);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    String errorMsg = String.format("Order not found with id: %s for user: %s",
                            orderId, user.getUsername());
                    LOGGER.log(Level.SEVERE, errorMsg);
                    return new IllegalArgumentException(errorMsg);
                });

        if (order.getStatus() != Order.OrderStatus.COMPLETED) {
            String errorMsg = String.format("Invalid order status: %s for order: %s. Expected: COMPLETED",
                    order.getStatus(), orderId);
            LOGGER.log(Level.WARNING, errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        LOGGER.log(Level.INFO, "Preparing order {0} for delivery by chef: {1}",
                new Object[]{orderId, user.getUsername()});
        order.setStatus(Order.OrderStatus.READY_FOR_DELIVERY);

        Order preparedOrder = orderRepository.save(order);
        LOGGER.log(Level.INFO, "Order {0} successfully prepared for delivery by chef: {1}",
                new Object[]{orderId, user.getUsername()});

        return preparedOrder;
    }
}