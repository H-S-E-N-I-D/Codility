package com.codingdojo.pancakelab.client;

import com.codingdojo.pancakelab.builder.PancakeBuilder;
import com.codingdojo.pancakelab.model.Pancake;
import com.codingdojo.pancakelab.security.SecurityManager;
import com.codingdojo.pancakelab.security.UserContext;
import com.codingdojo.pancakelab.security.UserRole;

public class PancakeBuilderClientImpl implements PancakeBuilderClient {
    private final SecurityManager securityManager;

    public PancakeBuilderClientImpl(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    @Override
    public Pancake buildDarkChocolatePancake(UserContext user) {
        securityManager.checkPermission(user, UserRole.DISCIPLE);
        return PancakeBuilder.newInstance()
                .withName("DarkChocolate")
                .withIngredient("dark-chocolate")
                .build();
    }

    @Override
    public Pancake buildDarkChocolateWhippedCreamPancake(UserContext user) {
        securityManager.checkPermission(user, UserRole.DISCIPLE);
        return PancakeBuilder.newInstance()
                .withName("DarkChocolateWhippedCream")
                .withIngredient("dark-chocolate")
                .withIngredient("whipped-cream")
                .build();
    }

    @Override
    public Pancake buildDarkChocolateWhippedCreamHazelnutPancake(UserContext user) {
        securityManager.checkPermission(user, UserRole.DISCIPLE);
        return PancakeBuilder.newInstance()
                .withName("DarkChocolateWhippedCreamHazelnut")
                .withIngredient("dark-chocolate")
                .withIngredient("whipped-cream")
                .withIngredient("hazelnuts")
                .build();
    }

    @Override
    public Pancake buildMilkChocolatePancake(UserContext user) {
        securityManager.checkPermission(user, UserRole.DISCIPLE);
        return PancakeBuilder.newInstance()
                .withName("MilkChocolate")
                .withIngredient("milk-chocolate")
                .build();
    }

    @Override
    public Pancake buildMilkChocolateHazelnutPancake(UserContext user) {
        securityManager.checkPermission(user, UserRole.DISCIPLE);
        return PancakeBuilder.newInstance()
                .withName("MilkChocolateHazelnut")
                .withIngredient("milk-chocolate")
                .withIngredient("hazelnuts")
                .build();
    }
}
