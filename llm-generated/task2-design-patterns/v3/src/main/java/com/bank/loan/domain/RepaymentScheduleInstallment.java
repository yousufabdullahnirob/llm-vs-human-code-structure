package com.bank.loan.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "repayment_schedule_installments")
public class RepaymentScheduleInstallment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int installmentNumber;
    private LocalDate dueDate;

    private BigDecimal principalDue = BigDecimal.ZERO;
    private BigDecimal interestDue = BigDecimal.ZERO;
    private BigDecimal chargesDue = BigDecimal.ZERO;

    private BigDecimal principalPaid = BigDecimal.ZERO;
    private BigDecimal interestPaid = BigDecimal.ZERO;
    private BigDecimal chargesPaid = BigDecimal.ZERO;

    private boolean completed = false;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    @JsonIgnore
    private Loan loan;

    public RepaymentScheduleInstallment() {}

    public RepaymentScheduleInstallment(int installmentNumber, LocalDate dueDate, BigDecimal principalDue, BigDecimal interestDue, Loan loan) {
        this.installmentNumber = installmentNumber;
        this.dueDate = dueDate;
        this.principalDue = principalDue;
        this.interestDue = interestDue;
        this.loan = loan;
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

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    // Helper business calculations
    public BigDecimal getPrincipalOutstanding() {
        return principalDue.subtract(principalPaid);
    }

    public BigDecimal getInterestOutstanding() {
        return interestDue.subtract(interestPaid);
    }

    public BigDecimal getChargesOutstanding() {
        return chargesDue.subtract(chargesPaid);
    }

    public BigDecimal getTotalDue() {
        return principalDue.add(interestDue).add(chargesDue);
    }

    public BigDecimal getTotalPaid() {
        return principalPaid.add(interestPaid).add(chargesPaid);
    }

    public BigDecimal getTotalOutstanding() {
        return getPrincipalOutstanding().add(getInterestOutstanding()).add(getChargesOutstanding());
    }

    public void resetPayments() {
        this.principalPaid = BigDecimal.ZERO;
        this.interestPaid = BigDecimal.ZERO;
        this.chargesPaid = BigDecimal.ZERO;
        this.completed = false;
    }
}
