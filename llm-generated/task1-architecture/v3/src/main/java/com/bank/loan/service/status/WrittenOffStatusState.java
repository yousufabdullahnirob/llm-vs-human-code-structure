package com.bank.loan.service.status;

import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import org.springframework.stereotype.Component;

@Component
public class WrittenOffStatusState implements LoanStatusState {

    @Override
    public LoanStatus getStatus() {
        return LoanStatus.WRITTEN_OFF;
    }

    @Override
    public boolean isActionAllowed(LoanActionType actionType) {
        return false;
    }
}
