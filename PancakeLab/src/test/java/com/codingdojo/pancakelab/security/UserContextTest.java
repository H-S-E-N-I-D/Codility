package com.codingdojo.pancakelab.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserContextTest {

    @Test
    void constructor_ShouldSetRoleAndUsername() {
        // Arrange
        UserRole expectedRole = UserRole.CHEF;
        String expectedUsername = "chef-1";

        // Act
        UserContext userContext = new UserContext(expectedRole, expectedUsername);

        // Assert
        assertNotNull(userContext);
        assertEquals(expectedRole, userContext.getRole());
        assertEquals(expectedUsername, userContext.getUsername());
    }

    @Test
    void getRole_ShouldReturnAssignedRole() {
        // Arrange
        UserRole expectedRole = UserRole.DISCIPLE;
        UserContext userContext = new UserContext(expectedRole, "disciple-1");

        // Act
        UserRole actualRole = userContext.getRole();

        // Assert
        assertEquals(expectedRole, actualRole);
    }

    @Test
    void getUsername_ShouldReturnAssignedUsername() {
        // Arrange
        String expectedUsername = "delivery-2";
        UserContext userContext = new UserContext(UserRole.DELIVERY, expectedUsername);

        // Act
        String actualUsername = userContext.getUsername();

        // Assert
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void constructor_ShouldHandleNullUsername() {
        // Arrange & Act
        UserContext userContext = new UserContext(UserRole.CHEF, null);

        // Assert
        assertNull(userContext.getUsername());
        assertEquals(UserRole.CHEF, userContext.getRole());
    }

    @Test
    void constructor_ShouldHandleNullRole() {
        // Arrange & Act
        UserContext userContext = new UserContext(null, "testuser");

        // Assert
        assertNull(userContext.getRole());
        assertEquals("testuser", userContext.getUsername());
    }

    @Test
    void constructor_ShouldHandleEmptyUsername() {
        // Arrange & Act
        UserContext userContext = new UserContext(UserRole.DISCIPLE, "");

        // Assert
        assertEquals("", userContext.getUsername());
        assertEquals(UserRole.DISCIPLE, userContext.getRole());
    }

    @Test
    void constructor_ShouldHandleAllUserRoles() {
        // Test all enum values to ensure complete coverage
        for (UserRole role : UserRole.values()) {
            UserContext userContext = new UserContext(role, role.name().toLowerCase());
            assertEquals(role, userContext.getRole());
        }
    }
}