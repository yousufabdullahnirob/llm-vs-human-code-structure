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
@Table(name = "loan_charges")
public class Charge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal amount;
    private LocalDate dateApplied;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    @JsonIgnore
    private Loan loan;

    public Charge() {}

    public Charge(String name, BigDecimal amount, LocalDate dateApplied, Loan loan) {
        this.name = name;
        this.amount = amount;
        this.dateApplied = dateApplied;
        this.loan = loan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(LocalDate dateApplied) {
        this.dateApplied = dateApplied;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
