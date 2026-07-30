package com.bank.loan.controller;

import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanStatus;
import com.bank.loan.dto.ChargeRequest;
import com.bank.loan.dto.GuarantorRecoveryRequest;
import com.bank.loan.dto.GuarantorRequest;
import com.bank.loan.dto.LoanApplicationRequest;
import com.bank.loan.dto.LoanDisbursementRequest;
import com.bank.loan.dto.ReassignOfficerRequest;
import com.bank.loan.dto.RepaymentRequest;
import com.bank.loan.dto.response.LoanResponse;
import com.bank.loan.service.LoanReadService;
import com.bank.loan.service.command.LoanActionDispatcher;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanReadService loanReadService;

    @Autowired
    private LoanActionDispatcher loanActionDispatcher;

    @PostMapping
    public ResponseEntity<LoanResponse> applyForLoan(@RequestBody LoanApplicationRequest request) {
        Long loanId = loanActionDispatcher.dispatchSubmit(request);
        LoanResponse response = loanReadService.getLoanDetails(loanId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<LoanResponse>> getFilteredLoans(
            @RequestParam(required = false) LoanStatus status,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) Long officerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<LoanResponse> loans = loanReadService.getFilteredLoans(status, clientName, officerId, PageRequest.of(page, size));
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getLoanDetails(@PathVariable Long id) {
        LoanResponse response = loanReadService.getLoanDetails(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<LoanResponse> approveLoan(@PathVariable Long id) {
        loanActionDispatcher.dispatch(id, LoanActionType.APPROVE, null);
        LoanResponse response = loanReadService.getLoanDetails(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<LoanResponse> rejectLoan(@PathVariable Long id) {
        loanActionDispatcher.dispatch(id, LoanActionType.REJECT, null);
        LoanResponse response = loanReadService.getLoanDetails(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<LoanResponse> withdrawLoan(@PathVariable Long id) {
        loanActionDispatcher.dispatch(id, LoanActionType.WITHDRAW, null);
        LoanResponse response = loanReadService.getLoanDetails(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/disburse")
    public ResponseEntity<LoanResponse> disburseLoan(@PathVariable Long id, @RequestBody LoanDisbursementRequest request) {
        loanActionDispatcher.dispatch(id, LoanActionType.DISBURSE, request);
        LoanResponse response = loanReadService.getLoanDetails(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/undo-disburse")
    public ResponseEntity<LoanResponse> undoDisbursement(@PathVariable Long id) {
        loanActionDispatcher.dispatch(id, LoanActionType.UNDO_DISBURSE, null);
        LoanResponse response = loanReadService.getLoanDetails(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reassign-officer")
    public ResponseEntity<LoanResponse> reassignOfficer(@PathVariable Long id, @RequestBody ReassignOfficerRequest request) {
        loanActionDispatcher.dispatch(id, LoanActionType.REASSIGN_OFFICER, request);
        LoanResponse response = loanReadService.getLoanDetails(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/charges")
    public ResponseEntity<LoanResponse> addCharge(@PathVariable Long id, @RequestBody ChargeRequest request) {
        loanActionDispatcher.dispatch(id, LoanActionType.ADD_CHARGE, request);
        LoanResponse response = loanReadService.getLoanDetails(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/guarantors")
    public ResponseEntity<LoanResponse> addGuarantor(@PathVariable Long id, @RequestBody GuarantorRequest request) {
        loanActionDispatcher.dispatch(id, LoanActionType.ADD_GUARANTOR, request);
        LoanResponse response = loanReadService.getLoanDetails(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/repayments")
    public ResponseEntity<LoanResponse> makeRepayment(@PathVariable Long id, @RequestBody RepaymentRequest request) {
        loanActionDispatcher.dispatch(id, LoanActionType.REPAY, request);
        LoanResponse response = loanReadService.getLoanDetails(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/guarantor-recovery")
    public ResponseEntity<LoanResponse> recoverFromGuarantor(@PathVariable Long id, @RequestBody GuarantorRecoveryRequest request) {
        loanActionDispatcher.dispatch(id, LoanActionType.RECOVER_GUARANTOR, request);
        LoanResponse response = loanReadService.getLoanDetails(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/write-off")
    public ResponseEntity<LoanResponse> writeOffLoan(@PathVariable Long id) {
        loanActionDispatcher.dispatch(id, LoanActionType.WRITE_OFF, null);
        LoanResponse response = loanReadService.getLoanDetails(id);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
