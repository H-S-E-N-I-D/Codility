package com.codingdojo.pancakelab.service;

import com.codingdojo.pancakelab.model.Order;
import com.codingdojo.pancakelab.security.UserContext;

import java.util.List;

public interface ChefService {

    List<Order> getPendingOrders(UserContext user);

    Order prepareOrder(UserContext user, String orderId);
}
