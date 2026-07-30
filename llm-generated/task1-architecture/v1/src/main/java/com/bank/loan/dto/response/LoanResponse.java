package com.bank.loan.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class LoanResponse {
    private Long id;
    private String clientOrGroupName;
    private boolean isGroup;
    private String loanProduct;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private int term;
    private String repaymentFrequency;
    private String status;
    private String loanOfficerName;
    private String allocationStrategy;
    private LocalDate creationDate;
    private LocalDate disbursementDate;
    private LocalDate closedDate;
    private LocalDate writtenOffDate;

    // Summary fields
    private BigDecimal totalPrincipal;
    private BigDecimal totalInterest;
    private BigDecimal totalCharges;
    private BigDecimal paidPrincipal;
    private BigDecimal paidInterest;
    private BigDecimal paidCharges;
    private BigDecimal totalDue;
    private BigDecimal totalPaid;
    private BigDecimal totalOutstanding;

    // Associated list DTOs
    private List<InstallmentResponse> installments;
    private List<TransactionResponse> transactions;
    private List<GuarantorResponse> guarantors;
    private List<ChargeResponse> charges;

    public LoanResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoanOfficerName() {
        return loanOfficerName;
    }

    public void setLoanOfficerName(String loanOfficerName) {
        this.loanOfficerName = loanOfficerName;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
    }

    public LocalDate getWrittenOffDate() {
        return writtenOffDate;
    }

    public void setWrittenOffDate(LocalDate writtenOffDate) {
        this.writtenOffDate = writtenOffDate;
    }

    public BigDecimal getTotalPrincipal() {
        return totalPrincipal;
    }

    public void setTotalPrincipal(BigDecimal totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }

    public BigDecimal getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(BigDecimal totalCharges) {
        this.totalCharges = totalCharges;
    }

    public BigDecimal getPaidPrincipal() {
        return paidPrincipal;
    }

    public void setPaidPrincipal(BigDecimal paidPrincipal) {
        this.paidPrincipal = paidPrincipal;
    }

    public BigDecimal getPaidInterest() {
        return paidInterest;
    }

    public void setPaidInterest(BigDecimal paidInterest) {
        this.paidInterest = paidInterest;
    }

    public BigDecimal getPaidCharges() {
        return paidCharges;
    }

    public void setPaidCharges(BigDecimal paidCharges) {
        this.paidCharges = paidCharges;
    }

    public BigDecimal getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(BigDecimal totalDue) {
        this.totalDue = totalDue;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public BigDecimal getTotalOutstanding() {
        return totalOutstanding;
    }

    public void setTotalOutstanding(BigDecimal totalOutstanding) {
        this.totalOutstanding = totalOutstanding;
    }

    public List<InstallmentResponse> getInstallments() {
        return installments;
    }

    public void setInstallments(List<InstallmentResponse> installments) {
        this.installments = installments;
    }

    public List<TransactionResponse> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionResponse> transactions) {
        this.transactions = transactions;
    }

    public List<GuarantorResponse> getGuarantors() {
        return guarantors;
    }

    public void setGuarantors(List<GuarantorResponse> guarantors) {
        this.guarantors = guarantors;
    }

    public List<ChargeResponse> getCharges() {
        return charges;
    }

    public void setCharges(List<ChargeResponse> charges) {
        this.charges = charges;
    }

    public String getAllocationStrategy() {
        return allocationStrategy;
    }

    public void setAllocationStrategy(String allocationStrategy) {
        this.allocationStrategy = allocationStrategy;
    }
}
