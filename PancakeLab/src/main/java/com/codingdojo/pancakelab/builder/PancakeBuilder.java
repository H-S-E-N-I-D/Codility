package com.codingdojo.pancakelab.builder;

import com.codingdojo.pancakelab.model.Pancake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PancakeBuilder {
    private String name;
    private List<String> ingredients = new ArrayList<>();

    public static PancakeBuilder newInstance() {
        return new PancakeBuilder();
    }

    private PancakeBuilder() {
    }

    public PancakeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PancakeBuilder withIngredient(String ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }

    public Pancake build() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Pancake name cannot be empty");
        }
        if (ingredients.isEmpty()) {
            throw new IllegalArgumentException("Pancake must have at least one ingredient");
        }
        return new Pancake(name, Collections.unmodifiableList(new ArrayList<>(ingredients)));

    }
}
