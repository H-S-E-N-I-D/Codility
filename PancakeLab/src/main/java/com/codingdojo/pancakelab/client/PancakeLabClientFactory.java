package com.codingdojo.pancakelab.client;

import com.codingdojo.pancakelab.repository.OrderRepository;
import com.codingdojo.pancakelab.repository.OrderRepositoryImpl;
import com.codingdojo.pancakelab.security.SecurityManager;
import com.codingdojo.pancakelab.service.*;

public class PancakeLabClientFactory {

    private PancakeLabClientFactory() {
        //Private access to the constructor
    }

    public static PancakeLabClient createClient() {
        OrderRepository orderRepository = new OrderRepositoryImpl();
        SecurityManager securityManager = new SecurityManager();

        OrderService orderService = new OrderServiceImpl(securityManager, orderRepository);
        ChefService chefService = new ChefServiceImpl(securityManager, orderRepository);
        DeliveryService deliveryService = new DeliveryServiceImpl(securityManager, orderRepository);

        return new PancakeLabClientImpl(
                orderService,
                chefService,
                deliveryService,
                securityManager
        );
    }

    public static PancakeBuilderClient createPancakeBuilderClient() {
        return new PancakeBuilderClientImpl(
                new SecurityManager()
        );
    }

}
