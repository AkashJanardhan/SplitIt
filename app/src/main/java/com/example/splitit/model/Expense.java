package com.example.splitit.model;

public class Expense {
    private long id;
    private String description;
    private double amount;
    private long payerId;
    private long groupId;
    private String payerName;

    public Expense(long id, String description, double amount, long payerId, long groupId, String payerName) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.payerId = payerId;
        this.groupId = groupId;
        this.payerName = payerName;
    }

    public Expense(String description, double amount, long payerId, long groupId) {
        this.description = description;
        this.amount = amount;
        this.payerId = payerId;
        this.groupId = groupId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public long getId() { return id; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public long getPayerId() { return payerId; }
    public long getGroupId() { return groupId; }
    public void setId(long id) { this.id = id; }
}

