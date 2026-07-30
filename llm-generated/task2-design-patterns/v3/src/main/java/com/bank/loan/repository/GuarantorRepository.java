package com.bank.loan.repository;

import com.bank.loan.domain.Guarantor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuarantorRepository extends JpaRepository<Guarantor, Long> {
}
