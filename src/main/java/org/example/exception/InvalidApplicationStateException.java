package org.example.exception;

public class InvalidApplicationStateException extends BankException {
    public InvalidApplicationStateException(String message) {
        super(message);
    }
}
