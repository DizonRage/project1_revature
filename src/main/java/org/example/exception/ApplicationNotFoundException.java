package org.example.exception;

public class ApplicationNotFoundException extends BankException {
    public ApplicationNotFoundException(String message) {
        super(message);
    }
}
