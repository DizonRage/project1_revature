package org.example.model;

public class AccountApplication {

    private int application_id;
    private int applicant_id;
    private int approver_id;
    private String account_type;
    private String status;

    public AccountApplication() {
    }

    public AccountApplication(int application_id, int applicant_id, int approver_id, String account_type, String status) {
        this.application_id = application_id;
        this.applicant_id = applicant_id;
        this.approver_id = approver_id;
        this.account_type = account_type;
        this.status = status;
    }

    public AccountApplication(int applicant_id, String account_type) {
        this.applicant_id = applicant_id;
        this.account_type = account_type;
    }

    public AccountApplication(int applicant_id, int approver_id, String account_type, String status) {
        this.applicant_id = applicant_id;
        this.approver_id = approver_id;
        this.account_type = account_type;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public int getApprover_id() {
        return approver_id;
    }

    public void setApprover_id(int approver_id) {
        this.approver_id = approver_id;
    }

    public int getApplicant_id() {
        return applicant_id;
    }

    public void setApplicant_id(int applicant_id) {
        this.applicant_id = applicant_id;
    }

    public int getApplication_id() {
        return application_id;
    }

    public void setApplication_id(int application_id) {
        this.application_id = application_id;
    }

    @Override
    public String toString() {
        return "Application ID: " + application_id +
                "\nApplicant ID: " + applicant_id +
                "\nApprover ID: " + approver_id +
                "\nAccount Type: " + account_type +
                "\nApproval Status: " + status + "\n";
    }
}
