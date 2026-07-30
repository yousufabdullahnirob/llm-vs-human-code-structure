package com.bank.loan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_action_audits")
public class LoanActionAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long loanId;

    @Enumerated(EnumType.STRING)
    private LoanActionType actionType;

    private LocalDateTime timestamp;
    private String status; // "SUCCESS", "FAILED"

    @Column(length = 2000)
    private String errorMsg;

    @Column(length = 2000)
    private String details;

    public LoanActionAudit() {}

    public LoanActionAudit(Long loanId, LoanActionType actionType, LocalDateTime timestamp, String status, String errorMsg, String details) {
        this.loanId = loanId;
        this.actionType = actionType;
        this.timestamp = timestamp;
        this.status = status;
        this.errorMsg = errorMsg;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public LoanActionType getActionType() {
        return actionType;
    }

    public void setActionType(LoanActionType actionType) {
        this.actionType = actionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
