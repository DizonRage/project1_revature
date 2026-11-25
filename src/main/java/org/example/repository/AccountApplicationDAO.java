package org.example.repository;

import org.example.model.AccountApplication;
import org.example.model.Account;
import org.example.util.DatabaseConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountApplicationDAO {

    public int create(int id, String type) throws SQLException {
        String sql = "INSERT INTO account_applications (applicant_id, account_type) VALUES (?, ?) RETURNING application_id;";

        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);
            ps.setString(2, type);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int applicationId = rs.getInt("application_id");
                    return applicationId;
                }
            }throw new SQLException("Creating Application failed, no ID obtained");
        }
    }

    public AccountApplication findById(int id) throws SQLException {
        String sql = "SELECT * FROM account_applications WHERE application_id = ?;";

        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new AccountApplication(rs.getInt("application_id"), rs.getInt("applicant_id"), rs.getInt("approver_id"), rs.getString("account_type"), rs.getString("status"));
                }
                return null;
            }

        }
    }

    public boolean update(AccountApplication application, int approverId) throws SQLException{
        String sql = "UPDATE account_applications SET applicant_id = ?, approver_id = ?, status = ? WHERE application_id = ?";

        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, application.getApplicantId());
            ps.setInt(2, approverId);
            ps.setString(3, application.getStatus());
            ps.setInt(4, application.getApplicationId());
            return ps.executeUpdate() > 0;
        }

    }

    public boolean delete(int applicationId) throws SQLException {
        String sql = "DELETE FROM account_applications WHERE account_id = ?";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            return ps.executeUpdate() > 0;
        }
    }
}
