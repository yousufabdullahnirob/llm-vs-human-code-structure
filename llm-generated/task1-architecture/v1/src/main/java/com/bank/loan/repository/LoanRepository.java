package com.bank.loan.repository;

import com.bank.loan.domain.Loan;
import com.bank.loan.domain.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("SELECT l FROM Loan l WHERE " +
           "(:status IS NULL OR l.status = :status) AND " +
           "(:clientName IS NULL OR LOWER(l.clientOrGroupName) LIKE LOWER(CONCAT('%', :clientName, '%'))) AND " +
           "(:officerId IS NULL OR l.loanOfficer.id = :officerId)")
    Page<Loan> findFilteredLoans(
        @Param("status") LoanStatus status,
        @Param("clientName") String clientName,
        @Param("officerId") Long officerId,
        Pageable pageable
    );
}
