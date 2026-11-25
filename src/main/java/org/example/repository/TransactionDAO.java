package org.example.repository;

import org.example.exception.DataAccessException;
import org.example.model.Account;
import org.example.model.Transaction;
import org.example.model.User;
import org.example.util.DatabaseConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionDAO {

    public int create(Transaction transaction) {
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount) VALUES (?, ?, ?) RETURNING account_id;";

        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, transaction.getAccountId());
            ps.setString(2, transaction.getTransactionType());
            ps.setBigDecimal(3, transaction.getAmount());
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int accountId = rs.getInt("account_id");
                    transaction.setAccountId(accountId);
                    return accountId;
                }
            }throw new SQLException("Creating user failed, no ID obtained");
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create transaction", e);
        }
    }

    public Transaction findById(int transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?;";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, transactionId);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new Transaction(rs.getInt("transaction_id"), rs.getInt("account_id"), rs.getString("transaction_type"), rs.getBigDecimal("amount"));
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find transaction", e);
        }
    }

    public boolean update(Transaction transaction, int id) {
        String sql = "UPDATE transactions SET account_id = ?, transaction_type = ?, amount = ? WHERE transaction_id = ?";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, transaction.getAccountId());
            ps.setString(2, transaction.getTransactionType());
            ps.setBigDecimal(3, transaction.getAmount());
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update transaction", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete transaction", e);
        }
    }


}
