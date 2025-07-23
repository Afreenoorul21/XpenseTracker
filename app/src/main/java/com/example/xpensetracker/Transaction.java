package com.example.xpensetracker;

public class Transaction {
    private String title;
    private double amount;
    private String type;
    private String date;
    private String notes;

    public Transaction(String title, double amount, String type, String date, String notes) {
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }
}
