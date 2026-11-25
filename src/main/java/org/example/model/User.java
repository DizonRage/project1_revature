package org.example.model;

public class User {
    private int userId;
    private String username;
    private String pass;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    public User() {
    }

    public User(int userId, String username, String pass, String firstName, String lastName, String email, String role) {
        this.userId = userId;
        this.username = username;
        this.pass = pass;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public User(String username, String pass, String firstName, String lastName, String email, String role) {
        this.username = username;
        this.pass = pass;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return  "User ID: " + userId +
                "\nUsername: " + username +
                "\nPassword: " + pass +
                "\nFirst Name: " + firstName +
                "\nLast Name: " + lastName +
                "\nEmail: " + email +
                "\nRole:" + role + "\n";
    }

    public String toStringCustomer(){
        return "First Name: " + firstName +
                "\nLast Name: " + lastName +
                "\nEmail: " + email + "\n";
    }
}
