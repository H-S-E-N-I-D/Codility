package com.codingdojo.pancakelab.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {

    @Test
    void testConstructorAndGetter() {
        String buildingName = "Main";
        Building building = new Building(buildingName);

        assertEquals(buildingName, building.getName());
    }

    @Test
    void testEquals_SameInstance() {
        Building building = new Building("East");
        assertTrue(building.equals(building));
    }

    @Test
    void testEquals_EqualBuildings() {
        Building building1 = new Building("Main");
        Building building2 = new Building("Main");

        assertTrue(building1.equals(building2));
        assertTrue(building2.equals(building1));
    }

    @Test
    void testEquals_DifferentBuildings() {
        Building building1 = new Building("South");
        Building building2 = new Building("North");

        assertFalse(building1.equals(building2));
        assertFalse(building2.equals(building1));
    }

    @Test
    void testEquals_NullComparison() {
        Building building = new Building("Northeast");
        assertFalse(building.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        Building building = new Building("Main");
        String notABuilding = "Main";

        assertFalse(building.equals(notABuilding));
    }

    @Test
    void testHashCode_EqualObjects() {
        Building building1 = new Building("West");
        Building building2 = new Building("West");

        assertEquals(building1.hashCode(), building2.hashCode());
    }

    @Test
    void testHashCode_DifferentObjects() {
        Building building1 = new Building("North");
        Building building2 = new Building("West");

        assertNotEquals(building1.hashCode(), building2.hashCode());
    }

    @Test
    void testToString() {
        String name = "Main";
        Building building = new Building(name);
        String expectedString = "Building{name='" + name + "'}";

        assertEquals(expectedString, building.toString());
    }
}