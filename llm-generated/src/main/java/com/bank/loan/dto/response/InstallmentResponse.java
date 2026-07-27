package com.bank.loan.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InstallmentResponse {
    private Long id;
    private int installmentNumber;
    private LocalDate dueDate;
    private BigDecimal principalDue;
    private BigDecimal interestDue;
    private BigDecimal chargesDue;
    private BigDecimal principalPaid;
    private BigDecimal interestPaid;
    private BigDecimal chargesPaid;
    private boolean completed;
    private BigDecimal totalDue;
    private BigDecimal totalPaid;
    private BigDecimal totalOutstanding;

    public InstallmentResponse() {}

    public InstallmentResponse(Long id, int installmentNumber, LocalDate dueDate, BigDecimal principalDue,
                               BigDecimal interestDue, BigDecimal chargesDue, BigDecimal principalPaid,
                               BigDecimal interestPaid, BigDecimal chargesPaid, boolean completed,
                               BigDecimal totalDue, BigDecimal totalPaid, BigDecimal totalOutstanding) {
        this.id = id;
        this.installmentNumber = installmentNumber;
        this.dueDate = dueDate;
        this.principalDue = principalDue;
        this.interestDue = interestDue;
        this.chargesDue = chargesDue;
        this.principalPaid = principalPaid;
        this.interestPaid = interestPaid;
        this.chargesPaid = chargesPaid;
        this.completed = completed;
        this.totalDue = totalDue;
        this.totalPaid = totalPaid;
        this.totalOutstanding = totalOutstanding;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(int installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getPrincipalDue() {
        return principalDue;
    }

    public void setPrincipalDue(BigDecimal principalDue) {
        this.principalDue = principalDue;
    }

    public BigDecimal getInterestDue() {
        return interestDue;
    }

    public void setInterestDue(BigDecimal interestDue) {
        this.interestDue = interestDue;
    }

    public BigDecimal getChargesDue() {
        return chargesDue;
    }

    public void setChargesDue(BigDecimal chargesDue) {
        this.chargesDue = chargesDue;
    }

    public BigDecimal getPrincipalPaid() {
        return principalPaid;
    }

    public void setPrincipalPaid(BigDecimal principalPaid) {
        this.principalPaid = principalPaid;
    }

    public BigDecimal getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(BigDecimal interestPaid) {
        this.interestPaid = interestPaid;
    }

    public BigDecimal getChargesPaid() {
        return chargesPaid;
    }

    public void setChargesPaid(BigDecimal chargesPaid) {
        this.chargesPaid = chargesPaid;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public BigDecimal getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(BigDecimal totalDue) {
        this.totalDue = totalDue;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public BigDecimal getTotalOutstanding() {
        return totalOutstanding;
    }

    public void setTotalOutstanding(BigDecimal totalOutstanding) {
        this.totalOutstanding = totalOutstanding;
    }
}
