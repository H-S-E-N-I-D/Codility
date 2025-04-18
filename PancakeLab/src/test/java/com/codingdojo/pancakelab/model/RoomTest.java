package com.codingdojo.pancakelab.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void testConstructorAndGetters() {
        Building building = new Building("Main");
        Room room = new Room("101", building);

        assertEquals("101", room.getNumber());
        assertEquals(building, room.getBuilding());
    }

    @Test
    void testEquals_SameInstance() {
        Building building = new Building("North");
        Room room = new Room("201", building);

        assertTrue(room.equals(room));
    }

    @Test
    void testEquals_EqualRooms() {
        Building building1 = new Building("West");
        Building building2 = new Building("West");
        Room room1 = new Room("301", building1);
        Room room2 = new Room("301", building2);

        assertTrue(room1.equals(room2));
        assertTrue(room2.equals(room1));
    }

    @Test
    void testEquals_DifferentRoomNumbers() {
        Building building = new Building("East");
        Room room1 = new Room("401", building);
        Room room2 = new Room("402", building);

        assertFalse(room1.equals(room2));
        assertFalse(room2.equals(room1));
    }

    @Test
    void testEquals_DifferentBuildings() {
        Building building1 = new Building("South");
        Building building2 = new Building("South Annex");
        Room room1 = new Room("501", building1);
        Room room2 = new Room("501", building2);

        assertFalse(room1.equals(room2));
        assertFalse(room2.equals(room1));
    }

    @Test
    void testEquals_NullComparison() {
        Building building = new Building("Central");
        Room room = new Room("601", building);

        assertFalse(room.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        Building building = new Building("Annex");
        Room room = new Room("701", building);

        assertFalse(room.equals("701 in Annex"));
    }

    @Test
    void testHashCode_EqualObjects() {
        Building building1 = new Building("Tower");
        Building building2 = new Building("Tower");
        Room room1 = new Room("801", building1);
        Room room2 = new Room("801", building2);

        assertEquals(room1.hashCode(), room2.hashCode());
    }

    @Test
    void testHashCode_DifferentObjects() {
        Building building1 = new Building("Main");
        Building building2 = new Building("Secondary");
        Room room1 = new Room("901", building1);
        Room room2 = new Room("902", building2);

        assertNotEquals(room1.hashCode(), room2.hashCode());
    }

    @Test
    void testToString() {
        Building building = new Building("Library");
        Room room = new Room("Study", building);
        String expected = "Room{number='Study', building=" + building.toString() + "}";

        assertEquals(expected, room.toString());
    }
}