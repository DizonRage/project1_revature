package org.example.exception;

public class InvalidCredentialsException extends BankException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
