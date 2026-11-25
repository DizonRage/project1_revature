package org.example.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** newBalance is the source account's balance; the destination balance is never exposed. */
public record TransferResponse(
        Integer fromAccountId,
        Integer toAccountId,
        BigDecimal amount,
        BigDecimal newBalance,
        LocalDateTime timestamp) {
}
