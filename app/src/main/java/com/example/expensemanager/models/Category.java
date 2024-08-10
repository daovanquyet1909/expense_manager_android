package com.example.expensemanager.models;

public class Category {
    private String id;
    private String name;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Override toString to display the category name in the Spinner
    @Override
    public String toString() {
        return name;
    }
}
