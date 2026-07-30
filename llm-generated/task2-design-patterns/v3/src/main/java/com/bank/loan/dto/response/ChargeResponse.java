package com.bank.loan.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ChargeResponse {
    private Long id;
    private String name;
    private BigDecimal amount;
    private LocalDate dateApplied;

    public ChargeResponse() {}

    public ChargeResponse(Long id, String name, BigDecimal amount, LocalDate dateApplied) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.dateApplied = dateApplied;
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
}
