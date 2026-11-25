package org.example.service;

import org.example.repository.UserDAO;
import org.example.entity.User;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {

    UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    // Create a CUSTOMER with a hashed password; UsernameTakenException if the username exists. @Transactional: yes (check + insert, plus a unique constraint).
    public User register(String username, String password, String firstName, String lastName, String email) {
        throw new UnsupportedOperationException("TODO: implement");
    }

    // Return the user whose password matches; InvalidCredentialsException (same message for unknown user or wrong password). @Transactional: no.
    public User authenticate(String username, String password) {
        throw new UnsupportedOperationException("TODO: implement");
    }

    // Return all users ordered by id (admin only, checked in the controller); no exceptions. @Transactional: no.
    public List<User> listUsers() {
        throw new UnsupportedOperationException("TODO: implement");
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
