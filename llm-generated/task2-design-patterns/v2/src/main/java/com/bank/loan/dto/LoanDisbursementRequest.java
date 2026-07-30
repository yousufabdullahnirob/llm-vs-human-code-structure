package com.bank.loan.dto;

import java.time.LocalDate;

public class LoanDisbursementRequest {
    private LocalDate disbursementDate;

    public LocalDate getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }
}
