package com.bank.loan.service.command.impl;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanOfficer;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.dto.LoanApplicationRequest;
import com.bank.loan.repository.LoanOfficerRepository;
import com.bank.loan.service.command.LoanActionHandler;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubmitActionHandler implements LoanActionHandler<LoanApplicationRequest> {

    @Autowired
    private LoanOfficerRepository loanOfficerRepository;

    @Override
    public LoanActionType getActionType() {
        return LoanActionType.SUBMIT;
    }

    @Override
    public void validate(Loan loan, LoanApplicationRequest request) {
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive.");
        }
        if (request.getInterestRate() == null || request.getInterestRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        }
        if (request.getTerm() <= 0) {
            throw new IllegalArgumentException("Term must be greater than zero.");
        }
    }

    @Override
    public void execute(Loan loan, LoanApplicationRequest request) {
        LoanOfficer officer = null;
        if (request.getLoanOfficerId() != null) {
            officer = loanOfficerRepository.findById(request.getLoanOfficerId())
                    .orElseThrow(() -> new IllegalArgumentException("Loan officer not found with ID: " + request.getLoanOfficerId()));
        }

        loan.setClientOrGroupName(request.getClientOrGroupName());
        loan.setGroup(request.isGroup());
        loan.setLoanProduct(request.getLoanProduct());
        loan.setAmount(request.getAmount());
        loan.setInterestRate(request.getInterestRate());
        loan.setTerm(request.getTerm());
        loan.setRepaymentFrequency(request.getRepaymentFrequency());
        loan.setLoanOfficer(officer);
        loan.setCreationDate(LocalDate.now());
        loan.setStatus(LoanStatus.SUBMITTED);
        loan.setTotalPrincipal(request.getAmount());

        // Select allocation strategy based on product name
        if ("Personal Consumer Loan".equalsIgnoreCase(request.getLoanProduct())) {
            loan.setAllocationStrategy("PrincipalInterestCharges");
        } else if ("Home Improvement Loan".equalsIgnoreCase(request.getLoanProduct())) {
            loan.setAllocationStrategy("InterestPrincipalCharges");
        } else {
            loan.setAllocationStrategy("ChargesInterestPrincipal");
        }
    }
}
