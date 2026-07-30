package com.bank.loan.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientOrGroupName;
    private boolean isGroup;
    private String loanProduct;
    private BigDecimal amount;
    private BigDecimal interestRate; // Annual rate in percentage (e.g. 12.00 for 12%)
    private int term; // Number of installments
    private String repaymentFrequency; // "WEEKLY" or "MONTHLY"
    private String allocationStrategy = "ChargesInterestPrincipal";

    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.SUBMITTED;

    @ManyToOne
    @JoinColumn(name = "loan_officer_id")
    private LoanOfficer loanOfficer;

    private LocalDate creationDate;
    private LocalDate disbursementDate;
    private LocalDate closedDate;
    private LocalDate writtenOffDate;

    // Loan Summary (cached for performance and querying)
    private BigDecimal totalPrincipal = BigDecimal.ZERO;
    private BigDecimal totalInterest = BigDecimal.ZERO;
    private BigDecimal totalCharges = BigDecimal.ZERO;

    private BigDecimal paidPrincipal = BigDecimal.ZERO;
    private BigDecimal paidInterest = BigDecimal.ZERO;
    private BigDecimal paidCharges = BigDecimal.ZERO;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("installmentNumber ASC")
    private List<RepaymentScheduleInstallment> installments = new ArrayList<>();

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("transactionDate ASC, id ASC")
    private List<LoanTransaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Guarantor> guarantors = new ArrayList<>();

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Charge> charges = new ArrayList<>();

    public Loan() {}

    public Loan(String clientOrGroupName, boolean isGroup, String loanProduct, BigDecimal amount,
                BigDecimal interestRate, int term, String repaymentFrequency, LoanOfficer loanOfficer,
                LocalDate creationDate) {
        this.clientOrGroupName = clientOrGroupName;
        this.isGroup = isGroup;
        this.loanProduct = loanProduct;
        this.amount = amount;
        this.interestRate = interestRate;
        this.term = term;
        this.repaymentFrequency = repaymentFrequency;
        this.loanOfficer = loanOfficer;
        this.creationDate = creationDate;
        this.status = LoanStatus.SUBMITTED;
        this.totalPrincipal = amount;
        
        if ("Personal Consumer Loan".equalsIgnoreCase(loanProduct)) {
            this.allocationStrategy = "PrincipalInterestCharges";
        } else if ("Home Improvement Loan".equalsIgnoreCase(loanProduct)) {
            this.allocationStrategy = "InterestPrincipalCharges";
        } else {
            this.allocationStrategy = "ChargesInterestPrincipal";
        }
    }

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

    public String getAllocationStrategy() {
        return allocationStrategy;
    }

    public void setAllocationStrategy(String allocationStrategy) {
        this.allocationStrategy = allocationStrategy;
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

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public LoanOfficer getLoanOfficer() {
        return loanOfficer;
    }

    public void setLoanOfficer(LoanOfficer loanOfficer) {
        this.loanOfficer = loanOfficer;
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

    public List<RepaymentScheduleInstallment> getInstallments() {
        return installments;
    }

    public void setInstallments(List<RepaymentScheduleInstallment> installments) {
        this.installments = installments;
    }

    public List<LoanTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<LoanTransaction> transactions) {
        this.transactions = transactions;
    }

    public List<Guarantor> getGuarantors() {
        return guarantors;
    }

    public void setGuarantors(List<Guarantor> guarantors) {
        this.guarantors = guarantors;
    }

    public List<Charge> getCharges() {
        return charges;
    }

    public void setCharges(List<Charge> charges) {
        this.charges = charges;
    }

    // Calculated fields
    public BigDecimal getTotalDue() {
        return totalPrincipal.add(totalInterest).add(totalCharges);
    }

    public BigDecimal getTotalPaid() {
        return paidPrincipal.add(paidInterest).add(paidCharges);
    }

    public BigDecimal getTotalOutstanding() {
        return getTotalDue().subtract(getTotalPaid());
    }

    // Helper methods to manage bi-directional relationships
    public void addInstallment(RepaymentScheduleInstallment installment) {
        this.installments.add(installment);
        installment.setLoan(this);
    }

    public void addTransaction(LoanTransaction transaction) {
        this.transactions.add(transaction);
        transaction.setLoan(this);
    }

    public void addGuarantor(Guarantor guarantor) {
        this.guarantors.add(guarantor);
        guarantor.setLoan(this);
    }

    public void addCharge(Charge charge) {
        this.charges.add(charge);
        charge.setLoan(this);
    }
}
