package org.example.dto;

import org.example.entity.AccountApplication;

public record ApplicationResponse(
        Integer applicationId,
        Integer applicantId,
        Integer approverId,
        String accountType,
        String status) {

    public static ApplicationResponse from(AccountApplication application) {
        // The entity uses an int, so 0 means no approver yet.
        Integer approverId = application.getApproverId() == 0 ? null : application.getApproverId();
        return new ApplicationResponse(
                application.getApplicationId(),
                application.getApplicantId(),
                approverId,
                application.getAccountType(),
                application.getStatus());
    }
}
