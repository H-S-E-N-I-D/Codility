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
import com.codingdojo.pancakelab.util.Validator;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PancakeLabClientImpl implements PancakeLabClient {
    private static final Logger LOGGER = Logger.getLogger(PancakeLabClientImpl.class.getName());

    private final OrderService orderService;
    private final ChefService chefService;
    private final DeliveryService deliveryService;
    private final SecurityManager securityManager;

    public PancakeLabClientImpl(OrderService orderService,
                                ChefService chefService,
                                DeliveryService deliveryService,
                                SecurityManager securityManager) {
        this.orderService = Objects.requireNonNull(orderService, "OrderService cannot be null");
        this.chefService = Objects.requireNonNull(chefService, "ChefService cannot be null");
        this.deliveryService = Objects.requireNonNull(deliveryService, "DeliveryService cannot be null");
        this.securityManager = Objects.requireNonNull(securityManager, "SecurityManager cannot be null");
        LOGGER.log(Level.INFO, "PancakeLabClient initialized with all required services");
    }

    @Override
    public Order createOrder(UserContext user, Room room) {
        LOGGER.log(Level.FINE, "Creating new order for user: {0}", user.getUsername());
        return orderService.createOrder(user, room);
    }

    @Override
    public Order getOrder(UserContext user, String orderId) {
        LOGGER.log(Level.FINE, "Retrieving order {0} for user: {1}", new Object[]{orderId, user.getUsername()});
        return orderService.getOrder(user, orderId);
    }

    @Override
    public Order addPancakeToOrder(UserContext user, String orderId, Pancake pancake) {
        LOGGER.log(Level.FINE, "Adding pancake to order {0} for user: {1}",
                new Object[]{orderId, user.getUsername()});
        return orderService.addPancakeToOrder(user, orderId, pancake);
    }

    @Override
    public Order addMultiplePancakesToOrder(UserContext user, String orderId, Pancake pancake, int count) {
        LOGGER.log(Level.FINE, "Adding {0} pancakes to order {1} for user: {2}",
                new Object[]{count, orderId, user.getUsername()});

        validatePancakeCount(count);
        Order order = orderService.getOrder(user, orderId);

        for (int i = 0; i < count; i++) {
            orderService.addPancakeToOrder(user, orderId, pancake);
        }

        LOGGER.log(Level.INFO, "Added {0} pancakes to order {1}", new Object[]{count, orderId});
        return order;
    }

    @Override
    public Order removePancakeFromOrder(UserContext user, String orderId, Pancake pancake) {
        LOGGER.log(Level.FINE, "Removing pancake from order {0} for user: {1}",
                new Object[]{orderId, user.getUsername()});
        return orderService.removePancakeFromOrder(user, orderId, pancake);
    }

    @Override
    public Order removeMultiplePancakesFromOrder(UserContext user, String orderId, Pancake pancake, int count) {
        LOGGER.log(Level.FINE, "Removing {0} pancakes from order {1} for user: {2}",
                new Object[]{count, orderId, user.getUsername()});

        validatePancakeCount(count);
        Order order = orderService.getOrder(user, orderId);

        for (int i = 0; i < count; i++) {
            orderService.removePancakeFromOrder(user, orderId, pancake);
        }

        LOGGER.log(Level.INFO, "Removed {0} pancakes from order {1}", new Object[]{count, orderId});
        return order;
    }

    @Override
    public Order completeOrder(UserContext user, String orderId) {
        LOGGER.log(Level.FINE, "Completing order {0} for user: {1}",
                new Object[]{orderId, user.getUsername()});
        return orderService.completeOrder(user, orderId);
    }

    @Override
    public void cancelOrder(UserContext user, String orderId) {
        LOGGER.log(Level.FINE, "Cancelling order {0} for user: {1}",
                new Object[]{orderId, user.getUsername()});
        orderService.cancelOrder(user, orderId);
    }

    @Override
    public List<Order> getPendingOrders(UserContext user) {
        LOGGER.log(Level.FINE, "Retrieving pending orders for chef: {0}", user.getUsername());
        return chefService.getPendingOrders(user);
    }

    @Override
    public Order prepareOrder(UserContext user, String orderId) {
        LOGGER.log(Level.FINE, "Preparing order {0} by chef: {1}",
                new Object[]{orderId, user.getUsername()});
        return chefService.prepareOrder(user, orderId);
    }

    @Override
    public List<Order> getOrdersReadyForDelivery(UserContext user) {
        LOGGER.log(Level.FINE, "Retrieving orders ready for delivery by: {0}", user.getUsername());
        return deliveryService.getOrdersReadyForDelivery(user);
    }

    @Override
    public void deliverOrder(UserContext user, String orderId) {
        LOGGER.log(Level.FINE, "Delivering order {0} by: {1}",
                new Object[]{orderId, user.getUsername()});
        deliveryService.deliverOrder(user, orderId);
    }

    @Override
    public List<Building> getAvailableBuildings() {
        LOGGER.log(Level.FINE, "Retrieving all available buildings");
        return Validator.getAllowedBuildings()
                .stream()
                .map(Building::new)
                .collect(Collectors.toList());
    }

    private void validatePancakeCount(int count) {
        if (count <= 0) {
            String errorMsg = "Pancake count must be positive: " + count;
            LOGGER.log(Level.WARNING, errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
    }
}