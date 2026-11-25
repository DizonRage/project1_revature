package org.example.service;

import org.example.repository.AccountDAO;
import org.example.model.Account;

import java.util.List;

public class AccountService {

    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public int addAccount(Account account, int applicationId) {
        return accountDAO.create(account, applicationId);
    }

    public Account getAccountById(int id) {
        return accountDAO.findById(id);
    }

    public Account getAccountByNumber(String number) {
        return accountDAO.findByNumber(number);
    }

    public List<Account> getAllAccounts() {
        return accountDAO.findAll();
    }

    public boolean updateAccount(Account account, int id) {
        if(accountDAO.findById(account.getAccountId()) != null){
            return accountDAO.update(account, id);
        }
        return false;
    }

    public boolean removeAccount(int id) {
        if(accountDAO.findById(id) != null){
            return accountDAO.delete(id);
        }
        return false;
    }
}
