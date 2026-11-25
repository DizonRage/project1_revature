package org.example.dto;

import org.example.entity.Account;

import java.math.BigDecimal;

public record AccountResponse(
        Integer accountId,
        String accountNumber,
        String accountType,
        BigDecimal balance,
        String status) {

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus());
    }
}
