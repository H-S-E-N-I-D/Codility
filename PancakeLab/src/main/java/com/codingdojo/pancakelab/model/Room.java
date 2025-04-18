package com.codingdojo.pancakelab.model;

import java.util.Objects;

public final class Room {

    private String number;
    private Building building;

    public Room(String number, Building building) {
        this.number = number;
        this.building = building;
    }

    public String getNumber() {
        return number;
    }

    public Building getBuilding() {
        return building;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return number.equals(room.number) && building.equals(room.building);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, building);
    }

    @Override
    public String toString() {
        return "Room{" +
                "number='" + number + '\'' +
                ", building=" + building +
                '}';
    }
}
