package com.codingdojo.pancakelab.client;

import com.codingdojo.pancakelab.model.Building;
import com.codingdojo.pancakelab.model.Order;
import com.codingdojo.pancakelab.model.Pancake;
import com.codingdojo.pancakelab.model.Room;
import com.codingdojo.pancakelab.security.UserContext;

import java.util.List;

public interface PancakeLabClient {

    // Disciple operations
    Order createOrder(UserContext user, Room room);

    Order getOrder(UserContext user, String orderId);

    Order addPancakeToOrder(UserContext user, String orderId, Pancake pancake);

    Order addMultiplePancakesToOrder(UserContext user, String orderId, Pancake pancake, int count);

    Order removeMultiplePancakesFromOrder(UserContext user, String orderId, Pancake pancake, int count);

    Order removePancakeFromOrder(UserContext user, String orderId, Pancake pancake);

    Order completeOrder(UserContext user, String orderId);

    void cancelOrder(UserContext user, String orderId);

    // Chef operations
    List<Order> getPendingOrders(UserContext user);

    Order prepareOrder(UserContext user, String orderId);

    // Delivery operations
    List<Order> getOrdersReadyForDelivery(UserContext user);

    void deliverOrder(UserContext user, String orderId);

    // Utility operations
    List<Building> getAvailableBuildings();


}
