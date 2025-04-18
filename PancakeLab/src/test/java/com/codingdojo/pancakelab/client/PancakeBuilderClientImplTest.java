package com.codingdojo.pancakelab.client;

import com.codingdojo.pancakelab.model.Pancake;
import com.codingdojo.pancakelab.security.SecurityManager;
import com.codingdojo.pancakelab.security.UserContext;
import com.codingdojo.pancakelab.security.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PancakeBuilderClientImplTest {

    @Mock
    private SecurityManager securityManager;
    @Mock
    private UserContext discipleUser;
    @Mock
    private UserContext nonDiscipleUser;

    private PancakeBuilderClientImpl pancakeBuilderClient;

    @BeforeEach
    void setUp() {
        pancakeBuilderClient = new PancakeBuilderClientImpl(securityManager);
    }

    @Test
    void constructor_ShouldInitializeWithSecurityManager() {
        assertNotNull(pancakeBuilderClient);
    }

    @Test
    void buildDarkChocolatePancake_ShouldReturnCorrectPancake() {
        Pancake pancake = pancakeBuilderClient.buildDarkChocolatePancake(discipleUser);

        assertNotNull(pancake);
        assertEquals("DarkChocolate", pancake.getName());
        assertTrue(pancake.getIngredients().contains("dark-chocolate"));
        verify(securityManager).checkPermission(discipleUser, UserRole.DISCIPLE);
    }

    @Test
    void buildDarkChocolatePancake_ShouldThrowWhenNotDisciple() {
        doThrow(new SecurityException("Unauthorized"))
                .when(securityManager).checkPermission(nonDiscipleUser, UserRole.DISCIPLE);

        assertThrows(SecurityException.class,
                () -> pancakeBuilderClient.buildDarkChocolatePancake(nonDiscipleUser));
    }

    @Test
    void buildDarkChocolateWhippedCreamPancake_ShouldReturnCorrectPancake() {
        Pancake pancake = pancakeBuilderClient.buildDarkChocolateWhippedCreamPancake(discipleUser);

        assertNotNull(pancake);
        assertEquals("DarkChocolateWhippedCream", pancake.getName());
        assertTrue(pancake.getIngredients().contains("dark-chocolate"));
        assertTrue(pancake.getIngredients().contains("whipped-cream"));
        verify(securityManager).checkPermission(discipleUser, UserRole.DISCIPLE);
    }

    @Test
    void buildDarkChocolateWhippedCreamHazelnutPancake_ShouldReturnCorrectPancake() {
        Pancake pancake = pancakeBuilderClient.buildDarkChocolateWhippedCreamHazelnutPancake(discipleUser);

        assertNotNull(pancake);
        assertEquals("DarkChocolateWhippedCreamHazelnut", pancake.getName());
        assertTrue(pancake.getIngredients().contains("dark-chocolate"));
        assertTrue(pancake.getIngredients().contains("whipped-cream"));
        assertTrue(pancake.getIngredients().contains("hazelnuts"));
        verify(securityManager).checkPermission(discipleUser, UserRole.DISCIPLE);
    }

    @Test
    void buildMilkChocolatePancake_ShouldReturnCorrectPancake() {
        Pancake pancake = pancakeBuilderClient.buildMilkChocolatePancake(discipleUser);

        assertNotNull(pancake);
        assertEquals("MilkChocolate", pancake.getName());
        assertTrue(pancake.getIngredients().contains("milk-chocolate"));
        verify(securityManager).checkPermission(discipleUser, UserRole.DISCIPLE);
    }

    @Test
    void buildMilkChocolateHazelnutPancake_ShouldReturnCorrectPancake() {
        Pancake pancake = pancakeBuilderClient.buildMilkChocolateHazelnutPancake(discipleUser);

        assertNotNull(pancake);
        assertEquals("MilkChocolateHazelnut", pancake.getName());
        assertTrue(pancake.getIngredients().contains("milk-chocolate"));
        assertTrue(pancake.getIngredients().contains("hazelnuts"));
        verify(securityManager).checkPermission(discipleUser, UserRole.DISCIPLE);
    }

    @Test
    void allMethods_ShouldCheckDisciplePermission() {
        // Verify all pancake building methods check for DISCIPLE role
        verifySecurityCheckForAllMethods(discipleUser);
    }

    private void verifySecurityCheckForAllMethods(UserContext user) {
        pancakeBuilderClient.buildDarkChocolatePancake(user);
        pancakeBuilderClient.buildDarkChocolateWhippedCreamPancake(user);
        pancakeBuilderClient.buildDarkChocolateWhippedCreamHazelnutPancake(user);
        pancakeBuilderClient.buildMilkChocolatePancake(user);
        pancakeBuilderClient.buildMilkChocolateHazelnutPancake(user);

        verify(securityManager, times(5)).checkPermission(user, UserRole.DISCIPLE);
    }
}