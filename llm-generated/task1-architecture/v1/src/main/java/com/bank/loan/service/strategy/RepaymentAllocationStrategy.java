package com.bank.loan.service.strategy;

import com.bank.loan.domain.RepaymentScheduleInstallment;
import java.math.BigDecimal;
import java.util.List;

public interface RepaymentAllocationStrategy {
    void allocate(List<RepaymentScheduleInstallment> installments, BigDecimal amount);
    String getName();
}
