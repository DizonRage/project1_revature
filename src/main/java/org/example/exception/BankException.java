package org.example.exception;

/** Base class for all unchecked exceptions thrown by the banking application. */
public class BankException extends RuntimeException {

    public BankException(String message) {
        super(message);
    }

    public BankException(String message, Throwable cause) {
        super(message, cause);
    }
}
