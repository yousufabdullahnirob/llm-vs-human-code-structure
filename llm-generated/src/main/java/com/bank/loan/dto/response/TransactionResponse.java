package com.bank.loan.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private String type;
    private LocalDate transactionDate;
    private boolean reversed;
    private String guarantorName;

    public TransactionResponse() {}

    public TransactionResponse(Long id, BigDecimal amount, String type, LocalDate transactionDate, boolean reversed, String guarantorName) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.transactionDate = transactionDate;
        this.reversed = reversed;
        this.guarantorName = guarantorName;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public String getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(String guarantorName) {
        this.guarantorName = guarantorName;
    }
}
