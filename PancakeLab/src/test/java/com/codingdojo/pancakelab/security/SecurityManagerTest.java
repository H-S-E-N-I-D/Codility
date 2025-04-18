package com.codingdojo.pancakelab.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityManagerTest {

    private final SecurityManager securityManager = new SecurityManager();

    @Test
    void checkPermission_ShouldAllowAccessWhenRoleMatches() {
        // Arrange
        UserRole requiredRole = UserRole.CHEF;
        UserContext user = mock(UserContext.class);
        when(user.getRole()).thenReturn(requiredRole);

        // Act & Assert
        assertDoesNotThrow(() -> securityManager.checkPermission(user, requiredRole));
    }

    @Test
    void checkPermission_ShouldThrowSecurityExceptionWhenUserIsNull() {
        // Arrange
        UserRole requiredRole = UserRole.DISCIPLE;

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class,
                () -> securityManager.checkPermission(null, requiredRole));

        assertEquals("Unauthorized access", exception.getMessage());
    }

    @Test
    void checkPermission_ShouldThrowSecurityExceptionWhenRoleDoesNotMatch() {
        // Arrange
        UserRole requiredRole = UserRole.DELIVERY;
        UserRole userRole = UserRole.DISCIPLE;
        UserContext user = mock(UserContext.class);
        when(user.getRole()).thenReturn(userRole);

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class,
                () -> securityManager.checkPermission(user, requiredRole));

        assertEquals("Unauthorized access", exception.getMessage());
    }

    @Test
    void checkPermission_ShouldThrowSecurityExceptionWhenUserRoleIsNull() {
        // Arrange
        UserRole requiredRole = UserRole.DISCIPLE;
        UserContext user = mock(UserContext.class);
        when(user.getRole()).thenReturn(null);

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class,
                () -> securityManager.checkPermission(user, requiredRole));

        assertEquals("Unauthorized access", exception.getMessage());
    }

    @Test
    void checkPermission_ShouldWorkWithAllUserRoleValues() {
        // Test all enum values to ensure complete coverage
        for (UserRole requiredRole : UserRole.values()) {
            for (UserRole userRole : UserRole.values()) {
                // Arrange
                UserContext user = mock(UserContext.class);
                when(user.getRole()).thenReturn(userRole);

                if (userRole == requiredRole) {
                    // Act & Assert
                    assertDoesNotThrow(() -> securityManager.checkPermission(user, requiredRole));
                } else {
                    // Act & Assert
                    assertThrows(SecurityException.class,
                            () -> securityManager.checkPermission(user, requiredRole));
                }
            }
        }
    }
}