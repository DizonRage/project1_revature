package org.example.exception;

public class AlreadyOwnerException extends BankException {
    public AlreadyOwnerException(String message) {
        super(message);
    }
}
