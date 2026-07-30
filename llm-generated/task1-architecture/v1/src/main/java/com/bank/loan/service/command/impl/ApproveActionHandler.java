package com.bank.loan.service.command.impl;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.service.command.LoanActionHandler;
import org.springframework.stereotype.Component;

@Component
public class ApproveActionHandler implements LoanActionHandler<Void> {

    @Override
    public LoanActionType getActionType() {
        return LoanActionType.APPROVE;
    }

    @Override
    public void validate(Loan loan, Void request) {
        if (loan.getStatus() != LoanStatus.SUBMITTED) {
            throw new IllegalStateException("Only SUBMITTED loans can be approved. Current status: " + loan.getStatus());
        }
    }

    @Override
    public void execute(Loan loan, Void request) {
        loan.setStatus(LoanStatus.APPROVED);
    }
}
