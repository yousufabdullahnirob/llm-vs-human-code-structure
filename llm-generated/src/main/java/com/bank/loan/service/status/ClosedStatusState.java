package com.bank.loan.service.status;

import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import org.springframework.stereotype.Component;

@Component
public class ClosedStatusState implements LoanStatusState {

    @Override
    public LoanStatus getStatus() {
        return LoanStatus.CLOSED;
    }

    @Override
    public boolean isActionAllowed(LoanActionType actionType) {
        return false;
    }
}
