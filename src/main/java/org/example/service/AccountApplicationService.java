package org.example.service;

import org.example.repository.AccountApplicationDAO;
import org.example.entity.AccountApplication;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class AccountApplicationService {


    AccountApplicationDAO accountApplicationDAO;

    public AccountApplicationService() {
        this.accountApplicationDAO = new AccountApplicationDAO();
    }

    // Create a PENDING application with userId as applicant and no approver; no business exceptions. @Transactional: no (single insert).
    public AccountApplication submitApplication(Integer userId, String accountType) {
        throw new UnsupportedOperationException("TODO: implement");
    }

    // Return only applications whose applicant is userId; no exceptions. @Transactional: no.
    public List<AccountApplication> getApplicationsForUser(Integer userId) {
        throw new UnsupportedOperationException("TODO: implement");
    }

    // Admin listing: all applications when status is null or blank, otherwise only that status; no exceptions. @Transactional: no.
    public List<AccountApplication> getApplications(String status) {
        throw new UnsupportedOperationException("TODO: implement");
    }

    // Only PENDING can become APPROVED or DENIED; record approverId; APPROVED also creates the OPEN account and its owner row. ApplicationNotFoundException, InvalidApplicationStateException. @Transactional: yes.
    public AccountApplication decideApplication(Integer applicationId, String status, Integer approverId) {
        throw new UnsupportedOperationException("TODO: implement");
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
