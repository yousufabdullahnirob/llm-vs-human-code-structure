package com.bank.loan.service;

import com.bank.loan.domain.Charge;
import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.domain.LoanTransaction;
import com.bank.loan.domain.RepaymentScheduleInstallment;
import com.bank.loan.domain.TransactionType;
import com.bank.loan.service.strategy.RepaymentAllocationStrategy;
import com.bank.loan.service.strategy.RepaymentStrategyRegistry;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanTransactionProcessor {

    @Autowired
    private RepaymentStrategyRegistry strategyRegistry;

    public void distributeChargesToSchedule(Loan loan) {
        if (loan.getInstallments().isEmpty()) return;

        for (RepaymentScheduleInstallment inst : loan.getInstallments()) {
            inst.setChargesDue(BigDecimal.ZERO);
        }

        BigDecimal totalCharges = BigDecimal.ZERO;
        for (Charge charge : loan.getCharges()) {
            totalCharges = totalCharges.add(charge.getAmount());
        }

        if (!loan.getInstallments().isEmpty()) {
            RepaymentScheduleInstallment first = loan.getInstallments().get(0);
            first.setChargesDue(totalCharges);
        }
    }

    public void reapplyTransactions(Loan loan) {
        // Reset all payments
        for (RepaymentScheduleInstallment inst : loan.getInstallments()) {
            inst.resetPayments();
        }

        // Retrieve strategy based on loan's configuration
        RepaymentAllocationStrategy strategy = strategyRegistry.getStrategy(loan.getAllocationStrategy());

        // Distribute each active payment (Repayments & Guarantor Recoveries) sequentially using the strategy
        for (LoanTransaction tx : loan.getTransactions()) {
            if (tx.isReversed()) continue;
            if (tx.getType() != TransactionType.REPAYMENT && tx.getType() != TransactionType.GUARANTOR_RECOVERY) {
                continue;
            }

            strategy.allocate(loan.getInstallments(), tx.getAmount());
        }

        recalculateLoanSummary(loan);

        // Adjust state if fully paid or overdue
        if (loan.getTotalOutstanding().compareTo(BigDecimal.ZERO) <= 0) {
            loan.setStatus(LoanStatus.CLOSED);
            loan.setClosedDate(LocalDate.now());
        } else {
            boolean hasOverdue = loan.getInstallments().stream()
                    .anyMatch(inst -> !inst.isCompleted() && inst.getDueDate().isBefore(LocalDate.now()));
            if (hasOverdue) {
                loan.setStatus(LoanStatus.OVERDUE);
            } else {
                loan.setStatus(LoanStatus.ACTIVE);
            }
            loan.setClosedDate(null);
        }
    }

    public void recalculateLoanSummary(Loan loan) {
        BigDecimal totalPrincipal = loan.getAmount();
        BigDecimal totalInterest = loan.getInstallments().stream()
                .map(RepaymentScheduleInstallment::getInterestDue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCharges = loan.getCharges().stream()
                .map(Charge::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal paidPrincipal = loan.getInstallments().stream()
                .map(RepaymentScheduleInstallment::getPrincipalPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal paidInterest = loan.getInstallments().stream()
                .map(RepaymentScheduleInstallment::getInterestPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal paidCharges = loan.getInstallments().stream()
                .map(RepaymentScheduleInstallment::getChargesPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        loan.setTotalPrincipal(totalPrincipal);
        loan.setTotalInterest(totalInterest);
        loan.setTotalCharges(totalCharges);
        loan.setPaidPrincipal(paidPrincipal);
        loan.setPaidInterest(paidInterest);
        loan.setPaidCharges(paidCharges);
    }
}
