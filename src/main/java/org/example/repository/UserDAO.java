package org.example.repository;

import org.example.exception.DataAccessException;
import org.example.model.User;
import org.example.util.DatabaseConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public int create(User user) {
        String sql = "INSERT INTO users (username, pass, first_name, last_name, email, role) VALUES (?, ?, ?, ?, ?, ?) RETURNING user_id;";

        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPass());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getRole());
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int userId = rs.getInt(1);
                    user.setUserId(userId);
                    return userId;
                }
            }throw new SQLException("Creating user failed, no ID obtained");
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create user", e);
        }
    }

    public User findById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?;";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("pass"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("role"));
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find user", e);
        }
    }

    public User findByLogin(String user, String pass) {
        String sql = "SELECT * FROM users WHERE username = ? AND pass = ?;";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, user);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("pass"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("role"));
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find user", e);
        }
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_id, username, pass, first_name, last_name, email, role FROM users ORDER BY user_id";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("pass"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("role")));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to list users", e);
        }
        return list;
    }

    public boolean update(User user, int userId) {
        String sql = "UPDATE users SET username = ?, pass = ?, first_name = ?, last_name = ?, email = ?, role = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPass());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getRole());
            ps.setInt(7, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update user", e);
        }
    }

    public boolean delete(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete user", e);
        }
    }
}
