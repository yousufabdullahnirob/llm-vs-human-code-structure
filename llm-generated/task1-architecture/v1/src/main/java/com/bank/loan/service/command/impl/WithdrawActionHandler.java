package com.bank.loan.service.command;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import org.springframework.stereotype.Component;

@Component
public class WithdrawActionHandler implements LoanActionHandler<Void> {

    @Override
    public LoanActionType getActionType() {
        return LoanActionType.WITHDRAW;
    }

    @Override
    public void validate(Loan loan, Void request) {
        if (loan.getStatus() != LoanStatus.SUBMITTED && loan.getStatus() != LoanStatus.APPROVED) {
            throw new IllegalStateException("Only SUBMITTED or APPROVED loans can be withdrawn. Current status: " + loan.getStatus());
        }
    }

    @Override
    public void execute(Loan loan, Void request) {
        loan.setStatus(LoanStatus.WITHDRAWN);
    }
}
