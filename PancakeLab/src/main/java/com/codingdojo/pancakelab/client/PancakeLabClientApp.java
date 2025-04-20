package com.codingdojo.pancakelab.client;

import com.codingdojo.pancakelab.model.Building;
import com.codingdojo.pancakelab.model.Order;
import com.codingdojo.pancakelab.model.Pancake;
import com.codingdojo.pancakelab.model.Room;
import com.codingdojo.pancakelab.security.UserContext;
import com.codingdojo.pancakelab.security.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PancakeLabClientApp {
    private static final Logger LOGGER = Logger.getLogger(PancakeLabClientApp.class.getName());
    private static final int THREAD_DELAY_MS = 500;
    private static final int ORDER_TASKS_COUNT = 5;
    private static final int CANCEL_TASKS_COUNT = 5;


    public void run() {
        try {
            PancakeLabClient client = PancakeLabClientFactory.createClient();
            PancakeBuilderClient pancakeBuilder = PancakeLabClientFactory.createPancakeBuilderClient();

            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            // Create latch with total task count
            CountDownLatch taskLatch = new CountDownLatch(ORDER_TASKS_COUNT + CANCEL_TASKS_COUNT);

            List<Callable<Void>> tasks = createOrderTasks(client, pancakeBuilder, taskLatch);

            // Log initial task count
            LOGGER.log(Level.INFO, "Submitted {0} tasks (delivery: {1} , cancellation: {2} )", new Object[]{ORDER_TASKS_COUNT + CANCEL_TASKS_COUNT,
                    ORDER_TASKS_COUNT,
                    CANCEL_TASKS_COUNT});

            processTasks(executor, tasks, taskLatch);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "PancakeLab error", e);
        }
    }

    private List<Callable<Void>> createOrderTasks(PancakeLabClient client,
                                                  PancakeBuilderClient pancakeBuilder,
                                                  CountDownLatch taskLatch) {
        List<Callable<Void>> tasks = new ArrayList<>();
        OrderProcessor processor = new OrderProcessor(client, pancakeBuilder);

        // Delivery tasks
        for (int i = 0; i < ORDER_TASKS_COUNT; i++) {
            tasks.add(() -> {
                try {
                    processor.placeOrderAndDeliver();
                    LOGGER.fine("Completed delivery task");
                } finally {
                    taskLatch.countDown();
                    LOGGER.fine("Remaining tasks: " + taskLatch.getCount());
                }
                return null;
            });
        }

        // Cancellation tasks
        for (int i = 0; i < CANCEL_TASKS_COUNT; i++) {
            tasks.add(() -> {
                try {
                    processor.placeOrderAndCancel();
                    LOGGER.fine("Completed cancellation task");
                } finally {
                    taskLatch.countDown();
                    LOGGER.fine("Remaining tasks: " + taskLatch.getCount());
                }
                return null;
            });
        }

        return tasks;
    }

    private void processTasks(ExecutorService executor,
                              List<Callable<Void>> tasks,
                              CountDownLatch taskLatch) {
        try {
            List<Future<Void>> futures = executor.invokeAll(tasks);

            // Create a thread to monitor completion
            new Thread(() -> {
                try {
                    taskLatch.await();
                    LOGGER.log(Level.INFO, "All {0} orders processed successfully", futures.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOGGER.warning("Task monitoring interrupted");
                }
            }).start();

            // Process futures as before
            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Order processing failed", e.getCause());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.WARNING, "Order processing interrupted", e);
        } finally {
            executor.shutdown();
        }
    }

    private class OrderProcessor {
        private final PancakeLabClient client;
        private final PancakeBuilderClient pancakeBuilder;

        public OrderProcessor(PancakeLabClient client, PancakeBuilderClient pancakeBuilder) {
            this.client = client;
            this.pancakeBuilder = pancakeBuilder;
        }

        public void placeOrderAndDeliver() throws InterruptedException {
            try {
                LOGGER.info(Thread.currentThread() + " started delivery order processing");

                UserContext disciple = new UserContext(UserRole.DISCIPLE, "disciple-1");
                UserContext chef = new UserContext(UserRole.CHEF, "chef-1");
                UserContext delivery = new UserContext(UserRole.DELIVERY, "delivery-1");

                Building building = client.getAvailableBuildings().get(0);
                Order order = createOrder(disciple, building, "101");

                addPancakesToOrder(disciple, order);
                completeAndDeliverOrder(disciple, chef, delivery, order);

                LOGGER.info(Thread.currentThread() + " finished delivery order processing");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Delivery order processing failed", e);
                throw e;
            }

        }

        public void placeOrderAndCancel() throws InterruptedException {
            try {
                LOGGER.info(Thread.currentThread() + " started cancellation order processing");

                UserContext disciple = new UserContext(UserRole.DISCIPLE, "disciple-2");
                Building building = client.getAvailableBuildings().get(1);
                Order order = createOrder(disciple, building, "102");

                addBasicPancakesToOrder(disciple, order);
                Thread.sleep(THREAD_DELAY_MS);
                client.cancelOrder(disciple, order.getId());

                LOGGER.info("Order cancelled: " + order.getId());
                LOGGER.info(Thread.currentThread() + " finished cancellation order processing");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Cancellation order processing failed", e);
                throw e;
            }

        }

        private Order createOrder(UserContext user, Building building, String roomNumber) {
            Room room = new Room(roomNumber, building);
            Order order = client.createOrder(user, room);
            LOGGER.info("Order created: " + order.getId());
            return order;
        }

        private void addPancakesToOrder(UserContext user, Order order) {
            Pancake[] pancakes = {
                    pancakeBuilder.buildMilkChocolatePancake(user),
                    pancakeBuilder.buildMilkChocolateHazelnutPancake(user),
                    pancakeBuilder.buildDarkChocolatePancake(user),
                    pancakeBuilder.buildDarkChocolateWhippedCreamPancake(user),
                    pancakeBuilder.buildDarkChocolateWhippedCreamHazelnutPancake(user)
            };

            client.addPancakeToOrder(user, order.getId(), pancakes[0]);
            client.addPancakeToOrder(user, order.getId(), pancakes[1]);
            client.addPancakeToOrder(user, order.getId(), pancakes[2]);
            client.addMultiplePancakesToOrder(user, order.getId(), pancakes[3], 2);
            client.addMultiplePancakesToOrder(user, order.getId(), pancakes[4], 3);
            client.removeMultiplePancakesFromOrder(user, order.getId(), pancakes[4], 2);
        }

        private void addBasicPancakesToOrder(UserContext user, Order order) {
            Pancake[] pancakes = {
                    pancakeBuilder.buildMilkChocolatePancake(user),
                    pancakeBuilder.buildMilkChocolateHazelnutPancake(user)
            };

            client.addPancakeToOrder(user, order.getId(), pancakes[0]);
            client.addPancakeToOrder(user, order.getId(), pancakes[1]);
        }

        private void completeAndDeliverOrder(UserContext disciple, UserContext chef,
                                             UserContext delivery, Order order)
                throws InterruptedException {
            order = client.completeOrder(disciple, order.getId());
            LOGGER.info("Order completed: " + order.getId());

            Order preparedOrder = client.prepareOrder(chef, order.getId());
            LOGGER.info("Order prepared: " + preparedOrder.getId());

            Thread.sleep(THREAD_DELAY_MS);
            client.deliverOrder(delivery, order.getId());
            LOGGER.info("Order delivered: " + order.getId());
        }
    }

}