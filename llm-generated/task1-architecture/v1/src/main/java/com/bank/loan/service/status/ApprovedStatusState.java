package com.bank.loan.service.status;

import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import org.springframework.stereotype.Component;

@Component
public class ApprovedStatusState implements LoanStatusState {

    @Override
    public LoanStatus getStatus() {
        return LoanStatus.APPROVED;
    }

    @Override
    public boolean isActionAllowed(LoanActionType actionType) {
        return actionType == LoanActionType.DISBURSE || 
               actionType == LoanActionType.WITHDRAW || 
               actionType == LoanActionType.REASSIGN_OFFICER ||
               actionType == LoanActionType.ADD_CHARGE ||
               actionType == LoanActionType.ADD_GUARANTOR;
    }
}
