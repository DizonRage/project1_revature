package org.example.service;

import org.example.repository.AccountApplicationDAO;
import org.example.model.AccountApplication;
import org.example.model.Account;


public class AccountApplicationService {


    AccountApplicationDAO accountApplicationDAO;

    public AccountApplicationService() {
        this.accountApplicationDAO = new AccountApplicationDAO();
    }

    public int addApplication(int id, String type) {
        return accountApplicationDAO.create(id, type);
    }

    public AccountApplication findApplicationById(int id) {
        return accountApplicationDAO.findById(id);
    }

    public boolean updateApplication(AccountApplication application, int approverId) {
        return accountApplicationDAO.update(application, approverId);
    }
}
