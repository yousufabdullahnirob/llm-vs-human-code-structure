package com.bank.loan.dto;

import java.math.BigDecimal;

public class GuarantorRequest {
    private String name;
    private String relationship;
    private BigDecimal amountGuaranteed;

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
}
