package com.bank.loan.service.command.impl;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.service.command.LoanActionHandler;
import org.springframework.stereotype.Component;

@Component
public class RejectActionHandler implements LoanActionHandler<Void> {

    @Override
    public LoanActionType getActionType() {
        return LoanActionType.REJECT;
    }

    @Override
    public void validate(Loan loan, Void request) {
        if (loan.getStatus() != LoanStatus.SUBMITTED) {
            throw new IllegalStateException("Only SUBMITTED loans can be rejected. Current status: " + loan.getStatus());
        }
    }

    @Override
    public void execute(Loan loan, Void request) {
        loan.setStatus(LoanStatus.REJECTED);
    }
}
