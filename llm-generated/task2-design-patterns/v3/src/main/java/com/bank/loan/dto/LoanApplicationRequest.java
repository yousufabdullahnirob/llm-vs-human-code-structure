package com.bank.loan.dto;

import java.math.BigDecimal;

public class LoanApplicationRequest {
    private String clientOrGroupName;
    private boolean isGroup;
    private String loanProduct;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private int term;
    private String repaymentFrequency; // "WEEKLY", "MONTHLY"
    private Long loanOfficerId;

    public String getClientOrGroupName() {
        return clientOrGroupName;
    }

    public void setClientOrGroupName(String clientOrGroupName) {
        this.clientOrGroupName = clientOrGroupName;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getLoanProduct() {
        return loanProduct;
    }

    public void setLoanProduct(String loanProduct) {
        this.loanProduct = loanProduct;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getRepaymentFrequency() {
        return repaymentFrequency;
    }

    public void setRepaymentFrequency(String repaymentFrequency) {
        this.repaymentFrequency = repaymentFrequency;
    }

    public Long getLoanOfficerId() {
        return loanOfficerId;
    }

    public void setLoanOfficerId(Long loanOfficerId) {
        this.loanOfficerId = loanOfficerId;
    }
}
