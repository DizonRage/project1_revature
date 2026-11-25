package org.example.service;

import org.example.repository.TransactionDAO;
import org.example.entity.Transaction;

import java.sql.SQLException;

public class TransactionService {

    TransactionDAO transactionDAO;

    public TransactionService() {
        transactionDAO = new TransactionDAO();
    }

    public int addTransaction(Transaction transaction) throws SQLException {
        return transactionDAO.create(transaction);
    }

    public Transaction findTransaction(int id) throws SQLException {
        return transactionDAO.findById(id);
    }
}
