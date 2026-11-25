package org.example.service;

import org.example.repository.TransactionDAO;
import org.example.repository.UserDAO;
import org.example.model.Transaction;
import org.example.model.User;


public class TransactionService {

    TransactionDAO transactionDAO;

    public TransactionService() {
        transactionDAO = new TransactionDAO();
    }

    public int addTransaction(Transaction transaction) {
        return transactionDAO.create(transaction);
    }

    public Transaction findTransaction(int id) {
        return transactionDAO.findById(id);
    }
}
