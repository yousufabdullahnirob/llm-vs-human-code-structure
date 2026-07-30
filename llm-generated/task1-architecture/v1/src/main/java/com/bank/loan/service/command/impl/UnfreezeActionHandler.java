package com.bank.loan.service.command.impl;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.service.command.LoanActionHandler;
import org.springframework.stereotype.Component;

@Component
public class UnfreezeActionHandler implements LoanActionHandler<Void> {

    @Override
    public LoanActionType getActionType() {
        return LoanActionType.UNFREEZE;
    }

    @Override
    public void validate(Loan loan, Void request) {
        if (loan.getStatus() != LoanStatus.FROZEN) {
            throw new IllegalStateException("Only FROZEN loans can be unfrozen. Current status: " + loan.getStatus());
        }
    }

    @Override
    public void execute(Loan loan, Void request) {
        loan.setStatus(LoanStatus.ACTIVE);
    }
}
