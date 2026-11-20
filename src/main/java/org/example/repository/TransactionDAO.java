package org.example.repository;

import org.example.repository.dto.AccountDTO;
import org.example.repository.dto.TransactionDTO;
import org.example.repository.dto.UserDTO;
import org.example.util.DatabaseConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionDAO {

    public int create(TransactionDTO transaction) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount) VALUES (?, ?, ?) RETURNING account_id;";

        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, transaction.getAccount_id());
            ps.setString(2, transaction.getTransaction_type());
            ps.setDouble(3, transaction.getAmount());
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int account_id = rs.getInt("account_id");
                    transaction.setAccount_id(account_id);
                    return account_id;
                }
            }throw new SQLException("Creating user failed, no ID obtained");
        }
    }

    public TransactionDTO findById(int transaction_id) throws SQLException{
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?;";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, transaction_id);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new TransactionDTO(rs.getInt("transaction_id"), rs.getInt("account_id"), rs.getString("transaction_type"), rs.getDouble("amount"));
                }
                return null;
            }

        }
    }

    public boolean update(TransactionDTO transaction, int id) throws SQLException {
        String sql = "UPDATE transactions SET account_id = ?, transaction_type = ?, amount = ? WHERE transaction_id = ?";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, transaction.getAccount_id());
            ps.setString(2, transaction.getTransaction_type());
            ps.setDouble(3, transaction.getAmount());
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }


}
