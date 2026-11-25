package org.example.entity;

public class AccountApplication {

    private int applicationId;
    private int applicantId;
    private int approverId;
    private String accountType;
    private String status;

    public AccountApplication() {
    }

    public AccountApplication(int applicationId, int applicantId, int approverId, String accountType, String status) {
        this.applicationId = applicationId;
        this.applicantId = applicantId;
        this.approverId = approverId;
        this.accountType = accountType;
        this.status = status;
    }

    public AccountApplication(int applicantId, String accountType) {
        this.applicantId = applicantId;
        this.accountType = accountType;
    }

    public AccountApplication(int applicantId, int approverId, String accountType, String status) {
        this.applicantId = applicantId;
        this.approverId = approverId;
        this.accountType = accountType;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getApproverId() {
        return approverId;
    }

    public void setApproverId(int approverId) {
        this.approverId = approverId;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    @Override
    public String toString() {
        return "Application ID: " + applicationId +
                "\nApplicant ID: " + applicantId +
                "\nApprover ID: " + approverId +
                "\nAccount Type: " + accountType +
                "\nApproval Status: " + status + "\n";
    }
}
