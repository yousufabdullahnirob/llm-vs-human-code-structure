package com.bank.loan;

import com.bank.loan.domain.LoanActionType;
import com.bank.loan.domain.LoanOfficer;
import com.bank.loan.dto.LoanApplicationRequest;
import com.bank.loan.dto.LoanDisbursementRequest;
import com.bank.loan.dto.RepaymentRequest;
import com.bank.loan.repository.LoanOfficerRepository;
import com.bank.loan.service.command.LoanActionDispatcher;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LoanApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedData(LoanOfficerRepository officerRepo,
                                      LoanActionDispatcher actionDispatcher) {
        return args -> {
            // Seed officers
            LoanOfficer officer1 = officerRepo.save(new LoanOfficer("John Doe", "john.doe@bank.com"));
            LoanOfficer officer2 = officerRepo.save(new LoanOfficer("Jane Smith", "jane.smith@bank.com"));

            System.out.println("Seeded Loan Officers:");
            System.out.println("ID: " + officer1.getId() + " - " + officer1.getName());
            System.out.println("ID: " + officer2.getId() + " - " + officer2.getName());

            // Seed a submitted loan
            LoanApplicationRequest req1 = new LoanApplicationRequest();
            req1.setClientOrGroupName("Acme Corp Client Group");
            req1.setGroup(true);
            req1.setLoanProduct("Micro Business Loan");
            req1.setAmount(BigDecimal.valueOf(10000));
            req1.setInterestRate(BigDecimal.valueOf(12.0));
            req1.setTerm(6);
            req1.setRepaymentFrequency("MONTHLY");
            req1.setLoanOfficerId(officer1.getId());

            Long loanId1 = actionDispatcher.dispatchSubmit(req1);
            System.out.println("Seeded Loan Application (ID: " + loanId1 + ") for Acme Corp Client Group");

            // Seed an approved loan
            LoanApplicationRequest req2 = new LoanApplicationRequest();
            req2.setClientOrGroupName("Alice Johnson");
            req2.setGroup(false);
            req2.setLoanProduct("Personal Consumer Loan");
            req2.setAmount(BigDecimal.valueOf(5000));
            req2.setInterestRate(BigDecimal.valueOf(8.5));
            req2.setTerm(12);
            req2.setRepaymentFrequency("MONTHLY");
            req2.setLoanOfficerId(officer2.getId());

            Long loanId2 = actionDispatcher.dispatchSubmit(req2);
            actionDispatcher.dispatch(loanId2, LoanActionType.APPROVE, null);
            System.out.println("Seeded Approved Loan Application (ID: " + loanId2 + ") for Alice Johnson");

            // Seed an active loan (disbursed, with schedule and transactions)
            LoanApplicationRequest req3 = new LoanApplicationRequest();
            req3.setClientOrGroupName("Bob Roberts");
            req3.setGroup(false);
            req3.setLoanProduct("Home Improvement Loan");
            req3.setAmount(BigDecimal.valueOf(12000));
            req3.setInterestRate(BigDecimal.valueOf(10.0));
            req3.setTerm(4);
            req3.setRepaymentFrequency("MONTHLY");
            req3.setLoanOfficerId(officer1.getId());

            Long loanId3 = actionDispatcher.dispatchSubmit(req3);
            actionDispatcher.dispatch(loanId3, LoanActionType.APPROVE, null);
            
            LoanDisbursementRequest disburseReq = new LoanDisbursementRequest();
            disburseReq.setDisbursementDate(LocalDate.now().minusMonths(2));
            actionDispatcher.dispatch(loanId3, LoanActionType.DISBURSE, disburseReq);
            
            // Apply repayment of 1 installment
            RepaymentRequest repayReq = new RepaymentRequest();
            repayReq.setAmount(BigDecimal.valueOf(3100.00));
            repayReq.setPaymentDate(LocalDate.now().minusMonths(1));
            actionDispatcher.dispatch(loanId3, LoanActionType.REPAY, repayReq);
            System.out.println("Seeded Active Loan (ID: " + loanId3 + ") for Bob Roberts with 1 repayment transaction made");
        };
    }
}
