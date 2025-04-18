package com.codingdojo.pancakelab.client;

import com.codingdojo.pancakelab.model.Pancake;
import com.codingdojo.pancakelab.security.UserContext;

public interface PancakeBuilderClient {

    Pancake buildDarkChocolatePancake(UserContext user);

    Pancake buildDarkChocolateWhippedCreamPancake(UserContext user);

    Pancake buildDarkChocolateWhippedCreamHazelnutPancake(UserContext user);

    Pancake buildMilkChocolatePancake(UserContext user);

    Pancake buildMilkChocolateHazelnutPancake(UserContext user);


}
