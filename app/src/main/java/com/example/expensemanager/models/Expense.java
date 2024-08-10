package com.example.expensemanager.models;

public class Expense {
    private String id;
    private double amount;
    private Category category_id;  // Now it's an object of Category
    private String date;
    private String note;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Category getCategory() { return category_id; }
    public void setCategory(Category category_id) { this.category_id = category_id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
