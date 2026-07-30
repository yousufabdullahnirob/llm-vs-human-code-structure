package com.bank.loan.repository;

import com.bank.loan.domain.LoanActionAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanActionAuditRepository extends JpaRepository<LoanActionAudit, Long> {
}
