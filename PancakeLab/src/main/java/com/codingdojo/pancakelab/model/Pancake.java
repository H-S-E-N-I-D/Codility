package com.codingdojo.pancakelab.model;

import java.util.List;

public class Pancake {

    private String name;
    private List<String> ingredients;

    public Pancake(String name, List<String> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        return "Pancake{" +
                "name='" + name + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}
