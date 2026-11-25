package org.example.dto;

import org.example.entity.AccountOwner;

public record OwnerResponse(Integer accountId, Integer userId) {

    public static OwnerResponse from(AccountOwner owner) {
        return new OwnerResponse(owner.getAccountId(), owner.getUserId());
    }
}
