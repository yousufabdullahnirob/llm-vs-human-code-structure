package com.bank.loan.service;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.RepaymentScheduleInstallment;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LoanScheduleGenerator {

    public List<RepaymentScheduleInstallment> generateSchedule(Loan loan, LocalDate disbursementDate) {
        List<RepaymentScheduleInstallment> installments = new ArrayList<>();
        BigDecimal principal = loan.getAmount();
        BigDecimal annualRate = loan.getInterestRate();
        int term = loan.getTerm();
        String frequency = loan.getRepaymentFrequency();

        if (term <= 0) {
            return installments;
        }

        // Calculate period rate 'r'
        BigDecimal r;
        if ("WEEKLY".equalsIgnoreCase(frequency)) {
            r = annualRate.divide(BigDecimal.valueOf(52 * 100), 10, RoundingMode.HALF_UP);
        } else { // Default to MONTHLY
            r = annualRate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);
        }

        BigDecimal installmentAmount;
        if (r.compareTo(BigDecimal.ZERO) == 0) {
            installmentAmount = principal.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
        } else {
            // PMT formula: P * (r * (1+r)^n) / ((1+r)^n - 1)
            BigDecimal onePlusR = BigDecimal.ONE.add(r);
            BigDecimal onePlusRPowN = onePlusR.pow(term);
            BigDecimal numerator = r.multiply(onePlusRPowN);
            BigDecimal denominator = onePlusRPowN.subtract(BigDecimal.ONE);
            
            BigDecimal factor = numerator.divide(denominator, 10, RoundingMode.HALF_UP);
            installmentAmount = principal.multiply(factor).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal remainingPrincipal = principal.setScale(2, RoundingMode.HALF_UP);
        LocalDate currentDate = disbursementDate;

        for (int i = 1; i <= term; i++) {
            // Calculate due date
            if ("WEEKLY".equalsIgnoreCase(frequency)) {
                currentDate = currentDate.plusWeeks(1);
            } else { // MONTHLY
                currentDate = currentDate.plusMonths(1);
            }

            BigDecimal interestDue = remainingPrincipal.multiply(r).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalDue = installmentAmount.subtract(interestDue).setScale(2, RoundingMode.HALF_UP);

            // Adjustment for last installment to prevent rounding mismatch
            if (i == term || principalDue.compareTo(remainingPrincipal) > 0) {
                principalDue = remainingPrincipal;
                installmentAmount = principalDue.add(interestDue);
            }

            remainingPrincipal = remainingPrincipal.subtract(principalDue);

            RepaymentScheduleInstallment installment = new RepaymentScheduleInstallment(
                i,
                currentDate,
                principalDue,
                interestDue,
                loan
            );
            
            installments.add(installment);
        }

        return installments;
    }
}
