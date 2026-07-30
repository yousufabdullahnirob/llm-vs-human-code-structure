package com.bank.loan.service.strategy;

import com.bank.loan.domain.RepaymentScheduleInstallment;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChargesInterestPrincipalStrategy implements RepaymentAllocationStrategy {

    @Override
    public String getName() {
        return "ChargesInterestPrincipal";
    }

    @Override
    public void allocate(List<RepaymentScheduleInstallment> installments, BigDecimal amount) {
        BigDecimal remaining = amount;

        for (RepaymentScheduleInstallment inst : installments) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;
            if (inst.isCompleted()) continue;

            // 1. Pay Charges
            BigDecimal chargesOutstanding = inst.getChargesOutstanding();
            if (chargesOutstanding.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal toPay = remaining.min(chargesOutstanding);
                inst.setChargesPaid(inst.getChargesPaid().add(toPay));
                remaining = remaining.subtract(toPay);
            }

            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;

            // 2. Pay Interest
            BigDecimal interestOutstanding = inst.getInterestOutstanding();
            if (interestOutstanding.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal toPay = remaining.min(interestOutstanding);
                inst.setInterestPaid(inst.getInterestPaid().add(toPay));
                remaining = remaining.subtract(toPay);
            }

            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;

            // 3. Pay Principal
            BigDecimal principalOutstanding = inst.getPrincipalOutstanding();
            if (principalOutstanding.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal toPay = remaining.min(principalOutstanding);
                inst.setPrincipalPaid(inst.getPrincipalPaid().add(toPay));
                remaining = remaining.subtract(toPay);
            }

            // Check completion
            if (inst.getChargesOutstanding().compareTo(BigDecimal.ZERO) == 0 &&
                inst.getInterestOutstanding().compareTo(BigDecimal.ZERO) == 0 &&
                inst.getPrincipalOutstanding().compareTo(BigDecimal.ZERO) == 0) {
                inst.setCompleted(true);
            }
        }
    }
}
