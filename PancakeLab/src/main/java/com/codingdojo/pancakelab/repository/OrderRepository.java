package com.codingdojo.pancakelab.repository;

import com.codingdojo.pancakelab.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(String id);

    List<Order> findAll();

    void delete(Order order);

    List<Order> findByStatus(Order.OrderStatus status);
}
