package com.bank.loan.repository;

import com.bank.loan.domain.LoanOfficer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanOfficerRepository extends JpaRepository<LoanOfficer, Long> {
}
