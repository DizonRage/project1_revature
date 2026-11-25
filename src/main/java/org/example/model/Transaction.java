package org.example.model;

public class Transaction {

    private int transactionId;
    private int accountId;
    private String transactionType;
    private double amount;

    public Transaction() {
    }

    public Transaction(int transactionId, int accountId, String transactionType, double amount) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public Transaction(int accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    public Transaction(int accountId, String transactionType, double amount) {
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "Transaction ID: " + transactionId +
                "\nAccount ID: " + accountId +
                "\nTransaction Type: " + transactionType +
                "\nAmount: " + amount + "\n";
    }
}
