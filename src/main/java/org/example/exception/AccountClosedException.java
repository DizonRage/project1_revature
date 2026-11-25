package org.example.exception;

public class AccountClosedException extends BankException {
    public AccountClosedException(String message) {
        super(message);
    }
}
