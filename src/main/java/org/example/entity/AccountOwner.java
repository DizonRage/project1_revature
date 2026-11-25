package org.example.entity;

public class AccountOwner {

    private int accountId;
    private int userId;

    public AccountOwner() {
    }

    public AccountOwner(int accountId, int userId) {
        this.accountId = accountId;
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
