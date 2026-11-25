package org.example.dto;

import org.example.entity.Transaction;

import java.math.BigDecimal;

public record TransactionResponse(
        Integer transactionId,
        Integer accountId,
        String transactionType,
        BigDecimal amount) {

    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.getTransactionId(),
                transaction.getAccountId(),
                transaction.getTransactionType(),
                BigDecimal.valueOf(transaction.getAmount()));
    }
}
