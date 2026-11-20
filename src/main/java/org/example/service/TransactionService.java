package org.example.service;

import org.example.repository.TransactionDAO;
import org.example.repository.UserDAO;
import org.example.repository.dto.TransactionDTO;
import org.example.repository.dto.UserDTO;

import java.sql.SQLException;

public class TransactionService {

    TransactionDAO transactionDAO;

    public TransactionService() {
        transactionDAO = new TransactionDAO();
    }

    public int addTransaction(TransactionDTO transaction) throws SQLException {
        return transactionDAO.create(transaction);
    }

    public TransactionDTO findTransaction(int id) throws SQLException {
        return transactionDAO.findById(id);
    }
}
