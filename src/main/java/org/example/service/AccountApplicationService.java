package org.example.service;

import org.example.repository.AccountApplicationDAO;
import org.example.model.AccountApplication;
import org.example.model.Account;

import java.sql.SQLException;

public class AccountApplicationService {


    AccountApplicationDAO accountApplicationDAO;

    public AccountApplicationService() {
        this.accountApplicationDAO = new AccountApplicationDAO();
    }

    public int addApplication(int id, String type) throws SQLException {
        return accountApplicationDAO.create(id, type);
    }

    public AccountApplication findApplicationById(int id) throws SQLException{
        return accountApplicationDAO.findById(id);
    }

    public boolean updateApplication(AccountApplication application, int approverId) throws SQLException {
        return accountApplicationDAO.update(application, approverId);
    }
}
