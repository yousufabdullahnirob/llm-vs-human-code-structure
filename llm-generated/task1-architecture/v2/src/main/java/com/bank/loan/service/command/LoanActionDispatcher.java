package com.bank.loan.service.command;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanActionAudit;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.repository.LoanActionAuditRepository;
import com.bank.loan.repository.LoanRepository;
import com.bank.loan.service.status.LoanStatusState;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoanActionDispatcher {

    private final Map<LoanActionType, LoanActionHandler<?>> handlerMap = new HashMap<>();
    private final Map<LoanStatus, LoanStatusState> stateMap = new HashMap<>();

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanActionAuditRepository auditRepository;

    @Autowired
    public LoanActionDispatcher(List<LoanActionHandler<?>> handlers, List<LoanStatusState> statusStates) {
        for (LoanActionHandler<?> handler : handlers) {
            handlerMap.put(handler.getActionType(), handler);
        }
        for (LoanStatusState state : statusStates) {
            stateMap.put(state.getStatus(), state);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Long dispatchSubmit(T requestPayload) {
        LoanActionHandler<T> handler = (LoanActionHandler<T>) handlerMap.get(LoanActionType.SUBMIT);
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for action: SUBMIT");
        }

        Loan loan = new Loan();
        LocalDateTime now = LocalDateTime.now();
        
        try {
            handler.validate(loan, requestPayload);
            handler.execute(loan, requestPayload);
            
            Loan savedLoan = loanRepository.save(loan);
            
            // Record audit log
            auditRepository.save(new LoanActionAudit(
                    savedLoan.getId(),
                    LoanActionType.SUBMIT,
                    now,
                    "SUCCESS",
                    null,
                    "Loan application submitted for client: " + savedLoan.getClientOrGroupName()
            ));
            
            return savedLoan.getId();
        } catch (Exception ex) {
            auditRepository.save(new LoanActionAudit(
                    null,
                    LoanActionType.SUBMIT,
                    now,
                    "FAILED",
                    ex.getMessage(),
                    "Submit failed during processing."
            ));
            throw ex;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void dispatch(Long loanId, LoanActionType actionType, T requestPayload) {
        LoanActionHandler<T> handler = (LoanActionHandler<T>) handlerMap.get(actionType);
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for action: " + actionType);
        }

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found with ID: " + loanId));

        LocalDateTime now = LocalDateTime.now();

        try {
            // Validate status rules (allowed transitions)
            validateStateTransition(loan.getStatus(), actionType);

            handler.validate(loan, requestPayload);
            handler.execute(loan, requestPayload);

            loanRepository.save(loan);

            // Record audit log
            auditRepository.save(new LoanActionAudit(
                    loanId,
                    actionType,
                    now,
                    "SUCCESS",
                    null,
                    "Action " + actionType + " executed successfully."
            ));
        } catch (Exception ex) {
            auditRepository.save(new LoanActionAudit(
                    loanId,
                    actionType,
                    now,
                    "FAILED",
                    ex.getMessage(),
                    "Failed executing action " + actionType
            ));
            throw ex;
        }
    }

    private void validateStateTransition(LoanStatus status, LoanActionType actionType) {
        LoanStatusState state = stateMap.get(status);
        if (state == null) {
            throw new IllegalStateException("No status state handler configured for status: " + status);
        }
        if (!state.isActionAllowed(actionType)) {
            throw new IllegalStateException("Action " + actionType + " is not allowed on a loan in status " + status);
        }
    }
}
