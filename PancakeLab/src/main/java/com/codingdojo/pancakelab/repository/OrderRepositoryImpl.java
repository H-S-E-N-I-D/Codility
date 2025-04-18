package com.codingdojo.pancakelab.repository;

import com.codingdojo.pancakelab.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class OrderRepositoryImpl implements OrderRepository {
    private final ConcurrentMap<String, Order> orders = new ConcurrentHashMap<>();

    @Override
    public synchronized Order save(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        orders.put(order.getId(), order);
        return order;
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public synchronized void delete(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        orders.remove(order.getId());
    }

    @Override
    public List<Order> findByStatus(Order.OrderStatus status) {
        return orders.values().stream()
                .filter(Objects::nonNull)
                .filter(order -> status.equals(order.getStatus()))
                .collect(Collectors.toList());
    }


}
