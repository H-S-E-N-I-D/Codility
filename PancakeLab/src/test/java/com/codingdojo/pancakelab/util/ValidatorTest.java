package com.codingdojo.pancakelab.util;

import com.codingdojo.pancakelab.model.Building;
import com.codingdojo.pancakelab.model.Pancake;
import com.codingdojo.pancakelab.model.Room;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ValidatorTest {

    @Mock
    private Handler logHandler;

    @Captor
    private ArgumentCaptor<LogRecord> logRecordCaptor;

    @Test
    void testConstructor_ShouldThrowAssertionError() {
        AssertionError error = assertThrows(AssertionError.class, () -> {
            new Validator();
        });
        assertEquals("Validator class cannot be instantiated", error.getMessage());
    }

    @Test
    void testGetAllowedBuildings_ShouldReturnUnmodifiableList() {
        List<String> result = Validator.getAllowedBuildings();
        assertEquals(Arrays.asList("Main", "North", "South", "East", "West"), result);

        // Test immutability
        assertThrows(UnsupportedOperationException.class, () -> {
            result.add("New Building");
        });
    }

    @Test
    void testValidateBuilding_WithValidBuilding_ShouldPass() {
        Building validBuilding = new Building("Main");
        Validator.validateBuilding(validBuilding);
        // No exception expected
    }

    @Test
    void testValidateBuilding_WithNullBuilding_ShouldThrowException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            Validator.validateBuilding(null);
        });
        assertEquals("Building cannot be null", exception.getMessage());
    }

    @Test
    void testValidateBuilding_WithInvalidBuilding_ShouldThrowException() {
        Building invalidBuilding = new Building("Invalid");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.validateBuilding(invalidBuilding);
        });
        assertEquals("Invalid building: Invalid", exception.getMessage());
    }

    @Test
    void testValidateBuilding_Logging_ShouldLogAppropriately() {
        Logger logger = Logger.getLogger(Validator.class.getName());
        logger.addHandler(logHandler);

        Building building = new Building("Main");
        Validator.validateBuilding(building);

        verify(logHandler, times(2)).publish(logRecordCaptor.capture());

        List<LogRecord> logRecords = logRecordCaptor.getAllValues();
        assertEquals(Level.FINE, logRecords.get(0).getLevel());
        assertTrue(logRecords.get(0).getMessage().contains("Validating building"));

        assertEquals(Level.INFO, logRecords.get(1).getLevel());
        assertTrue(logRecords.get(1).getMessage().contains("Building validation successful"));
    }

    @Test
    void testValidateRoom_WithValidRoom_ShouldPass() {
        Room validRoom = new Room("101", new Building("Main"));
        Validator.validateRoom(validRoom);
        // No exception expected
    }

    @Test
    void testValidateRoom_WithNullRoom_ShouldThrowException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            Validator.validateRoom(null);
        });
        assertEquals("Room cannot be null", exception.getMessage());
    }

    @Test
    void testValidateRoom_WithNullRoomNumber_ShouldThrowException() {
        Room room = new Room(null, new Building("Main"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.validateRoom(room);
        });
        assertEquals("Room number cannot be empty", exception.getMessage());
    }

    @Test
    void testValidateRoom_WithEmptyRoomNumber_ShouldThrowException() {
        Room room = new Room("", new Building("Main"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.validateRoom(room);
        });
        assertEquals("Room number cannot be empty", exception.getMessage());
    }

    @Test
    void testValidateRoom_WithInvalidBuilding_ShouldThrowException() {
        Room room = new Room("101", new Building("Invalid"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.validateRoom(room);
        });
        assertEquals("Invalid building: Invalid", exception.getMessage());
    }

    @Test
    void testValidateRoom_Logging_ShouldLogAppropriately() {
        Logger logger = Logger.getLogger(Validator.class.getName());
        logger.addHandler(logHandler);

        Room room = new Room("101", new Building("Main"));
        Validator.validateRoom(room);

        verify(logHandler, times(4)).publish(logRecordCaptor.capture());

        List<LogRecord> logRecords = logRecordCaptor.getAllValues();
        assertEquals(Level.FINE, logRecords.get(0).getLevel());
        assertTrue(logRecords.get(0).getMessage().contains("Validating room"));

        assertEquals(Level.FINE, logRecords.get(1).getLevel());
        assertTrue(logRecords.get(1).getMessage().contains("Validating building"));

        assertEquals(Level.INFO, logRecords.get(2).getLevel());
        assertTrue(logRecords.get(2).getMessage().contains("Building validation successful"));

        assertEquals(Level.INFO, logRecords.get(3).getLevel());
        assertTrue(logRecords.get(3).getMessage().contains("Room validation successful"));
    }

    @Test
    void testValidatePancake_WithValidPancake_ShouldPass() {
        Pancake validPancake = new Pancake("Chocolate", Collections.singletonList("milk-chocolate"));
        Validator.validatePancake(validPancake);
        // No exception expected
    }

    @Test
    void testValidatePancake_WithNullPancake_ShouldThrowException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            Validator.validatePancake(null);
        });
        assertEquals("Pancake cannot be null", exception.getMessage());
    }

    @Test
    void testValidatePancake_WithNullName_ShouldThrowException() {
        Pancake pancake = new Pancake(null, Collections.singletonList("milk-chocolate"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.validatePancake(pancake);
        });
        assertEquals("Pancake name cannot be empty", exception.getMessage());
    }

    @Test
    void testValidatePancake_WithEmptyName_ShouldThrowException() {
        Pancake pancake = new Pancake("", Collections.singletonList("milk-chocolate"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.validatePancake(pancake);
        });
        assertEquals("Pancake name cannot be empty", exception.getMessage());
    }

    @Test
    void testValidatePancake_WithInvalidIngredient_ShouldThrowException() {
        Pancake pancake = new Pancake("Invalid", Collections.singletonList("invalid-ingredient"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.validatePancake(pancake);
        });
        assertEquals("Ingredient is not allowed: invalid-ingredient", exception.getMessage());
    }

    @Test
    void testValidatePancake_WithMultipleIngredients_ShouldCheckAll() {
        Pancake pancake = new Pancake("Mixed", Arrays.asList("milk-chocolate", "invalid-ingredient"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.validatePancake(pancake);
        });
        assertEquals("Ingredient is not allowed: invalid-ingredient", exception.getMessage());
    }

    @Test
    void testValidatePancake_Logging_ShouldLogAppropriately() {
        Logger logger = Logger.getLogger(Validator.class.getName());
        logger.addHandler(logHandler);

        Pancake pancake = new Pancake("Chocolate", Collections.singletonList("milk-chocolate"));
        Validator.validatePancake(pancake);

        verify(logHandler, times(2)).publish(logRecordCaptor.capture());

        List<LogRecord> logRecords = logRecordCaptor.getAllValues();
        assertEquals(Level.FINE, logRecords.get(0).getLevel());
        assertTrue(logRecords.get(0).getMessage().contains("Validating pancake"));

        assertEquals(Level.INFO, logRecords.get(1).getLevel());
        assertTrue(logRecords.get(1).getMessage().contains("Pancake validation successful"));
    }
}