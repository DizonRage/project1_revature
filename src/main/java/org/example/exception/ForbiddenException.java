package org.example.exception;

public class ForbiddenException extends BankException {
    public ForbiddenException(String message) {
        super(message);
    }
}
