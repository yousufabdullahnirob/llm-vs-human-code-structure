package com.bank.loan.service;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.dto.response.ChargeResponse;
import com.bank.loan.dto.response.GuarantorResponse;
import com.bank.loan.dto.response.InstallmentResponse;
import com.bank.loan.dto.response.LoanResponse;
import com.bank.loan.dto.response.TransactionResponse;
import com.bank.loan.repository.LoanRepository;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LoanReadService {

    @Autowired
    private LoanRepository loanRepository;

    public LoanResponse getLoanDetails(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found with ID: " + loanId));
        return mapToResponse(loan);
    }

    public Page<LoanResponse> getFilteredLoans(LoanStatus status, String clientName, Long officerId, Pageable pageable) {
        Page<Loan> loans = loanRepository.findFilteredLoans(status, clientName, officerId, pageable);
        return loans.map(this::mapToResponse);
    }

    public LoanResponse mapToResponse(Loan loan) {
        LoanResponse response = new LoanResponse();
        response.setId(loan.getId());
        response.setClientOrGroupName(loan.getClientOrGroupName());
        response.setGroup(loan.isGroup());
        response.setLoanProduct(loan.getLoanProduct());
        response.setAmount(loan.getAmount());
        response.setInterestRate(loan.getInterestRate());
        response.setTerm(loan.getTerm());
        response.setRepaymentFrequency(loan.getRepaymentFrequency());
        response.setStatus(loan.getStatus().name());
        response.setLoanOfficerName(loan.getLoanOfficer() != null ? loan.getLoanOfficer().getName() : null);
        response.setAllocationStrategy(loan.getAllocationStrategy());
        response.setCreationDate(loan.getCreationDate());
        response.setDisbursementDate(loan.getDisbursementDate());
        response.setClosedDate(loan.getClosedDate());
        response.setWrittenOffDate(loan.getWrittenOffDate());

        // Summaries
        response.setTotalPrincipal(loan.getTotalPrincipal());
        response.setTotalInterest(loan.getTotalInterest());
        response.setTotalCharges(loan.getTotalCharges());
        response.setPaidPrincipal(loan.getPaidPrincipal());
        response.setPaidInterest(loan.getPaidInterest());
        response.setPaidCharges(loan.getPaidCharges());
        response.setTotalDue(loan.getTotalDue());
        response.setTotalPaid(loan.getTotalPaid());
        response.setTotalOutstanding(loan.getTotalOutstanding());

        // Installments
        response.setInstallments(loan.getInstallments().stream().map(inst -> new InstallmentResponse(
                inst.getId(),
                inst.getInstallmentNumber(),
                inst.getDueDate(),
                inst.getPrincipalDue(),
                inst.getInterestDue(),
                inst.getChargesDue(),
                inst.getPrincipalPaid(),
                inst.getInterestPaid(),
                inst.getChargesPaid(),
                inst.isCompleted(),
                inst.getTotalDue(),
                inst.getTotalPaid(),
                inst.getTotalOutstanding()
        )).collect(Collectors.toList()));

        // Transactions
        response.setTransactions(loan.getTransactions().stream().map(tx -> new TransactionResponse(
                tx.getId(),
                tx.getAmount(),
                tx.getType().name(),
                tx.getTransactionDate(),
                tx.isReversed(),
                tx.getGuarantor() != null ? tx.getGuarantor().getName() : null
        )).collect(Collectors.toList()));

        // Guarantors
        response.setGuarantors(loan.getGuarantors().stream().map(g -> new GuarantorResponse(
                g.getId(),
                g.getName(),
                g.getRelationship(),
                g.getAmountGuaranteed()
        )).collect(Collectors.toList()));

        // Charges
        response.setCharges(loan.getCharges().stream().map(c -> new ChargeResponse(
                c.getId(),
                c.getName(),
                c.getAmount(),
                c.getDateApplied()
        )).collect(Collectors.toList()));

        return response;
    }
}
