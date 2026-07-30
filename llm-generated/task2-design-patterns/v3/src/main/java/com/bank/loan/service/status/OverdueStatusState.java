package com.bank.loan.service.status;

import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import org.springframework.stereotype.Component;

@Component
public class OverdueStatusState implements LoanStatusState {

    @Override
    public LoanStatus getStatus() {
        return LoanStatus.OVERDUE;
    }

    @Override
    public boolean isActionAllowed(LoanActionType actionType) {
        return actionType == LoanActionType.REPAY || 
               actionType == LoanActionType.ADD_CHARGE || 
               actionType == LoanActionType.ADD_GUARANTOR || 
               actionType == LoanActionType.RECOVER_GUARANTOR ||
               actionType == LoanActionType.REASSIGN_OFFICER ||
               actionType == LoanActionType.WRITE_OFF ||
               actionType == LoanActionType.FREEZE;
    }
}
