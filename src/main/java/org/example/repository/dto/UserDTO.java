package org.example.repository.dto;

public class UserDTO {
    private int user_id;
    private String username;
    private String pass;
    private String first_name;
    private String last_name;
    private String email;
    private String role;

    public UserDTO() {
    }

    public UserDTO(int user_id, String username, String pass, String first_name, String last_name, String email, String role) {
        this.user_id = user_id;
        this.username = username;
        this.pass = pass;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.role = role;
    }

    public UserDTO(String username, String pass, String first_name, String last_name, String email, String role) {
        this.username = username;
        this.pass = pass;
        this.first_name = first_name;
        this.last_name = last_name;
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

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
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

    public int getId() {
        return user_id;
    }

    public void setId(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return  "User ID: " + user_id +
                "\nUsername: " + username +
                "\nPassword: " + pass +
                "\nFirst Name: " + first_name +
                "\nLast Name: " + last_name +
                "\nEmail: " + email +
                "\nRole:" + role + "\n";
    }

    public String toStringCustomer(){
        return "First Name: " + first_name +
                "\nLast Name: " + last_name +
                "\nEmail: " + email + "\n";
    }
}
