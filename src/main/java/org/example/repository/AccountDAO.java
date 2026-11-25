package org.example.repository;

import org.example.entity.Account;
import org.example.util.DatabaseConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public int create(Account account, int applicationId) throws SQLException {
        String sql = "INSERT INTO accounts (application_id, account_number, account_type, balance) VALUES (?, ?, ?, ?) RETURNING account_id;";

        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, applicationId);
            ps.setString(2, account.getAccountNumber());
            ps.setString(3, account.getAccountType());
            ps.setBigDecimal(4, account.getBalance());
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int account_id = rs.getInt("account_id");
                    account.setAccountId(account_id);
                    return account_id;
                }
            }throw new SQLException("Creating user failed, no ID obtained");
        }
    }

    public Account findById(int accountId) throws SQLException{
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

        }
    }

    public Account findByNumber(String accountNumber) throws SQLException{
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

        }
    }

    public List<Account> findAll() throws SQLException {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts ORDER BY account_id";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Account(rs.getInt("account_id"), rs.getString("account_number"), rs.getString("account_type"), rs.getBigDecimal("balance")));
            }
        }
        return list;
    }

    public boolean update(Account account, int id) throws SQLException {
        String sql = "UPDATE accounts SET application_id = ?, account_number = ?, account_type = ?, balance = ? WHERE account_id = ?";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, account.getApplicationId());
            ps.setString(2, account.getAccountNumber());
            ps.setString(3, account.getAccountType());
            ps.setBigDecimal(4, account.getBalance());
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int account_id) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, account_id);
            return ps.executeUpdate() > 0;
        }
    }
}
