package org.example.service;

import org.example.repository.UserDAO;
import org.example.entity.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public int addUser(User user) throws SQLException {
        return userDAO.create(user);
    }

    public User login(String user, String pass)throws SQLException {
        return userDAO.findByLogin(user, pass);
    }

    public User getUserById(int id) throws SQLException {
        return userDAO.findById(id);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    public boolean updateUser(User user, int id) throws SQLException {
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
