package com.bank.loan.service.status;

import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import org.springframework.stereotype.Component;

@Component
public class SubmittedStatusState implements LoanStatusState {

    @Override
    public LoanStatus getStatus() {
        return LoanStatus.SUBMITTED;
    }

    @Override
    public boolean isActionAllowed(LoanActionType actionType) {
        return actionType == LoanActionType.APPROVE || 
               actionType == LoanActionType.REJECT || 
               actionType == LoanActionType.WITHDRAW || 
               actionType == LoanActionType.REASSIGN_OFFICER ||
               actionType == LoanActionType.ADD_CHARGE ||
               actionType == LoanActionType.ADD_GUARANTOR;
    }
}
