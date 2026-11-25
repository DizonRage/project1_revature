package org.example.service;

import org.example.repository.TransactionDAO;
import org.example.entity.Transaction;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class TransactionService {

    TransactionDAO transactionDAO;

    public TransactionService() {
        transactionDAO = new TransactionDAO();
    }

    // Newest first, only for an owner (account_owners) or ADMIN; AccountNotFoundException, ForbiddenException. @Transactional: no (readOnly optional).
    public List<Transaction> getTransactionsForAccount(Integer accountId, Integer userId, String role) {
        throw new UnsupportedOperationException("TODO: implement");
    }

    public int addTransaction(Transaction transaction) throws SQLException {
        return transactionDAO.create(transaction);
    }

    public Transaction findTransaction(int id) throws SQLException {
        return transactionDAO.findById(id);
    }
}
