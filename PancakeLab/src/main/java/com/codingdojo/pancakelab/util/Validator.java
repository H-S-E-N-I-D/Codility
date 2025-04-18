package com.codingdojo.pancakelab.util;

import com.codingdojo.pancakelab.model.Building;
import com.codingdojo.pancakelab.model.Pancake;
import com.codingdojo.pancakelab.model.Room;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Validator {
    private static final Logger LOGGER = Logger.getLogger(Validator.class.getName());

    private static final List<String> ALLOWED_BUILDINGS = Collections.unmodifiableList(
            Arrays.asList("Main", "North", "South", "East", "West"));

    private static final List<String> ALLOWED_INGREDIENTS = Collections.unmodifiableList(
            Arrays.asList("dark-chocolate", "whipped-cream", "milk-chocolate", "hazelnuts"));

    // Private constructor to prevent instantiation
    Validator() {
        throw new AssertionError("Validator class cannot be instantiated");
    }

    static {
        LOGGER.setLevel(Level.FINE);
    }

    public static void validateRoom(Room room) {
        LOGGER.log(Level.FINE, "Validating room: {0}", room);
        Objects.requireNonNull(room, "Room cannot be null");
        validateBuilding(room.getBuilding());

        if (room.getNumber() == null || room.getNumber().trim().isEmpty()) {
            String errorMsg = "Room number cannot be empty";
            LOGGER.log(Level.WARNING, errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        LOGGER.log(Level.INFO, "Room validation successful: {0}", room.getNumber());
    }

    public static void validateBuilding(Building building) {
        LOGGER.log(Level.FINE, "Validating building: {0}", building);

        Objects.requireNonNull(building, "Building cannot be null");

        if (!ALLOWED_BUILDINGS.contains(building.getName())) {
            String errorMsg = String.format("Invalid building: %s", building.getName());
            LOGGER.log(Level.WARNING, errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        LOGGER.log(Level.INFO, "Building validation successful: {0}", building.getName());
    }

    public static void validatePancake(Pancake pancake) {
        LOGGER.log(Level.FINE, "Validating pancake: {0}", pancake);

        Objects.requireNonNull(pancake, "Pancake cannot be null");

        if (pancake.getName() == null || pancake.getName().trim().isEmpty()) {
            String errorMsg = "Pancake name cannot be empty";
            LOGGER.log(Level.WARNING, errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        for (String ingredient : pancake.getIngredients()) {
            if (!ALLOWED_INGREDIENTS.contains(ingredient.toLowerCase())) {
                String errorMsg = String.format("Ingredient is not allowed: %s", ingredient);
                LOGGER.log(Level.WARNING, errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
        }

        LOGGER.log(Level.INFO, "Pancake validation successful: {0}", pancake.getName());
    }

    public static List<String> getAllowedBuildings() {
        LOGGER.log(Level.FINE, "Retrieving allowed buildings");
        return ALLOWED_BUILDINGS;
    }
}