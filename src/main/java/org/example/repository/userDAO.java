package org.example.repository;

import org.example.repository.dto.userDTO;
import org.example.util.DatabaseConnectionFactory;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userDAO {

    public int create(userDTO user) throws SQLException{
        String sql = "INSERT INTO users (username) VALUES (?) RETURNING id;";

        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, user.getUsername());
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int id = rs.getInt(1);
                    user.setId(id);
                }
            }throw new SQLException("Creating user failed, no ID obtained");
        }
    }

    public userDTO findById(int id) throws SQLException{
        String sql = "SELECT id, username, first_name, last_name, email, role FROM users WHERE id = ?;";
        try (Connection conn = DatabaseConnectionFactory.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new userDTO(rs.getInt(id), rs.getString("username"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("role"));
                }
                return null;
            }

        }
    }
}
