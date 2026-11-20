package org.example.service;

import org.example.repository.UserDAO;
import org.example.repository.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public int addUser(UserDTO user) throws SQLException {
        return userDAO.create(user);
    }

    public UserDTO login(String user, String pass)throws SQLException {
        return userDAO.findByLogin(user, pass);
    }

    public UserDTO getUserById(int id) throws SQLException {
        return userDAO.findById(id);
    }

    public List<UserDTO> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    public boolean updateUser(UserDTO user, int id) throws SQLException {
        if (userDAO.findById(id) != null) {
            return userDAO.update(user, id);
        }
        return false;
    }

    public boolean removeUser(int id) throws SQLException {
        if(userDAO.findById(id) != null){
            return userDAO.delete(id);
        }
        return false;
    }


}
