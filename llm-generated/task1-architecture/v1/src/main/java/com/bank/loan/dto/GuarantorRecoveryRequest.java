package com.bank.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GuarantorRecoveryRequest {
    private Long guarantorId;
    private BigDecimal amount;
    private LocalDate paymentDate;

    public Long getGuarantorId() {
        return guarantorId;
    }

    public void setGuarantorId(Long guarantorId) {
        this.guarantorId = guarantorId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}
