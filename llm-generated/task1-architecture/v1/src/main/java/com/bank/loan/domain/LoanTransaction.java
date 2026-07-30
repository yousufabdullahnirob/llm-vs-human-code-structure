package com.bank.loan.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loan_transactions")
public class LoanTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDate transactionDate;
    private boolean reversed = false;

    @ManyToOne
    @JoinColumn(name = "guarantor_id")
    private Guarantor guarantor; // optional, for GUARANTOR_RECOVERY

    @ManyToOne
    @JoinColumn(name = "loan_id")
    @JsonIgnore
    private Loan loan;

    public LoanTransaction() {}

    public LoanTransaction(BigDecimal amount, TransactionType type, LocalDate transactionDate, Loan loan) {
        this.amount = amount;
        this.type = type;
        this.transactionDate = transactionDate;
        this.loan = loan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public Guarantor getGuarantor() {
        return guarantor;
    }

    public void setGuarantor(Guarantor guarantor) {
        this.guarantor = guarantor;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
