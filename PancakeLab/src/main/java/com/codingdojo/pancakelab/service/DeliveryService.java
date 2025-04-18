package com.codingdojo.pancakelab.service;

import com.codingdojo.pancakelab.model.Order;
import com.codingdojo.pancakelab.security.UserContext;

import java.util.List;

public interface DeliveryService {

    List<Order> getOrdersReadyForDelivery(UserContext user);

    void deliverOrder(UserContext user, String orderId);
}
