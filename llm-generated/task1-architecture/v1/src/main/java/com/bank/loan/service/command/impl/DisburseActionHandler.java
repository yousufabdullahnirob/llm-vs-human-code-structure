package com.bank.loan.service.command.impl;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.domain.LoanTransaction;
import com.bank.loan.domain.RepaymentScheduleInstallment;
import com.bank.loan.domain.TransactionType;
import com.bank.loan.dto.LoanDisbursementRequest;
import com.bank.loan.service.LoanScheduleGenerator;
import com.bank.loan.service.LoanTransactionProcessor;
import com.bank.loan.service.command.LoanActionHandler;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DisburseActionHandler implements LoanActionHandler<LoanDisbursementRequest> {

    @Autowired
    private LoanScheduleGenerator scheduleGenerator;

    @Autowired
    private LoanTransactionProcessor transactionProcessor;

    @Override
    public LoanActionType getActionType() {
        return LoanActionType.DISBURSE;
    }

    @Override
    public void validate(Loan loan, LoanDisbursementRequest request) {
        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new IllegalStateException("Only APPROVED loans can be disbursed. Current status: " + loan.getStatus());
        }
        if (request.getDisbursementDate() == null) {
            throw new IllegalArgumentException("Disbursement date is required.");
        }
    }

    @Override
    public void execute(Loan loan, LoanDisbursementRequest request) {
        LocalDate disbursementDate = request.getDisbursementDate();
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setDisbursementDate(disbursementDate);

        // Generate repayment schedule
        List<RepaymentScheduleInstallment> installments = scheduleGenerator.generateSchedule(loan, disbursementDate);
        loan.getInstallments().clear();
        for (RepaymentScheduleInstallment installment : installments) {
            loan.addInstallment(installment);
        }

        // Add charges applied before disbursement to the schedule
        transactionProcessor.distributeChargesToSchedule(loan);

        // Record disbursement transaction
        LoanTransaction tx = new LoanTransaction(loan.getAmount(), TransactionType.DISBURSEMENT, disbursementDate, loan);
        loan.addTransaction(tx);

        // Calculate summary
        transactionProcessor.recalculateLoanSummary(loan);
    }
}
