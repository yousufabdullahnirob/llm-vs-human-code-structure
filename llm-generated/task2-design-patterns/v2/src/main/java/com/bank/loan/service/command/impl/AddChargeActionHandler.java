package com.bank.loan.service.command.impl;

import com.bank.loan.domain.Charge;
import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.dto.ChargeRequest;
import com.bank.loan.service.LoanTransactionProcessor;
import com.bank.loan.service.command.LoanActionHandler;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddChargeActionHandler implements LoanActionHandler<ChargeRequest> {

    @Autowired
    private LoanTransactionProcessor transactionProcessor;

    @Override
    public LoanActionType getActionType() {
        return LoanActionType.ADD_CHARGE;
    }

    @Override
    public void validate(Loan loan, ChargeRequest request) {
        if (loan.getStatus() == LoanStatus.CLOSED || loan.getStatus() == LoanStatus.WRITTEN_OFF) {
            throw new IllegalStateException("Cannot add charge to a CLOSED or WRITTEN_OFF loan. Current status: " + loan.getStatus());
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Charge amount must be positive.");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Charge name is required.");
        }
    }

    @Override
    public void execute(Loan loan, ChargeRequest request) {
        Charge charge = new Charge(request.getName(), request.getAmount(), LocalDate.now(), loan);
        loan.addCharge(charge);

        if (loan.getStatus() == LoanStatus.ACTIVE || loan.getStatus() == LoanStatus.OVERDUE) {
            transactionProcessor.distributeChargesToSchedule(loan);
            transactionProcessor.reapplyTransactions(loan);
        } else {
            transactionProcessor.recalculateLoanSummary(loan);
        }
    }
}
