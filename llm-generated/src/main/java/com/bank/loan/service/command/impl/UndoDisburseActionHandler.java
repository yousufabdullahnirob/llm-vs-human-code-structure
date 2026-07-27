package com.bank.loan.service.command.impl;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.domain.TransactionType;
import com.bank.loan.service.LoanTransactionProcessor;
import com.bank.loan.service.command.LoanActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UndoDisburseActionHandler implements LoanActionHandler<Void> {

    @Autowired
    private LoanTransactionProcessor transactionProcessor;

    @Override
    public LoanActionType getActionType() {
        return LoanActionType.UNDO_DISBURSE;
    }

    @Override
    public void validate(Loan loan, Void request) {
        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE loans can undo disbursement. Current status: " + loan.getStatus());
        }

        // Check if any payment transactions exist (excluding disbursement)
        boolean hasPayments = loan.getTransactions().stream()
                .anyMatch(t -> !t.isReversed() && t.getType() != TransactionType.DISBURSEMENT);
        if (hasPayments) {
            throw new IllegalStateException("Cannot undo disbursement because payments have already been made on this loan.");
        }
    }

    @Override
    public void execute(Loan loan, Void request) {
        // Remove schedule
        loan.getInstallments().clear();

        // Reverse disbursement transaction
        loan.getTransactions().forEach(t -> {
            if (t.getType() == TransactionType.DISBURSEMENT) {
                t.setReversed(true);
            }
        });

        loan.setStatus(LoanStatus.APPROVED);
        loan.setDisbursementDate(null);

        // Reset summary
        transactionProcessor.recalculateLoanSummary(loan);
    }
}
