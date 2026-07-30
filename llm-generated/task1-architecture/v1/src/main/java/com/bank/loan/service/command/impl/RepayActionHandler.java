package com.bank.loan.service.command.impl;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.domain.LoanTransaction;
import com.bank.loan.domain.TransactionType;
import com.bank.loan.dto.RepaymentRequest;
import com.bank.loan.service.LoanTransactionProcessor;
import com.bank.loan.service.command.LoanActionHandler;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepayActionHandler implements LoanActionHandler<RepaymentRequest> {

    @Autowired
    private LoanTransactionProcessor transactionProcessor;

    @Override
    public LoanActionType getActionType() {
        return LoanActionType.REPAY;
    }

    @Override
    public void validate(Loan loan, RepaymentRequest request) {
        if (loan.getStatus() != LoanStatus.ACTIVE && loan.getStatus() != LoanStatus.OVERDUE) {
            throw new IllegalStateException("Repayment only allowed for ACTIVE or OVERDUE loans. Current status: " + loan.getStatus());
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Repayment amount must be positive.");
        }
        if (request.getPaymentDate() == null) {
            throw new IllegalArgumentException("Repayment date is required.");
        }
    }

    @Override
    public void execute(Loan loan, RepaymentRequest request) {
        LoanTransaction tx = new LoanTransaction(request.getAmount(), TransactionType.REPAYMENT, request.getPaymentDate(), loan);
        loan.addTransaction(tx);

        transactionProcessor.reapplyTransactions(loan);
    }
}
