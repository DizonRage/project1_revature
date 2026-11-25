package org.example.service;

import org.example.repository.TransactionDAO;
import org.example.repository.UserDAO;
import org.example.model.Transaction;
import org.example.model.User;

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
