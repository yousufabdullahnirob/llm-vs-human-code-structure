package com.bank.loan.service.command;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;

public interface LoanActionHandler<T> {
    LoanActionType getActionType();
    void validate(Loan loan, T request);
    void execute(Loan loan, T request);
}
