package org.example.repository;

import org.example.exception.DataAccessException;
import org.example.model.Account;
import org.example.util.DatabaseConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public int create(Account account, int applicationId) {
        String sql = "INSERT INTO accounts (application_id, account_number, account_type, balance) VALUES (?, ?, ?, ?) RETURNING account_id;";

        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, applicationId);
            ps.setString(2, account.getAccountNumber());
            ps.setString(3, account.getAccountType());
            ps.setBigDecimal(4, account.getBalance());
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int accountId = rs.getInt("account_id");
                    account.setAccountId(accountId);
                    return accountId;
                }
            }throw new SQLException("Creating user failed, no ID obtained");
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create account", e);
        }
    }

    public Account findById(int accountId) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?;";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new Account(rs.getInt("account_id"), rs.getInt("application_id"), rs.getString("account_number"), rs.getString("account_type"), rs.getBigDecimal("balance"));
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find account", e);
        }
    }

    public Account findByNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?;";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new Account(rs.getInt("account_id"), rs.getInt("application_id"), rs.getString("account_number"), rs.getString("account_type"), rs.getBigDecimal("balance"));
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find account", e);
        }
    }

    public List<Account> findAll() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts ORDER BY account_id";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Account(rs.getInt("account_id"), rs.getString("account_number"), rs.getString("account_type"), rs.getBigDecimal("balance")));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to list accounts", e);
        }
        return list;
    }

    public boolean update(Account account, int id) {
        String sql = "UPDATE accounts SET application_id = ?, account_number = ?, account_type = ?, balance = ? WHERE account_id = ?";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, account.getApplicationId());
            ps.setString(2, account.getAccountNumber());
            ps.setString(3, account.getAccountType());
            ps.setBigDecimal(4, account.getBalance());
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update account", e);
        }
    }

    public boolean delete(int accountId) {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete account", e);
        }
    }
}
