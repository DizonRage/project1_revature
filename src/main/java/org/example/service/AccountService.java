package org.example.service;

import org.example.repository.AccountDAO;
import org.example.model.Account;

import java.sql.SQLException;
import java.util.List;

public class AccountService {

    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public int addAccount(Account account, int application_id) throws SQLException {
        return accountDAO.create(account, application_id);
    }

    public Account getAccountById(int id) throws SQLException {
        return accountDAO.findById(id);
    }

    public Account getAccountByNumber(String number) throws SQLException {
        return accountDAO.findByNumber(number);
    }

    public List<Account> getAllAccounts() throws SQLException {
        return accountDAO.findAll();
    }

    public boolean updateAccount(Account account, int id) throws SQLException {
        if(accountDAO.findById(account.getAccount_id()) != null){
            return accountDAO.update(account, id);
        }
        return false;
    }

    public boolean removeAccount(int id) throws SQLException {
        if(accountDAO.findById(id) != null){
            return accountDAO.delete(id);
        }
        return false;
    }
}
