package org.example.model;

import java.math.BigDecimal;

public class Account {
    private int accountId;
    private int applicationId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;

    public Account() {
    }

    public Account(int accountId, int applicationId, String accountNumber, String accountType, BigDecimal balance) {
        this.accountId = accountId;
        this.applicationId = applicationId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
    }

    public Account(int applicationId, String accountNumber, String accountType, BigDecimal balance) {
        this.applicationId = applicationId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
    }

    public Account(String accountNumber, String accountType, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "Account Number: " + accountNumber +
                "\nAccount Type: " + accountType +
                "\nBalance:" + balance + "\n";
    }
}
