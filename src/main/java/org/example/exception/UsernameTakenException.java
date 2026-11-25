package org.example.exception;

public class UsernameTakenException extends BankException {
    public UsernameTakenException(String message) {
        super(message);
    }
}
