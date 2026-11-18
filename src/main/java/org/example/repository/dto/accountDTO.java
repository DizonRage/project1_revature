package org.example.repository.dto;

public class accountDTO {
    private int account_id;
    private String account_number;
    private String account_type;
    private double balance;

    public accountDTO() {
    }

    public accountDTO(String account_number, String account_type, int account_id, double balance) {
        this.account_number = account_number;
        this.account_type = account_type;
        this.account_id = account_id;
        this.balance = balance;
    }

    public accountDTO(double balance, String account_type, String account_number) {
        this.balance = balance;
        this.account_type = account_type;
        this.account_number = account_number;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }
}
