package com.codingdojo.pancakelab.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Order {
    private final String id;
    private Room room;
    private List<Pancake> pancakes;
    private OrderStatus status;

    public enum OrderStatus {
        CREATED, COMPLETED, PREPARING, READY_FOR_DELIVERY, DELIVERED, CANCELLED
    }

    public Order(Room room) {
        this.id = UUID.randomUUID().toString();
        this.room = room;
        this.pancakes = new ArrayList<>();
        this.status = OrderStatus.CREATED;
    }

    public String getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public List<Pancake> getPancakes() {
        return new ArrayList<>(pancakes);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void addPancake(Pancake pancake) {
        pancakes.add(pancake);
    }

    public void removePancake(Pancake pancake) {
        pancakes.remove(pancake);
    }

    public void completeOrder() {
        this.status = OrderStatus.COMPLETED;
    }

    public void cancelOrder() {
        this.status = OrderStatus.CANCELLED;
    }

    @Override
    public String toString() {
        return "Order{\n" +
                "Id = '" + id + "\',\n" +
                "Room = '" + room.getNumber() + "\',\n" +
                "Building = '" + room.getBuilding().getName() + "\',\n" +
                "pancakes = " + pancakes + "\',\n" +
                "status = " + status + "\n" +
                '}';
    }

}
