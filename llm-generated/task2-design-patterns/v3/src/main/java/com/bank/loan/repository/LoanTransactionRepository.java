package com.bank.loan.repository;

import com.bank.loan.domain.LoanTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanTransactionRepository extends JpaRepository<LoanTransaction, Long> {
}
