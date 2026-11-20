package org.example.service;

import org.example.repository.AccountApplicationDAO;
import org.example.repository.dto.AccountApplicationDTO;
import org.example.repository.dto.AccountDTO;

import java.sql.SQLException;

public class AccountApplicationService {


    AccountApplicationDAO accountApplicationDAO;

    public AccountApplicationService() {
        this.accountApplicationDAO = new AccountApplicationDAO();
    }

    public int addApplication(int id, String type) throws SQLException {
        return accountApplicationDAO.create(id, type);
    }

    public AccountApplicationDTO findApplicationById(int id) throws SQLException{
        return accountApplicationDAO.findById(id);
    }

    public boolean updateApplication(AccountApplicationDTO application, int approver_id) throws SQLException {
        return accountApplicationDAO.update(application, approver_id);
    }
}
