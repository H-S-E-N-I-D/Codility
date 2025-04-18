package com.codingdojo.pancakelab.service;

import com.codingdojo.pancakelab.model.Order;
import com.codingdojo.pancakelab.model.Pancake;
import com.codingdojo.pancakelab.model.Room;
import com.codingdojo.pancakelab.security.UserContext;

public interface OrderService {

    Order createOrder(UserContext user, Room room);

    Order addPancakeToOrder(UserContext user, String orderId, Pancake pancake);

    Order removePancakeFromOrder(UserContext user, String orderId, Pancake pancake);

    Order completeOrder(UserContext user, String orderId);

    void cancelOrder(UserContext user, String orderId);

    Order getOrder(UserContext user, String orderId);
}
