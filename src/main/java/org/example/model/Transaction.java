package org.example.model;

public class Transaction {

    private int transaction_id;
    private int account_id;
    private String transaction_type;
    private double amount;

    public Transaction() {
    }

    public Transaction(int transaction_id, int account_id, String transaction_type, double amount) {
        this.transaction_id = transaction_id;
        this.account_id = account_id;
        this.transaction_type = transaction_type;
        this.amount = amount;
    }

    public Transaction(int account_id, double amount) {
        this.account_id = account_id;
        this.amount = amount;
    }

    public Transaction(int account_id, String transaction_type, double amount) {
        this.account_id = account_id;
        this.transaction_type = transaction_type;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    @Override
    public String toString() {
        return "Transaction ID: " + transaction_id +
                "\nAccount ID: " + account_id +
                "\nTransaction Type: " + transaction_type +
                "\nAmount: " + amount + "\n";
    }
}
