package com.bank.loan.service.status;

import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;

public interface LoanStatusState {
    LoanStatus getStatus();
    boolean isActionAllowed(LoanActionType actionType);
}
