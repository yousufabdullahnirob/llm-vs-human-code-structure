package com.bank.loan.repository;

import com.bank.loan.domain.Charge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeRepository extends JpaRepository<Charge, Long> {
}
