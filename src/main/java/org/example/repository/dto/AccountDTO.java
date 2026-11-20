package org.example.repository.dto;

public class AccountDTO {
    private int account_id;
    private int application_id;
    private String account_number;
    private String account_type;
    private double balance;

    public AccountDTO() {
    }

    public AccountDTO(int account_id, int application_id, String account_number, String account_type, double balance) {
        this.account_id = account_id;
        this.application_id = application_id;
        this.account_number = account_number;
        this.account_type = account_type;
        this.balance = balance;
    }

    public AccountDTO(int application_id, String account_number, String account_type, double balance) {
        this.application_id = application_id;
        this.account_number = account_number;
        this.account_type = account_type;
        this.balance = balance;
    }

    public AccountDTO(String account_number, String account_type, double balance) {
        this.account_number = account_number;
        this.account_type = account_type;
        this.balance = balance;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getApplication_id() {
        return application_id;
    }

    public void setApplication_id(int application_id) {
        this.application_id = application_id;
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

    @Override
    public String toString() {
        return "Account Number: " + account_number +
                "\nAccount Type: " + account_type +
                "\nBalance:" + balance + "\n";
    }
}
