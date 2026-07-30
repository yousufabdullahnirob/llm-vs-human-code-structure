package com.bank.loan.service.command.impl;

import com.bank.loan.domain.Guarantor;
import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.domain.LoanTransaction;
import com.bank.loan.domain.TransactionType;
import com.bank.loan.dto.GuarantorRecoveryRequest;
import com.bank.loan.service.LoanTransactionProcessor;
import com.bank.loan.service.command.LoanActionHandler;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecoverGuarantorActionHandler implements LoanActionHandler<GuarantorRecoveryRequest> {

    @Autowired
    private LoanTransactionProcessor transactionProcessor;

    @Override
    public LoanActionType getActionType() {
        return LoanActionType.RECOVER_GUARANTOR;
    }

    @Override
    public void validate(Loan loan, GuarantorRecoveryRequest request) {
        if (loan.getStatus() != LoanStatus.ACTIVE && loan.getStatus() != LoanStatus.OVERDUE) {
            throw new IllegalStateException("Guarantor recovery only allowed for ACTIVE or OVERDUE loans. Current status: " + loan.getStatus());
        }
        if (request.getGuarantorId() == null) {
            throw new IllegalArgumentException("Guarantor ID is required.");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Recovery amount must be positive.");
        }
        if (request.getPaymentDate() == null) {
            throw new IllegalArgumentException("Payment date is required.");
        }

        boolean hasGuarantor = loan.getGuarantors().stream()
                .anyMatch(g -> g.getId().equals(request.getGuarantorId()));
        if (!hasGuarantor) {
            throw new IllegalArgumentException("Guarantor not found on this loan.");
        }
    }

    @Override
    public void execute(Loan loan, GuarantorRecoveryRequest request) {
        Guarantor guarantor = loan.getGuarantors().stream()
                .filter(g -> g.getId().equals(request.getGuarantorId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Guarantor not found on this loan."));

        LoanTransaction tx = new LoanTransaction(request.getAmount(), TransactionType.GUARANTOR_RECOVERY, request.getPaymentDate(), loan);
        tx.setGuarantor(guarantor);
        loan.addTransaction(tx);

        transactionProcessor.reapplyTransactions(loan);
    }
}
