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

@Entity
@Table(name = "guarantors")
public class Guarantor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String relationship;
    private BigDecimal amountGuaranteed;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    @JsonIgnore
    private Loan loan;

    public Guarantor() {}

    public Guarantor(String name, String relationship, BigDecimal amountGuaranteed, Loan loan) {
        this.name = name;
        this.relationship = relationship;
        this.amountGuaranteed = amountGuaranteed;
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

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public BigDecimal getAmountGuaranteed() {
        return amountGuaranteed;
    }

    public void setAmountGuaranteed(BigDecimal amountGuaranteed) {
        this.amountGuaranteed = amountGuaranteed;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
