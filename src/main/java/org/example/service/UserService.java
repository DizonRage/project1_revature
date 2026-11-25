package org.example.service;

import org.example.repository.UserDAO;
import org.example.model.User;

import java.util.List;

public class UserService {

    UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public int addUser(User user) {
        return userDAO.create(user);
    }

    public User login(String user, String pass) {
        return userDAO.findByLogin(user, pass);
    }

    public User getUserById(int id) {
        return userDAO.findById(id);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public boolean updateUser(User user, int id) {
        if (userDAO.findById(id) != null) {
            return userDAO.update(user, id);
        }
        return false;
    }

    public boolean removeUser(int id) {
        if(userDAO.findById(id) != null){
            return userDAO.delete(id);
        }
        return false;
    }


}
