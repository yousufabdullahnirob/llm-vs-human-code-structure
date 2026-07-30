package com.bank.loan.service.command.impl;

import com.bank.loan.domain.Guarantor;
import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.dto.GuarantorRequest;
import com.bank.loan.service.command.LoanActionHandler;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class AddGuarantorActionHandler implements LoanActionHandler<GuarantorRequest> {

    @Override
    public LoanActionType getActionType() {
        return LoanActionType.ADD_GUARANTOR;
    }

    @Override
    public void validate(Loan loan, GuarantorRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Guarantor name is required.");
        }
        if (request.getAmountGuaranteed() == null || request.getAmountGuaranteed().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Guaranteed amount must be positive.");
        }
    }

    @Override
    public void execute(Loan loan, GuarantorRequest request) {
        Guarantor guarantor = new Guarantor(request.getName(), request.getRelationship(), request.getAmountGuaranteed(), loan);
        loan.addGuarantor(guarantor);
    }
}
