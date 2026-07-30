package com.bank.loan;

import com.bank.loan.domain.LoanActionAudit;
import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanOfficer;
import com.bank.loan.dto.ChargeRequest;
import com.bank.loan.dto.GuarantorRecoveryRequest;
import com.bank.loan.dto.GuarantorRequest;
import com.bank.loan.dto.LoanApplicationRequest;
import com.bank.loan.dto.LoanDisbursementRequest;
import com.bank.loan.dto.ReassignOfficerRequest;
import com.bank.loan.dto.RepaymentRequest;
import com.bank.loan.dto.response.LoanResponse;
import com.bank.loan.repository.LoanActionAuditRepository;
import com.bank.loan.repository.LoanOfficerRepository;
import com.bank.loan.service.LoanReadService;
import com.bank.loan.service.command.LoanActionDispatcher;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoanApplicationTests {

    @Autowired
    private LoanReadService loanReadService;

    @Autowired
    private LoanActionDispatcher actionDispatcher;

    @Autowired
    private LoanOfficerRepository officerRepository;

    @Autowired
    private LoanActionAuditRepository auditRepository;

    @Test
    public void testCompleteLoanLifecycleWithCommandAndStrategy() {
        // 1. Create a loan officer
        LoanOfficer officer = officerRepository.save(new LoanOfficer("Test Officer", "test@bank.com"));
        Assertions.assertNotNull(officer.getId());

        // 2. Submit application
        LoanApplicationRequest submitReq = new LoanApplicationRequest();
        submitReq.setClientOrGroupName("John Client");
        submitReq.setGroup(false);
        submitReq.setLoanProduct("Micro Loan"); // Resolves to standard Charges -> Interest -> Principal strategy
        submitReq.setAmount(BigDecimal.valueOf(1000.00));
        submitReq.setInterestRate(BigDecimal.valueOf(12.00));
        submitReq.setTerm(5);
        submitReq.setRepaymentFrequency("MONTHLY");
        submitReq.setLoanOfficerId(officer.getId());

        Long loanId = actionDispatcher.dispatchSubmit(submitReq);
        Assertions.assertNotNull(loanId);
        
        LoanResponse response = loanReadService.getLoanDetails(loanId);
        Assertions.assertEquals("SUBMITTED", response.getStatus());
        Assertions.assertEquals("ChargesInterestPrincipal", response.getAllocationStrategy());

        // 3. Approve loan
        actionDispatcher.dispatch(loanId, LoanActionType.APPROVE, null);
        response = loanReadService.getLoanDetails(loanId);
        Assertions.assertEquals("APPROVED", response.getStatus());

        // 4. Disburse loan
        LocalDate disbursementDate = LocalDate.now();
        LoanDisbursementRequest disburseReq = new LoanDisbursementRequest();
        disburseReq.setDisbursementDate(disbursementDate);
        actionDispatcher.dispatch(loanId, LoanActionType.DISBURSE, disburseReq);
        response = loanReadService.getLoanDetails(loanId);
        Assertions.assertEquals("ACTIVE", response.getStatus());

        // 5. Add a charge
        ChargeRequest chargeReq = new ChargeRequest();
        chargeReq.setName("Processing Fee");
        chargeReq.setAmount(BigDecimal.valueOf(50.00));
        actionDispatcher.dispatch(loanId, LoanActionType.ADD_CHARGE, chargeReq);
        response = loanReadService.getLoanDetails(loanId);
        Assertions.assertEquals(0, response.getTotalCharges().compareTo(BigDecimal.valueOf(50.00)));

        // 6. Make a repayment
        RepaymentRequest repayReq = new RepaymentRequest();
        repayReq.setAmount(BigDecimal.valueOf(300.00));
        repayReq.setPaymentDate(LocalDate.now());
        actionDispatcher.dispatch(loanId, LoanActionType.REPAY, repayReq);
        response = loanReadService.getLoanDetails(loanId);
        // Charges should be paid first (50.00 paid on charges)
        Assertions.assertEquals(0, response.getPaidCharges().compareTo(BigDecimal.valueOf(50.00)));

        // 7. Add a guarantor
        GuarantorRequest guarantorReq = new GuarantorRequest();
        guarantorReq.setName("Guarantor G");
        guarantorReq.setRelationship("Friend");
        guarantorReq.setAmountGuaranteed(BigDecimal.valueOf(1000.00));
        actionDispatcher.dispatch(loanId, LoanActionType.ADD_GUARANTOR, guarantorReq);
        response = loanReadService.getLoanDetails(loanId);
        Assertions.assertEquals(1, response.getGuarantors().size());
        Long guarantorId = response.getGuarantors().get(0).getId();

        // 8. Recover from guarantor
        BigDecimal remainingOutstanding = response.getTotalOutstanding();
        GuarantorRecoveryRequest recoverReq = new GuarantorRecoveryRequest();
        recoverReq.setGuarantorId(guarantorId);
        recoverReq.setAmount(remainingOutstanding);
        recoverReq.setPaymentDate(LocalDate.now());
        actionDispatcher.dispatch(loanId, LoanActionType.RECOVER_GUARANTOR, recoverReq);
        
        response = loanReadService.getLoanDetails(loanId);
        Assertions.assertEquals("CLOSED", response.getStatus());

        // 9. Reassign loan officer
        LoanOfficer newOfficer = officerRepository.save(new LoanOfficer("New Officer", "new@bank.com"));
        ReassignOfficerRequest reassignReq = new ReassignOfficerRequest();
        reassignReq.setOfficerId(newOfficer.getId());
        actionDispatcher.dispatch(loanId, LoanActionType.REASSIGN_OFFICER, reassignReq);
        response = loanReadService.getLoanDetails(loanId);
        Assertions.assertEquals("New Officer", response.getLoanOfficerName());

        // 10. Check that state machine transition guard throws exception
        // E.g. trying to approve a CLOSED loan should fail
        Assertions.assertThrows(IllegalStateException.class, () -> {
            actionDispatcher.dispatch(loanId, LoanActionType.APPROVE, null);
        });

        // 11. Check audit log records exist in DB
        List<LoanActionAudit> audits = auditRepository.findAll();
        Assertions.assertTrue(audits.size() > 0);
        // Verify we have a failed attempt audited as well
        boolean hasFailedAudit = audits.stream()
                .anyMatch(a -> "FAILED".equals(a.getStatus()) && a.getActionType() == LoanActionType.APPROVE);
        Assertions.assertTrue(hasFailedAudit, "Failed state transition must be audited");
    }

    @Test
    public void testAlternativeRepaymentAllocationStrategy() {
        LoanOfficer officer = officerRepository.save(new LoanOfficer("Strategy Officer", "strat@bank.com"));

        // Create loan with product "Personal Consumer Loan" -> Resolves to Principal -> Interest -> Charges
        LoanApplicationRequest submitReq = new LoanApplicationRequest();
        submitReq.setClientOrGroupName("Jane Client");
        submitReq.setGroup(false);
        submitReq.setLoanProduct("Personal Consumer Loan");
        submitReq.setAmount(BigDecimal.valueOf(1000.00));
        submitReq.setInterestRate(BigDecimal.valueOf(12.00));
        submitReq.setTerm(5);
        submitReq.setRepaymentFrequency("MONTHLY");
        submitReq.setLoanOfficerId(officer.getId());

        Long loanId = actionDispatcher.dispatchSubmit(submitReq);
        LoanResponse response = loanReadService.getLoanDetails(loanId);
        Assertions.assertEquals("PrincipalInterestCharges", response.getAllocationStrategy());

        actionDispatcher.dispatch(loanId, LoanActionType.APPROVE, null);
        
        LoanDisbursementRequest disburseReq = new LoanDisbursementRequest();
        disburseReq.setDisbursementDate(LocalDate.now());
        actionDispatcher.dispatch(loanId, LoanActionType.DISBURSE, disburseReq);

        // Add charge of 50.00
        ChargeRequest chargeReq = new ChargeRequest();
        chargeReq.setName("Processing Fee");
        chargeReq.setAmount(BigDecimal.valueOf(50.00));
        actionDispatcher.dispatch(loanId, LoanActionType.ADD_CHARGE, chargeReq);

        // Repay 200.00
        RepaymentRequest repayReq = new RepaymentRequest();
        repayReq.setAmount(BigDecimal.valueOf(200.00));
        repayReq.setPaymentDate(LocalDate.now());
        actionDispatcher.dispatch(loanId, LoanActionType.REPAY, repayReq);

        response = loanReadService.getLoanDetails(loanId);
        
        // Under PrincipalInterestCharges strategy:
        // Principal outstanding for installment 1 is paid off first, interest and charges paid should remain 0
        Assertions.assertEquals(0, response.getPaidPrincipal().compareTo(BigDecimal.valueOf(200.00)));
        Assertions.assertEquals(0, response.getPaidCharges().compareTo(BigDecimal.ZERO));
        Assertions.assertEquals(0, response.getPaidInterest().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void testFreezeAndUnfreezeLifecycle() {
        LoanOfficer officer = officerRepository.save(new LoanOfficer("Freeze Officer", "freeze@bank.com"));

        LoanApplicationRequest submitReq = new LoanApplicationRequest();
        submitReq.setClientOrGroupName("Frozen Client");
        submitReq.setGroup(false);
        submitReq.setLoanProduct("Micro Loan");
        submitReq.setAmount(BigDecimal.valueOf(1000.00));
        submitReq.setInterestRate(BigDecimal.valueOf(12.00));
        submitReq.setTerm(5);
        submitReq.setRepaymentFrequency("MONTHLY");
        submitReq.setLoanOfficerId(officer.getId());

        Long loanId = actionDispatcher.dispatchSubmit(submitReq);
        actionDispatcher.dispatch(loanId, LoanActionType.APPROVE, null);

        LoanDisbursementRequest disburseReq = new LoanDisbursementRequest();
        disburseReq.setDisbursementDate(LocalDate.now());
        actionDispatcher.dispatch(loanId, LoanActionType.DISBURSE, disburseReq);

        LoanResponse response = loanReadService.getLoanDetails(loanId);
        Assertions.assertEquals("ACTIVE", response.getStatus());

        // 1. Freeze the loan
        actionDispatcher.dispatch(loanId, LoanActionType.FREEZE, null);
        response = loanReadService.getLoanDetails(loanId);
        Assertions.assertEquals("FROZEN", response.getStatus());

        // 2. Repayment should fail while FROZEN
        RepaymentRequest repayReq = new RepaymentRequest();
        repayReq.setAmount(BigDecimal.valueOf(100.00));
        repayReq.setPaymentDate(LocalDate.now());
        Assertions.assertThrows(IllegalStateException.class, () -> {
            actionDispatcher.dispatch(loanId, LoanActionType.REPAY, repayReq);
        });

        // 3. Add Charge should fail while FROZEN
        ChargeRequest chargeReq = new ChargeRequest();
        chargeReq.setName("Late Fee");
        chargeReq.setAmount(BigDecimal.valueOf(10.00));
        Assertions.assertThrows(IllegalStateException.class, () -> {
            actionDispatcher.dispatch(loanId, LoanActionType.ADD_CHARGE, chargeReq);
        });

        // 4. Unfreeze the loan
        actionDispatcher.dispatch(loanId, LoanActionType.UNFREEZE, null);
        response = loanReadService.getLoanDetails(loanId);
        Assertions.assertEquals("ACTIVE", response.getStatus());

        // 5. Unfreeze should fail on ACTIVE loan
        Assertions.assertThrows(IllegalStateException.class, () -> {
            actionDispatcher.dispatch(loanId, LoanActionType.UNFREEZE, null);
        });
    }
}
