package com.bank.loan.service.command.impl;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanOfficer;
import com.bank.loan.dto.ReassignOfficerRequest;
import com.bank.loan.repository.LoanOfficerRepository;
import com.bank.loan.service.command.LoanActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReassignOfficerActionHandler implements LoanActionHandler<ReassignOfficerRequest> {

    @Autowired
    private LoanOfficerRepository loanOfficerRepository;

    @Override
    public LoanActionType getActionType() {
        return LoanActionType.REASSIGN_OFFICER;
    }

    @Override
    public void validate(Loan loan, ReassignOfficerRequest request) {
        if (request.getOfficerId() == null) {
            throw new IllegalArgumentException("Officer ID is required.");
        }
    }

    @Override
    public void execute(Loan loan, ReassignOfficerRequest request) {
        LoanOfficer officer = loanOfficerRepository.findById(request.getOfficerId())
                .orElseThrow(() -> new IllegalArgumentException("Loan officer not found with ID: " + request.getOfficerId()));
        loan.setLoanOfficer(officer);
    }
}
