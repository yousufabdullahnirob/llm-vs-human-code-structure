package com.bank.loan.dto.response;

import java.math.BigDecimal;

public class GuarantorResponse {
    private Long id;
    private String name;
    private String relationship;
    private BigDecimal amountGuaranteed;

    public GuarantorResponse() {}

    public GuarantorResponse(Long id, String name, String relationship, BigDecimal amountGuaranteed) {
        this.id = id;
        this.name = name;
        this.relationship = relationship;
        this.amountGuaranteed = amountGuaranteed;
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
}
