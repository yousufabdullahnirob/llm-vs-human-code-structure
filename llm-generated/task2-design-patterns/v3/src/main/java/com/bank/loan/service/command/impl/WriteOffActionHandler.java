package com.bank.loan.service.command.impl;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.service.command.LoanActionHandler;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class WriteOffActionHandler implements LoanActionHandler<Void> {

    @Override
    public LoanActionType getActionType() {
        return LoanActionType.WRITE_OFF;
    }

    @Override
    public void validate(Loan loan, Void request) {
        if (loan.getStatus() != LoanStatus.ACTIVE && loan.getStatus() != LoanStatus.OVERDUE) {
            throw new IllegalStateException("Only ACTIVE or OVERDUE loans can be written off. Current status: " + loan.getStatus());
        }
    }

    @Override
    public void execute(Loan loan, Void request) {
        loan.setStatus(LoanStatus.WRITTEN_OFF);
        loan.setWrittenOffDate(LocalDate.now());
    }
}
