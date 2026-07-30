package com.bank.loan.service.command.impl;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.service.command.LoanActionHandler;
import org.springframework.stereotype.Component;

@Component
public class FreezeActionHandler implements LoanActionHandler<Void> {

    @Override
    public LoanActionType getActionType() {
        return LoanActionType.FREEZE;
    }

    @Override
    public void validate(Loan loan, Void request) {
        if (loan.getStatus() != LoanStatus.ACTIVE && loan.getStatus() != LoanStatus.OVERDUE) {
            throw new IllegalStateException("Only ACTIVE or OVERDUE loans can be frozen. Current status: " + loan.getStatus());
        }
    }

    @Override
    public void execute(Loan loan, Void request) {
        loan.setStatus(LoanStatus.FROZEN);
    }
}
