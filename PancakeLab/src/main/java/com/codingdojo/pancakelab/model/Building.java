package com.codingdojo.pancakelab.model;

import java.util.Objects;

public final class Building {
    private String name;

    public Building(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Building)) return false;
        Building building = (Building) o;
        return name.equals(building.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Building{" +
                "name='" + name + '\'' +
                '}';
    }
}
