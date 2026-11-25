package org.example.exception;

import java.sql.SQLException;

/**
 * Wraps a SQLException raised in the repository layer. The message includes database
 * details, so it should not be returned to API clients.
 */
public class DataAccessException extends BankException {

    public DataAccessException(String message, SQLException cause) {
        super(message + ": " + cause.getMessage(), cause);
    }
}
